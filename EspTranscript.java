import java.io.Serializable;
import java.util.ArrayList;

public class EspTranscript implements Serializable {
    private Float gpa;
    private Integer rank;
    private Integer rankOutOf;
    private ArrayList<EspTranscriptGroup> groups;

    public EspTranscript(Float gpa, Integer rnk, Integer roo, ArrayList<EspTranscriptGroup> gps) {
        this.gpa = gpa;
        rank = rnk;
        rankOutOf = roo;
        groups = gps;
    }

    public Float getGpa() {
        return this.gpa;
    }

    public void setGpa(Float gpa) {
        this.gpa = gpa;
    }

    public Integer getRank() {
        return this.rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getRankOutOf() {
        return this.rankOutOf;
    }

    public void setRankOutOf(Integer rankOutOf) {
        this.rankOutOf = rankOutOf;
    }

    public ArrayList<EspTranscriptGroup> getGroups() {
        return this.groups;
    }

    public void setGroups(ArrayList<EspTranscriptGroup> groups) {
        this.groups = groups;
    }
}
