import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.*;

public class TwitterDataSentimentAnalysis {

    private static List<String> positiveList = new ArrayList<>();
    private static List<String> negativeList = new ArrayList<>();
    private static List<String> neutralList=new ArrayList<>();
    private static List<TweetSentimentAnalysis> analysisList=new ArrayList<>();
    public static void main(String[] args) throws IOException {
        List<String> tweetList=TwitterAndNewsDataRetrievalFromMongo.getTwitterData();
        getLexicalWords();
        createBagOfWordsAndAnalyse(tweetList);
        createCsvFile();
        createCSVForVisualization();
    }



    private static void createBagOfWordsAndAnalyse(List<String> tweetList){
        for(String tweet:tweetList){
            HashMap<String,Integer> bagOfWords=new HashMap<>();
            tweet=tweet.toLowerCase();
            String[] tweetWords=tweet.split(" ");
            for(String word:tweetWords){
                if(bagOfWords.containsKey(word)){
                    int count = bagOfWords.get(word); // get word count
                    bagOfWords.put(word, count + 1);
                }else
                {
                    bagOfWords.put(word, 1);
                }
            }
            analysisList.add(analyseSentiment(bagOfWords,tweet));

        }

    }

    public static TweetSentimentAnalysis analyseSentiment(HashMap<String,Integer> bagOfWords,String tweet){
        int positiveCount=0;int negativeCount=0;int neutralCount=0;
        TweetSentimentAnalysis analysisObj=new TweetSentimentAnalysis();
        analysisObj.setTweet(tweet);
        analysisObj.setMatchWords(new ArrayList<>());
        Set<String> keySet=bagOfWords.keySet();
        for(String word:keySet){
            if(positiveList.contains(word)){
                positiveCount+=bagOfWords.get(word);
                analysisObj.getMatchWords().add(word);
            }
            else if(negativeList.contains(word)){
                negativeCount+=bagOfWords.get(word);
                analysisObj.getMatchWords().add(word);
            }else if(neutralList.contains(word)){
                neutralCount+=bagOfWords.get(word);
                analysisObj.getMatchWords().add(word);
            }
        }
        if(positiveCount>negativeCount){
            analysisObj.setPolarity("Positive");
        }
        else if(positiveCount<negativeCount){
            analysisObj.setPolarity("Negative");
        }
        else if(positiveCount==negativeCount){
            analysisObj.setPolarity("Neutral");
        }

        return analysisObj;

    }

    private static void getLexicalWords(){
        String fileToParse = "C:/Users/suraj/OneDrive/Documents/Assignments Dalhousie/Data Managemnet/Assignment_4/" +
                "Lexical_Words.csv";
        BufferedReader fileReader = null;
        final String DELIMITER = ",";
        try
        {
            String line = "";
            fileReader = new BufferedReader(new FileReader(fileToParse));
            String header=fileReader.readLine();
            while ((line = fileReader.readLine()) != null)
            {
                String[] tokens = line.split(DELIMITER);
                if(Integer.parseInt(tokens[1])==1){
                    positiveList.add(tokens[0]);
                }
                else if(Integer.parseInt(tokens[2])==1){
                    negativeList.add(tokens[0]);
                }
                else{
                    neutralList.add(tokens[0]);
                }

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createCsvFile() throws IOException {
        FileWriter csvWriter = new FileWriter("C:/Users/suraj/OneDrive/Documents/Assignments Dalhousie/Data Managemnet/Assignment_4/"+
                "Sentiment_Analysis.csv");
        List<List<String>> csvFileList = new ArrayList<>();
        int rowCount=1;
        List<String> headers=new ArrayList<>();
        headers.add("Tweet Number");
        headers.add("Text");
        headers.add("Matched Words");
        headers.add("Polarity");
        csvFileList.add(headers);
        for(TweetSentimentAnalysis obj:analysisList){
            List<String> csvData=new ArrayList<>();
            csvData.add(Integer.toString(rowCount));
            csvData.add(obj.getTweet());
            csvData.add(String.join(";",obj.getMatchWords()));
            csvData.add(obj.getPolarity());
            csvFileList.add(csvData);
            rowCount++;
        }
        for (List<String> rowData : csvFileList) {
            csvWriter.append(String.join(",", rowData));
            csvWriter.append("\n");
        }
        csvWriter.flush();
        csvWriter.close();
    }
    private static void createCSVForVisualization() throws IOException {
        FileWriter csvWriter = new FileWriter("C:/Users/suraj/OneDrive/Documents/Assignments Dalhousie/Data Managemnet/Assignment_4/"+
                "Visualization.csv");
        HashMap<String,Integer> map=new HashMap<>();
        for(TweetSentimentAnalysis obj:analysisList){
            for(String word:obj.getMatchWords()){
                List<String> csvData=new ArrayList<>();
                if(map.containsKey(word)){
                    int count = map.get(word); // get word count
                    map.put(word, count + 1);
                }else
                {
                    map.put(word, 1);
                }
            }
        }
        List<List<String>> csvFileList = new ArrayList<>();
        int rowCount=1;
        List<String> headers=new ArrayList<>();
        headers.add("Matched Word");
        headers.add("Word Polarity");
        headers.add("Count");
        csvFileList.add(headers);
        for(Map.Entry mapEle: map.entrySet()){
            List<String> rowData=new ArrayList<>();
            rowData.add(mapEle.getKey().toString());
            if(positiveList.contains(mapEle.getKey().toString())){
                rowData.add("Positive");
            }
            else if(negativeList.contains(mapEle.getKey().toString())){
                rowData.add("Negative");
            }
            else if(neutralList.contains(mapEle.getKey().toString())){
                rowData.add("Neutral");
            }
            rowData.add(mapEle.getValue().toString());
            csvFileList.add(rowData);
        }
        for (List<String> rowData : csvFileList) {
            csvWriter.append(String.join(",", rowData));
            csvWriter.append("\n");
        }
        csvWriter.flush();
        csvWriter.close();

    }
}
