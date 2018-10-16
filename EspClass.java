import java.util.ArrayList;

public class EspClass {
    private String className;
    private String classId;
    private Float average;
    private ArrayList<EspAssignment> assignments;
    private ArrayList<EspAssignmentCategory> assignmentCategories;

    public EspClass(String className, String classId, Float average, ArrayList<EspAssignment> assignments, ArrayList<EspAssignmentCategory> assignmentCategories) {
        this.className = className;
        this.classId = classId;
        this.average = average;
        this.assignments = assignments;
        this.assignmentCategories = assignmentCategories;
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

    public ArrayList<EspAssignmentCategory> getAssignmentCategories() {
        return this.assignmentCategories;
    }

    public void setAssignmentCategories(ArrayList<EspAssignmentCategory> assignmentCategories) {
        this.assignmentCategories = assignmentCategories;
    }
}
