import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.InterruptedException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class EspApi {
    EspConnection conn;
    private String username;
    private String password;

    private Elements tElements;
    EspTranscript transcript;

    private Elements classElements;
    private ArrayList<EspClass> classes;
    private int longestAssignment = 0;

    public EspApi() {
        conn = new EspConnection();
    }

    public EspTranscript getTranscript() {
        return this.transcript;
    }

    public ArrayList<EspClass> getClasses() {
        return this.classes;
    }

    public int getLongestAssignment() {
        return longestAssignment;
    }

    public void fetchClasses(String username, String password) throws IOException {
        this.username = username;
        this.password = password;

        conn.logOn(username, password);
        String html = conn.getAssignments();
        Document doc = Jsoup.parse(html);
        this.classElements = doc.getElementsByClass("AssignmentClass");
    }

    public void parseClasses() throws EspException {
        classes = new ArrayList<>(10);

        Pattern gradePattern = Pattern.compile("\\d+.\\d{2}");
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Matcher matcher;
        ArrayList<EspAssignment> assignments;
        String className = null, classId, gradeString = null;
        Float classGrade;
        int classNameDelimiterIndex = 0;

        for(Element classElement : this.classElements) {
            Date dateDue, dateAssigned;
            String assignmentName, category;
            Float[] stats;
            String percentageString;
            Float percentage;
            Elements assignmentElements, cols, categoryElements;
            assignments = new ArrayList<>(20);

            // BEGIN parsing general class information.

            try {
                className = classElement.select(
                        "a.sg-header-heading").first().text();
                classNameDelimiterIndex = className.indexOf('-');
                classId = className.substring(0, classNameDelimiterIndex - 1);
                className = className.substring(classNameDelimiterIndex + 1);

                gradeString = classElement.select(
                       "span[id^=plnMain_rptAssigmnetsByCourse_lblHdrAverage_]")
                       .first().text();
                matcher = gradePattern.matcher(gradeString);
                classGrade = matcher.find() ?
                        Float.parseFloat(gradeString.substring(
                        matcher.start(), matcher.end())) : null;

                assignmentElements = classElement.select(
                        "table[id^=plnMain_rptAssigmnetsByCourse_dgCourseAssignments_] > tbody > tr.sg-asp-table-data-row");
            } catch(NullPointerException e) {
                String element;
                if(className == null) {
                    element = "className";
                } else if(gradeString == null) {
                    element = "gradeString";
                } else {
                    element = "assignmentElements";
                }
                throw new EspException("Class element failure when selecting " + element,
                    "ElemSel" + element.substring(0, 1).toUpperCase());
            }

            // END parsing general class information.
            // BEGIN parsing class assignments.

            for(Element assignmentElement : assignmentElements) {
                cols = assignmentElement.select("td");
                if(cols.size() != 10) {
                    throw new EspException("Assignment column size mismatch: expected 10, got " + cols.size(), "ColSize");
                }

                try {
                    dateDue = dateFormat.parse(cols.get(0).text());
                } catch(ParseException e) {
                    dateDue = null;
                }
                try {
                    dateAssigned = dateFormat.parse(cols.get(1).text());
                } catch(ParseException e) {
                    dateAssigned = null;
                }
                assignmentName = cols.get(2).text();
                assignmentName = assignmentName.substring(0, (assignmentName.length() - 2));

                if(assignmentName.length() > longestAssignment) longestAssignment = assignmentName.length();

                category = cols.get(3).text();
                stats = new Float[5];
                for(int i = 0; i < stats.length; i++) {
                    try {
                        stats[i] = Float.parseFloat(cols.get(i + 4).text());
                    } catch(NumberFormatException e) {
                        stats[i] = null;
                    }
                }
                percentageString = cols.get(9).text();
                try {
                    percentage = Float.parseFloat(percentageString.substring(0, percentageString.length() - 1));
                } catch(StringIndexOutOfBoundsException | NumberFormatException e) {
                    percentage = null;
                }
                assignments.add(new EspAssignment(dateDue, dateAssigned, assignmentName, category, stats, percentage));
            }

            // END parsing class assignments.
            // BEGIN parsing class assignment categories.

            Float points, maximumPoints, percent, categoryWeight = null, categoryPoints = null;
            ArrayList<EspAssignmentCategory> assignmentCategories = new ArrayList<>(4);

            categoryElements = classElement.select(
                    "table[id^=plnMain_rptAssigmnetsByCourse_dgCourseCategories_] > tbody > tr.sg-asp-table-data-row");
            for(Element categoryElement : categoryElements) {
                cols = categoryElement.select("td");
                try {
                    category = cols.get(0).text();
                    try {
                        points = Float.parseFloat(cols.get(1).text());
                    } catch(NumberFormatException e) {
                        points = null;
                    }
                    try {
                        maximumPoints = Float.parseFloat(cols.get(2).text());
                    } catch(NumberFormatException e) {
                        maximumPoints = null;
                    }
                    try {
                        percentageString = cols.get(3).text();
                        percent = Float.parseFloat(percentageString.substring(0, percentageString.length() - 1));
                    } catch(StringIndexOutOfBoundsException | NumberFormatException e) {
                        percent = null;
                    }

                    if(cols.size() == 6) {
                        try {
                            categoryWeight = Float.parseFloat(cols.get(4).text());
                        } catch(NumberFormatException e) {
                            categoryWeight = null;
                        }
                        try {
                            categoryPoints = Float.parseFloat(cols.get(5).text());
                        } catch(NumberFormatException e) {
                            categoryPoints = null;
                        }
                    }
                    assignmentCategories.add(new EspAssignmentCategory(category, points, maximumPoints, percent, categoryWeight, categoryPoints));
                } catch(NullPointerException | IndexOutOfBoundsException e) {
                    throw new EspException("Category col size mismatch expected 4 or 6, got: " + cols.size(), "CatColSize");
                }
            }

            // END parsing class assignment categories.

            classes.add(new EspClass(className, classId, classGrade, assignments, assignmentCategories));
        }
    }

    public void fetchTranscript(String username, String password) throws IOException, EspException {
        this.username = username;
        this.password = password;

        conn.logOn(username, password);
        String html = conn.getTranscript();
        Document doc = Jsoup.parse(html);
        this.tElements = doc.getElementsByClass("sg-transcript-group");

        Float tGpa;
        Integer tRankNum, tRankDenom;
        ArrayList<EspTranscriptGroup> tGroups = new ArrayList<>(4);

        try {
            tGpa = Float.parseFloat(doc.getElementById("plnMain_rpTranscriptGroup_lblGPACum1").text());
            String rankString = doc.getElementById("plnMain_rpTranscriptGroup_lblGPARank1").text();
            int slashIndex = rankString.indexOf('/');
            tRankNum = Integer.parseInt(rankString.substring(0, slashIndex - 1));
            tRankDenom = Integer.parseInt(rankString.substring(slashIndex + 2));
        } catch(Exception e) {
            throw new EspException("Error parsing GPA/Rank.", "TsGpaRank");
        }
        this.transcript = new EspTranscript(tGpa, tRankNum, tRankDenom, tGroups);
    }

    public void parseTranscript() throws EspException {
        for(Element te : this.tElements) {
            String year = te.selectFirst("span[id^=plnMain_rpTranscriptGroup_lblYearValue_]").text();
            Integer grade = Integer.parseInt(te.selectFirst(
                "span[id^=plnMain_rpTranscriptGroup_lblGradeValue_]").text());
            String building = te.selectFirst("span[id^=plnMain_rpTranscriptGroup_lblBuildingValue_]").text();

            ArrayList<EspTranscriptCourse> tCourses = new ArrayList<>(10);
            Elements tClassElements = te.select(
                "table[id^=plnMain_rpTranscriptGroup_dgCourses_] > tbody > tr.sg-asp-table-data-row");
            for(Element tClassElement : tClassElements) {
                Elements cs = tClassElement.children();
                String id = cs.get(0).text();
                String desc = cs.get(1).text();
                Integer pct = null;
                try {
                    pct = Integer.parseInt(cs.get(2).text());
                } catch(NumberFormatException e) {}
                String lg = cs.get(3).text();
                Float cdt = Float.parseFloat(cs.get(4).text());
                tCourses.add(new EspTranscriptCourse(id, desc, pct, lg, cdt));
            }

            Elements brCels = te.select("table[style=float:right] > tbody > tr > td");
            Float gpa = null;
            try {
                gpa = Float.parseFloat(brCels.get(1).text());
            } catch(NumberFormatException e) {}
            Float tCd = Float.parseFloat(brCels.get(3).text());

            this.transcript.getGroups().add(new EspTranscriptGroup(year, grade, building, tCourses, gpa, tCd));
        }
    }
}
