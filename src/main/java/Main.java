import uk.ac.shef.wit.simmetrics.similaritymetrics.CosineSimilarity;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Levenshtein;
import uk.ac.shef.wit.simmetrics.similaritymetrics.MongeElkan;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main {

    private static List<String> schema = new ArrayList<String>();
    private static List<String> target = new ArrayList<String>();

    public static void main(String[] args) {

        String patternPath = "C:\\Users\\Ariane\\git\\Gorilla\\src\\main\\resources\\imdb.csv";
        String patternPath2 = "C:\\Users\\Ariane\\git\\Gorilla\\src\\main\\resources\\rotten_tomatoes.csv";

        readData(patternPath);
        readData(patternPath2);
        cleanSchema();





	// write your code here
    }

    private static void cleanSchema() {
        ArrayList<String> discarded = new ArrayList<>();
        ArrayList<String> discarded2 = new ArrayList<>();

        for (int i = 0; i < schema.size(); i++){
            String one = schema.get(i);
            for (int j = 0; j < target.size(); j++){
                String two = target.get(j);
                //System.out.println("out cleaning : " + one + ", " + two );
                //if(!one.equals(two)){
                    //System.out.println("out cleaning : " + one + ", " + two );
                    MongeElkan metric = new MongeElkan();
                    Levenshtein metric2 = new Levenshtein();
                    float result = metric.getSimilarity(one, two);
                    float result2 = metric2.getSimilarity(one, two);
                    //System.out.println("Monge-Elkan Similarity: " + result + ", Levenshtein Similarity: " + result2);
                    //if(one.contains(two) || two.contains(one)){
                    //    System.out.println("im cleaning : " + one + ", " + two );
                    //    if(one.length() > two.length()){
                    //        discarded.add(one);
                            //schema.remove(one);
                    //    }
                    //    else{
                    //        discarded.add(two);
                            //schema.remove(two);
                    //    }
                    //}

                        if (result > 0.8 && result2 > 0.4) {
                            discarded.add(two);
                            discarded2.add(one); //schema.remove(two);
                        }

               // }


            }
        }
        System.out.println("matches between sources and target :" + discarded);
        target.removeAll(discarded);
        System.out.println("distinct attributes of target :" +target);
        ArrayList<String> schemaDistinct = new ArrayList<String>();
        schemaDistinct.addAll(schema);
        schemaDistinct.removeAll(discarded2);
        System.out.println("distinct attributes of source :" + schemaDistinct);
        schema.addAll(target);
        System.out.println(schema);

    }

    private static void readData(String filepath){

        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            boolean header = true;
            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                String[] split = sCurrentLine.split(",");
                if(header){
                    createSchema(split);
                    header = false;
                }
                //System.out.println(split[0]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void createSchema(String[] split) {
        boolean targetFile = schema.isEmpty();
        for(int i = 0; i < split.length; i++){
            // first cleaning:
            // lower case and remove empty fields
            String attribute = split[i].trim().toLowerCase().replace('"', ' ').replace(" ", "");
            if(!targetFile) target.add(attribute);
            else schema.add(attribute);
        }
        System.out.println(schema);
    }
}
