import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class EspAssignment {
    private String dateDue;
    private String dateAssigned;
    private String assignment;
    private String category;
    private Float score;
    private Float totalPoints;
    private Float weight;
    private Float weightedScore;
    private Float weightedTotalPoints;
    private Float averageScore;
    private Float percentage;

    public EspAssignment(Date dateDue, Date dateAssigned, String assignment, String category, Float[] stats, Float percentage) {
            final DateFormat format = new SimpleDateFormat("EEE, MMM d");
            this.dateDue = (dateDue == null) ? "" : format.format(dateDue);
            this.dateAssigned = (dateAssigned == null) ? "" : format.format(dateAssigned);
            this.assignment = assignment;
            this.category = category;
            this.score = stats[0];
            this.totalPoints = stats[1];
            this.weight = stats[2];
            this.weightedScore = stats[3];
            this.weightedTotalPoints = stats[4];
            //this.averageScore = stats[5];
            this.percentage = (this.score == null) ? null : percentage;
    }

    public String getDateDue() {
        return this.dateDue;
    }

    public void setDateDue(String dateDue) {
        this.dateDue = dateDue;
    }

    public String getDateAssigned() {
        return this.dateAssigned;
    }

    public void setDateAssigned(String dateAssigned) {
        this.dateAssigned = dateAssigned;
    }

    public String getAssignment() {
        return this.assignment;
    }

    public void setAssignment(String assignment) {
        this.assignment = assignment;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Float getScore() {
        return this.score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public Float getTotalPoints() {
        return this.totalPoints;
    }

    public void setTotalPoints(Float totalPoints) {
        this.totalPoints = totalPoints;
    }

    public Float getWeight() {
        return this.weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Float getWeightedScore() {
        return this.weightedScore;
    }

    public void setWeightedScore(Float weightedScore) {
        this.weightedScore = weightedScore;
    }

    public Float getWeightedTotalPoints() {
        return this.weightedTotalPoints;
    }

    public void setWeightedTotalPoints(Float weightedTotalPoints) {
        this.weightedTotalPoints = weightedTotalPoints;
    }

    public Float getAverageScore() {
        return this.averageScore;
    }

    public void setAverageScore(Float averageScore) {
        this.averageScore = averageScore;
    }

    public Float getPercentage() {
        return this.percentage;
    }

    public void setPercentage(Float percentage) {
        this.percentage = percentage;
    }
}
