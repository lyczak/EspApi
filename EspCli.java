import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.lang.InterruptedException;

public class EspCli {
    private EspConnection conn;

    private static final int MAX_GRADE_LENGTH = 7;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yy");

    private EspCli(String username, String password) {
        conn = EspConnection.getInstance();
        try {
            System.out.print("Requesting AuthToken... ");
            conn.logOn(username, password);
            System.out.print("Success\nFetching Classes... ");
            conn.fetchClasses();
            System.out.print("Success\nParsing Classes... ");
            conn.parseClasses();
            System.out.println("Success");
        } catch(IOException | InterruptedException e) {
            System.out.println();
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static void printHelp() {
        throw new IllegalArgumentException(
                "Usage: java EspCli <username> <password> (classes)");
    }

    private static String getGradeSpacing(int gradeLength) {
        String spacing = "";
        gradeLength = MAX_GRADE_LENGTH - gradeLength;
        for(; gradeLength > 0; gradeLength--) {
            spacing += " ";
        }
        return spacing;
    }

    private static String getGradeValue(Float grade) {
        return (grade == null) ? "N/A" : String.format("%.2f", grade);
    }

    private static String getBars(int amount) {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < amount; i++) {
            builder.append("─");
        }
        return builder.toString();
    }

    private void printAssignments(EspClass espClass) {
        int longestAssignment = conn.getLongestAssignment();
        String format = "│ %-8s │ %-" + longestAssignment + "s │ %-6s │ %-6s │ %-6s%% │%n";
        System.out.println("┌──────────┬─" + getBars(longestAssignment) + "─┬────────┬────────┬─────────┐");
        System.out.printf(format, "Due Date", "Assignment", "Score", "Total", "");
        System.out.println("├──────────┼─" + getBars(longestAssignment) + "─┼────────┼────────┼─────────┤");
        for(EspAssignment assignment : espClass.getAssignments()) {
            String dueDate = DATE_FORMAT.format(assignment.getDateDue());
            String score = getGradeValue(assignment.getScore());
            String totalPoints = getGradeValue(assignment.getTotalPoints());
            String percentage = getGradeValue(assignment.getPercentage());
            System.out.printf(format, dueDate, assignment.getAssigment(), score, totalPoints, percentage);;
        }
        System.out.println("└──────────┴─" + getBars(longestAssignment) + "─┴────────┴────────┴─────────┘");
    }

    private void printClassGrades(boolean printAssignments) {
        for(EspClass espClass : conn.getClasses()) {
            String gradeString = (espClass.getAverage() == null) ? "N/G" :
                    String.format("%.2f%%", espClass.getAverage());
            System.out.printf("%s%s %s\n", gradeString,
                    getGradeSpacing(gradeString.length()), espClass.getClassName());
            if(printAssignments) printAssignments(espClass);
        }
    }

    public static void main(String[] args) {
        if(args.length < 3) EspCli.printHelp();
        String username = args[0];
        String password = args[1];
        String command = args[2];
        EspCli cli = new EspCli(username, password);
        switch(command) {
            case "classes":
                cli.printClassGrades(false);
                break;
            case "assignments":
                cli.printClassGrades(true);
                break;
            default:
                EspCli.printHelp();
        }
    }
}
