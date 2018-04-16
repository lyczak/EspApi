import java.util.ArrayList;

public class EspClass {
    private String className;
    private String classId;
    private Float average;
    private ArrayList<EspAssignment> assignments;

    public EspClass(String className, String classId, Float average, ArrayList<EspAssignment> assignments) {
        this.className = className;
        this.classId = classId;
        this.average = average;
        this.assignments = assignments;
    }

    public String getClassName() {
        return this.className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassId() {
        return this.classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public Float getAverage() {
        return this.average;
    }

    public void setAverage(Float average) {
        this.average = average;
    }

    public ArrayList<EspAssignment> getAssignments() {
        return this.assignments;
    }

    public void setAssignments(ArrayList<EspAssignment> assignments) {
        this.assignments = assignments;
    }
}
