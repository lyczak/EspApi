import java.io.Serializable;
import java.util.List;
import java.util.Date;

public class EspStudent implements Serializable {
    private String name;
    private Date birthDate;
    private String house;
    private String counselor;
    private String building;
    private String gender;
    private String calendar;
    private String homeroom;
    private int grade;
    private String language;
    private String homeroomTeacher;
    private List<EspTransportation> transportation;
    private List<EspContact> contacts;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthDate() {
        return this.birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getHouse() {
        return this.house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getCounselor() {
        return this.counselor;
    }

    public void setCounselor(String counselor) {
        this.counselor = counselor;
    }

    public String getBuilding() {
        return this.building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCalendar() {
        return this.calendar;
    }

    public void setCalendar(String calendar) {
        this.calendar = calendar;
    }

    public String getHomeroom() {
        return this.homeroom;
    }

    public void setHomeroom(String homeroom) {
        this.homeroom = homeroom;
    }

    public int getGrade() {
        return this.grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getHomeroomTeacher() {
        return this.homeroomTeacher;
    }

    public void setHomeroomTeacher(String homeroomTeacher) {
        this.homeroomTeacher = homeroomTeacher;
    }

    public List<EspTransportation> getTransportation() {
        return this.transportation;
    }

    public void setTransportation(List<EspTransportation> transportation) {
        this.transportation = transportation;
    }

    public List<EspContact> getContacts() {
        return this.contacts;
    }

    public void setContacts(List<EspContact> contacts) {
        this.contacts = contacts;
    }
}
