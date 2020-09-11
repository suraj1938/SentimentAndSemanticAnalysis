import java.util.Date;

public class Tweet {
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public int getReTweetCount() {
        return reTweetCount;
    }

    public void setReTweetCount(int reTweetCount) {
        this.reTweetCount = reTweetCount;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    private Date creationTime;
    private int reTweetCount;
    private String place;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
