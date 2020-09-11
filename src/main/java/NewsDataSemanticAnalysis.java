
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsDataSemanticAnalysis {

    private static List<HashMap<String,Integer>> allArticlesList=new ArrayList<>();
    private static HashMap<String,Integer> wordOccurenceInArticleCount=new HashMap<>();
    private static  Map.Entry<String, Float> maxEntry = null;
    private static HashMap<String,Float> relativeFrequency=new HashMap<>();


    public static void main(String[] args) throws IOException {
        List<String> newsList=TwitterAndNewsDataRetrievalFromMongo.getNewsData();
        dataMapper(newsList);
        articleCountHavingRequiredWords();
        createAllWordsFrequencyCSV();
        countCanada();
        printMaxRelativeFreq();
    }

    private  static void printMaxRelativeFreq(){
        for(Map.Entry mapElement : relativeFrequency.entrySet()){
            if(mapElement.getValue()==maxEntry.getValue()){
                System.out.println(mapElement.getKey() +" Has max relative frequency of "+mapElement.getValue());
            }
        }
    }
    public static void createAllWordsFrequencyCSV() throws IOException {
        FileWriter csvWriter = new FileWriter("C:/Users/suraj/OneDrive/Documents/Assignments Dalhousie/Data Managemnet/Assignment_4/"+
                "TF_IDF.csv");
        List<List<String>> csvFileList = new ArrayList<>();
        List<String> headers=new ArrayList<>();
        headers.add("Search Query");
        headers.add("Document containing term (df)");
        headers.add("Total Documents(N)/ number of documents term appeared (df)");
        headers.add("Log (N/df)");
        csvFileList.add(headers);
        for (Map.Entry mapElement : wordOccurenceInArticleCount.entrySet()) {
            List<String> rowData=new ArrayList<>();
            String key = (String)mapElement.getKey();
            int value = (int) mapElement.getValue();
            rowData.add(key);
            rowData.add(Integer.toString(value));
            rowData.add(allArticlesList.size() +" / "+value);
            rowData.add(Double.toString(Math.log(allArticlesList.size()/value)));
            csvFileList.add(rowData);
        }
        for (List<String> rowData : csvFileList) {
            csvWriter.append(String.join(",", rowData));
            csvWriter.append("\n");
        }
        csvWriter.flush();
        csvWriter.close();

    }
    public static  void countCanada() throws IOException {
        FileWriter csvWriter = new FileWriter("C:/Users/suraj/OneDrive/Documents/Assignments Dalhousie/Data Managemnet/Assignment_4/"+
                "Canada_Frequency_Per_Article.csv");
        List<List<String>> csvFileList = new ArrayList<>();
        List<String> headers=new ArrayList<>();
        headers.add("Article Number");
        headers.add("Total Words");
        headers.add("Frequency");
        csvFileList.add(headers);

        for(int i=0;i<allArticlesList.size();i++){
            if(allArticlesList.get(i).containsKey("canada")){
                List<String> rowData=new ArrayList<>();
                int frequency=allArticlesList.get(i).get("canada");
                rowData.add("Article "+i);
                rowData.add(Integer.toString(allArticlesList.get(i).keySet().size()));
                rowData.add(Integer.toString(frequency));
                csvFileList.add(rowData);
                float relativeFreq=((float)frequency/allArticlesList.get(i).keySet().size());
              //  relativeFreq = Math.round(relativeFreq*100)/100;
                relativeFrequency.put("Article "+i, relativeFreq);
            }
        }

        for (Map.Entry mapElement : relativeFrequency.entrySet()) {
            if (maxEntry == null || (float)mapElement.getValue()>maxEntry.getValue())
            {
                maxEntry = mapElement;

            }

        }
        for (List<String> rowData : csvFileList) {
            csvWriter.append(String.join(",", rowData));
            csvWriter.append("\n");
        }
        csvWriter.flush();
        csvWriter.close();
    }

    public static void articleCountHavingRequiredWords(){
        List<String> requiredWords=new ArrayList<>();
        requiredWords.add("canada"); requiredWords.add("university");
        requiredWords.add("halifax");requiredWords.add("business");
        for(String word:requiredWords){
            int occurenceCount=0;
            for(HashMap<String,Integer> wordsMap:allArticlesList){
                if(wordsMap.containsKey(word))
                {
                    occurenceCount++;
                }
            }
            wordOccurenceInArticleCount.put(word,occurenceCount);
        }

    }
    public static void dataMapper(List<String> newsList){
        for(String article:newsList){
            HashMap<String,Integer> wordsInArticle=new HashMap<>();
            String[] words=article.split(" ");
            for(String word:words){
                if(wordsInArticle.containsKey(word)){
                    int count = wordsInArticle.get(word); // get word count
                    wordsInArticle.put(word, count + 1);
                }else
                {
                    wordsInArticle.put(word, 1);
                }
            }
            allArticlesList.add(wordsInArticle);

        }
    }

}
