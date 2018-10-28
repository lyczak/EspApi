import java.io.Serializable;
import java.util.ArrayList;

public class EspTranscriptGroup implements Serializable {
    private String year;
    private int grade;
    private String building;
    private ArrayList<EspTranscriptCourse> courses;
    private Float gpa;
    private Float totalCredit;

    public EspTranscriptGroup(String yr, int gd, String bld, ArrayList<EspTranscriptCourse> crs, Float gpa, Float tCd) {
        year = yr;
        grade = gd;
        building = bld;
        courses = crs;
        this.gpa = gpa;
        totalCredit = tCd;
    }

    public String getYear() {
        return this.year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getGrade() {
        return this.grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getBuilding() {
        return this.building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public ArrayList<EspTranscriptCourse> getCourses() {
        return this.courses;
    }

    public void setCourses(ArrayList<EspTranscriptCourse> courses) {
        this.courses = courses;
    }

    public Float getGpa() {
        return this.gpa;
    }

    public void setGpa(Float gpa) {
        this.gpa = gpa;
    }

    public Float getTotalCredit() {
        return this.totalCredit;
    }

    public void setTotalCredit(Float totalCredit) {
        this.totalCredit = totalCredit;
    }
}
