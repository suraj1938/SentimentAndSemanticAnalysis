import java.util.List;

public class TweetSentimentAnalysis {

    private String tweet;
    private List<String> matchWords;
    private String polarity;

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public List<String> getMatchWords() {
        return matchWords;
    }

    public void setMatchWords(List<String> matchWords) {
        this.matchWords = matchWords;
    }

    public String getPolarity() {
        return polarity;
    }

    public void setPolarity(String polarity) {
        this.polarity = polarity;
    }
}
