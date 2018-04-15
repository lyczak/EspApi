import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
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

    private final String SCRIPT_DIRECTORY;
    private String BASE_URL = "https://hac.pvsd.org/HomeAccess";
    private String username;
    private String password;
    private Elements classElements;

    @FunctionalInterface
    interface ClassGradeProcessor {
        void process(String className, Float grade);
    }

    public static EspConnection getInstance() {
        if(instance == null) instance = new EspConnection();
        return instance;
    }

    private EspConnection() {
        this.SCRIPT_DIRECTORY = this.getClass().getProtectionDomain()
                .getCodeSource().getLocation().toString().substring(5);
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

    public void processClassGrades(ClassGradeProcessor processor) {
        Pattern gradePattern = Pattern.compile("\\d+.\\d{2}");
        Matcher matcher = null;
        String className = "";
        String gradeString = "";
        Float grade = null;

        for(Element classElement : this.classElements) {
            className = classElement.select(
                    "a.sg-header-heading").first().text();
            className = className.substring(CLASS_PREFIX_LENGTH);

            gradeString = classElement.select(
                   "span[id^=plnMain_rptAssigmnetsByCourse_lblHdrAverage_]")
                   .first().text();
            matcher = gradePattern.matcher(gradeString);
            grade = matcher.find() ?
                    Float.parseFloat(gradeString.substring(
                    matcher.start(), matcher.end())) : null;

            processor.process(className, grade);
        }
    }
}
