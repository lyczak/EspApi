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

public class EspConnection {
    private static EspConnection instance;

    private static final int CLASS_PREFIX_LENGTH = 9;
    private static final int CLASS_ID_LENGTH = 4;

    private final String SCRIPT_DIRECTORY;
    private String BASE_URL = "https://hac.pvsd.org/HomeAccess";
    private String username;
    private String password;
    private Elements classElements;

    private ArrayList<EspClass> classes;
    private int longestAssignment = 0;

    public static EspConnection getInstance() {
        if(instance == null) instance = new EspConnection();
        return instance;
    }

    private EspConnection() {
        this.SCRIPT_DIRECTORY = this.getClass().getProtectionDomain()
                .getCodeSource().getLocation().toString().substring(5);
    }

    public ArrayList<EspClass> getClasses() {
        return this.classes;
    }

    public int getLongestAssignment() {
        return longestAssignment;
    }

    public void logOn(String username, String password) throws IOException, InterruptedException {
        this.username = username;
        this.password = password;
        Process loginProcess = new ProcessBuilder(SCRIPT_DIRECTORY + "LogOn.sh",
                BASE_URL, username, password).start();
        loginProcess.waitFor();
    }

    public void fetchClasses() throws IOException {
        Process assignmentsProcess = new ProcessBuilder(
                SCRIPT_DIRECTORY + "Assignments.sh", BASE_URL).start();

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(
                assignmentsProcess.getInputStream()));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
            builder.append(System.getProperty("line.separator"));
        }

        String html = builder.toString();
        Document doc = Jsoup.parse(html);
        this.classElements = doc.getElementsByClass("AssignmentClass");
    }

    public void parseClasses() {
        classes = new ArrayList<>(10);

        Pattern gradePattern = Pattern.compile("\\d+.\\d{2}");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Matcher matcher;
        ArrayList<EspAssignment> assignments;
        String className, classId, gradeString;
        Float classGrade;

        for(Element classElement : this.classElements) {
            className = classElement.select(
                    "a.sg-header-heading").first().text();
            classId = className.substring(0, CLASS_ID_LENGTH);
            className = className.substring(CLASS_PREFIX_LENGTH);

            gradeString = classElement.select(
                   "span[id^=plnMain_rptAssigmnetsByCourse_lblHdrAverage_]")
                   .first().text();
            matcher = gradePattern.matcher(gradeString);
            classGrade = matcher.find() ?
                    Float.parseFloat(gradeString.substring(
                    matcher.start(), matcher.end())) : null;

            Date dateDue, dateAssigned;
            String assignmentName, category;
            Float[] stats;
            String percentageString;
            Float percentage;
            Elements assignmentElements = classElement.select(
                    "table[id^=plnMain_rptAssigmnetsByCourse_dgCourseAssignments_] > tbody > tr.sg-asp-table-data-row");
            Elements cols;
            assignments = new ArrayList<>(20);
            for(Element assignmentElement : assignmentElements) {
                cols = assignmentElement.select("td");
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
                stats = new Float[6];
                for(int i = 0; i < stats.length; i++) {
                    try {
                        stats[i] = Float.parseFloat(cols.get(i + 4).text());
                    } catch(NumberFormatException e) {
                        stats[i] = null;
                    }
                }
                percentageString = cols.get(10).text();
                try {
                    percentage = Float.parseFloat(percentageString.substring(0, percentageString.length() - 1));
                } catch(StringIndexOutOfBoundsException | NumberFormatException e) {
                    percentage = null;
                }
                assignments.add(new EspAssignment(dateDue, dateAssigned, assignmentName, category, stats, percentage));
            }

            classes.add(new EspClass(className, classId, classGrade, assignments));
        }
    }
}
