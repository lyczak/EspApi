import java.io.Serializable;

public class EspTranscriptCourse implements Serializable {
    private String id;
    private String description;
    private Integer percentage;
    private String letterGrade;
    private Float credit;

    public EspTranscriptCourse(String id, String desc, Integer pct, String lg, Float cdt) {
        this.id = id;
        this.description = desc;
        this.percentage = pct;
        this.letterGrade = lg;
        this.credit = cdt;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPercentage() {
        return this.percentage;
    }

    public void setPercentage(Integer percentage) {
        this.percentage = percentage;
    }

    public String getLetterGrade() {
        return this.letterGrade;
    }

    public void setLetterGrade(String letterGrade) {
        this.letterGrade = letterGrade;
    }

    public Float getCredit() {
        return this.credit;
    }

    public void setCredit(Float credit) {
        this.credit = credit;
    }
}
