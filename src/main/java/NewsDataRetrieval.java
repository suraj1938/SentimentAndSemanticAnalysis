import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewsDataRetrieval {
    public static void main(String[] args) {
        try {
            MongoDb.addNewsData(getNewsForKeyWords(getKeyWords()));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getKeyWords() {
        List<String> keyWords = new ArrayList<>();
        keyWords.add("Canada");
        keyWords.add("University");
        keyWords.add("Dalhousie%20University");
        keyWords.add("Halifax");
        keyWords.add("Canada%20Education");
        keyWords.add("Moncton");
        keyWords.add("Toronto");
        return keyWords;
    }

    public  static HashMap<String, List<News>>  getNewsForKeyWords(List<String> keys){
        HashMap<String, List<News>> newsMap=new HashMap<>();
        try {

            for(String key:keys){
                URL newsApiUrl = new URL("http://newsapi.org/v2/everything?q="+key+"&from=2020-04-01" +
                        "&sortBy=publishedAt&apiKey=5c85715828ec4a45a4b3b5d8c2b6ed6e");
                HttpURLConnection conn = (HttpURLConnection) newsApiUrl.openConnection();

                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("Failed : HTTP Error code : "
                            + conn.getResponseCode());
                }
                InputStreamReader in = new InputStreamReader(conn.getInputStream());
                BufferedReader br = new BufferedReader(in);
                String output;
                while ((output = br.readLine()) != null) {
                    JSONObject jsonObject = new JSONObject(output);
                    newsMap.put(key,handleJson(jsonObject));
                }
                conn.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newsMap;
    }
    public static String cleanText(String text) {
        text = text.replace("\n", "\\n");
        text = text.replace("\t", "\\t");
        text = text.replaceAll("[^\\w\\s]", "");
        text = text.replaceAll("http.*?\\s", "");
        if (text.contains("http")) {
            int indexOfHttp = text.indexOf("http");
            int endPoint = (text.indexOf(' ', indexOfHttp) != -1) ? text.indexOf(' ', indexOfHttp) :
                    text.length();
            String url = text.substring(indexOfHttp, endPoint);
            text = text.replace(url, "");

        }


        return text;
    }

    public static List<News> handleJson(JSONObject jsonObject){
        List<News> newsList = new ArrayList<News>();
        List<JSONObject> articleJsonObj=new ArrayList<JSONObject>();
        JSONArray array = jsonObject.getJSONArray("articles");
        System.out.println(array.length()+"Size");
        for(int i = 0 ; i < array.length() ; i++){
            News news=new News();
            news.setContent(cleanText(array.getJSONObject(i).get("content").toString()));
            news.setDescription((cleanText(array.getJSONObject(i).get("description").toString())));
            news.setTitle((cleanText(array.getJSONObject(i).get("title").toString())));
            newsList.add(news);
        }
        System.out.println(newsList.size());
        return newsList;

    }

}
