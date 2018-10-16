import java.io.Serializable;

public class EspAssignmentCategory implements Serializable {
    private String category;
    private Float points;
    private Float maximumPoints;
    private Float percent;
    private Float categoryWeight;
    private Float categoryPoints;

    public EspAssignmentCategory(String category, Float points, Float maximumPoints, Float percent, Float categoryWeight, Float categoryPoints) {
        this.category = category;
        this.points = points;
        this.maximumPoints = maximumPoints;
        this.percent = percent;
        this.categoryWeight = categoryWeight;
        this.categoryPoints = categoryPoints;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Float getPoints() {
        return this.points;
    }

    public void setPoints(Float points) {
        this.points = points;
    }

    public Float getMaximumPoints() {
        return this.maximumPoints;
    }

    public void setMaximumPoints(Float maximumPoints) {
        this.maximumPoints = maximumPoints;
    }

    public Float getPercent() {
        return this.percent;
    }

    public void setPercent(Float percent) {
        this.percent = percent;
    }

    public Float getCategoryWeight() {
        return this.categoryWeight;
    }

    public void setCategoryWeight(Float categoryWeight) {
        this.categoryWeight = categoryWeight;
    }

    public Float getCategoryPoints() {
        return this.categoryPoints;
    }

    public void setCategoryPoints(Float categoryPoints) {
        this.categoryPoints = categoryPoints;
    }
}
