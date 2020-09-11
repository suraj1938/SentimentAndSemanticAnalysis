import com.mongodb.client.*;
import org.bson.Document;
import org.json.JSONObject;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MongoDb {

    public MongoDb() throws UnknownHostException {
    }

    public static  MongoDatabase getConnection() throws UnknownHostException {
        String url="mongodb+srv://suraj:pass@cluster0-dg60i.mongodb.net/test" ;
        MongoClient mongoClient = MongoClients.create(url);
        MongoDatabase database = mongoClient.getDatabase("DataStore");
        return database;

    }



    public static void addNewsData(HashMap<String, List<News>> newsMap) throws UnknownHostException {
        MongoDatabase database=getConnection();
        MongoCollection<Document> table = database.getCollection("NewsCollection");
        List<Document> documentList=new ArrayList<>();
        for(String key:newsMap.keySet()){
            List<News> newsList=newsMap.get(key);
            for(News news:newsList){
                Document doc=new Document();
                doc.put(key +" News",Arrays.asList(news.getTitle(),news.getDescription(),news.getContent()));
                documentList.add(doc);
            }
        }
        table.insertMany(documentList);
    }


    public static  List<JSONObject> getNewsDataFromMongo() throws UnknownHostException {
        MongoDatabase database=getConnection();
        MongoCollection<Document> dataCollection = database.getCollection("NewsCollection");
        FindIterable<Document> findIterable = dataCollection.find(new Document());
        MongoCursor<Document> cursor = findIterable.iterator();
        List<JSONObject> dataJSONList=new ArrayList<>();
        try {
            while(cursor.hasNext()) {
                dataJSONList.add(new JSONObject(cursor.next().toJson()));
            }
            System.out.println(dataJSONList.size() +" Size");
        } finally {
            cursor.close();
        }
        return dataJSONList;
    }

    public static List<JSONObject> getTwitterAndNewsDataFromMongo() throws UnknownHostException {
        MongoDatabase database=getConnection();
        MongoCollection<Document> dataCollection = database.getCollection("TwitterAndNewsData");
        FindIterable<Document> findIterable = dataCollection.find(new Document());
        MongoCursor<Document> cursor = findIterable.iterator();
        List<JSONObject> dataJSONList=new ArrayList<>();
        try {
            while(cursor.hasNext()) {
                dataJSONList.add(new JSONObject(cursor.next().toJson()));
            }
            System.out.println(dataJSONList.size() +" Size");
        } finally {
            cursor.close();
        }
        return dataJSONList;

    }

}

