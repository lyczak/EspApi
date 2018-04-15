import java.io.IOException;
import java.lang.InterruptedException;

public class EspCli {
    private EspConnection conn;

    private static final int MAX_GRADE_LENGTH = 7;

    private EspCli(String username, String password) {
        conn = EspConnection.getInstance();
        try {
            System.out.print("Requesting AuthToken...");
            conn.logOn(username, password);
            System.out.print(" Success\nFetching Classes...");
            conn.fetchClasses();
            System.out.println(" Success");
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

    public void printClassGrades() {
        conn.processClassGrades((className, grade) -> {
            String gradeString = (grade == null) ? "N/G" :
                    String.format("%.2f%%", grade);
            System.out.printf("%s%s %s\n", gradeString,
                    getGradeSpacing(gradeString.length()), className);
        });
    }

    public static void main(String[] args) {
        if(args.length < 3) EspCli.printHelp();
        String username = args[0];
        String password = args[1];
        String command = args[2];
        EspCli cli = new EspCli(username, password);
        switch(command) {
            case "classes":
                cli.printClassGrades();
                break;
            default:
                EspCli.printHelp();
        }
    }
}
