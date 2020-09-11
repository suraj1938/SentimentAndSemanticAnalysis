import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class TwitterAndNewsDataRetrievalFromMongo {

    public static List<String> getTwitterData() throws UnknownHostException {
        List<JSONObject> twitterAndNewsData = MongoDb.getTwitterAndNewsDataFromMongo();
        List<String> tweetList = new ArrayList<>();
        for (JSONObject obj : twitterAndNewsData) {
            Set<String> keySet = obj.keySet();
            String key = "";
            for (Iterator<String> it = keySet.iterator(); it.hasNext(); ) {
                String tempKey = it.next();
                if (!tempKey.equals("_id")) {
                    key = tempKey;

                }
            }
            if (key.contains("Tweet")) {
                JSONArray tweetObj = obj.getJSONArray(key);
                String tweet = tweetObj.get(0).toString();
                tweet = tweet.replace("RT", "");
                tweetList.add(tweet);
            }
        }
        return tweetList;

    }

    public static List<String> getNewsData() throws UnknownHostException {
        List<JSONObject> newsData = MongoDb.getNewsDataFromMongo();
        List<String> newsList = new ArrayList<>();
        for (JSONObject obj : newsData) {
            Set<String> keySet = obj.keySet();
            String key = "";
            for (Iterator<String> it = keySet.iterator(); it.hasNext(); ) {
                String tempKey = it.next();
                if (!tempKey.equals("_id")) {
                    key = tempKey;

                }
            }
            if (key.contains("News")) {
                JSONArray newsObj = obj.getJSONArray(key);
                String news = newsObj.get(2).toString().toLowerCase();
                newsList.add(news);

            }
        }
        return newsList;
    }
}

