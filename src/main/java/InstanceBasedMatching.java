import uk.ac.shef.wit.simmetrics.similaritymetrics.Levenshtein;
import uk.ac.shef.wit.simmetrics.similaritymetrics.MongeElkan;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class InstanceBasedMatching {

    private static List<String> schema = new ArrayList<String>();
    private static List<String> source = new ArrayList<>();

    private static HashMap imdb = new HashMap();
    private static HashMap rottenTomatoes = new HashMap();

    public static void main(String[] args) {

        String patternPath = "/home/fwille/Gorilla/Gorilla/src/main/resources/imdbtest2.csv";
        String patternPath2 = "/home/fwille/Gorilla/Gorilla/src/main/resources/rotten_tomatoestest2.csv";
        int maxColumnImdb = 14;
        int maxColumnRt = 17;

        imdb = readData(patternPath);
        rottenTomatoes = readData(patternPath2);

        for (int columncounterImdb = 0; columncounterImdb<=maxColumnImdb; columncounterImdb++){
            //System.out.println(imdb.containsValue(columncounter));
            if (imdb.containsValue(columncounterImdb)){
                float resultME = 0f;
                int count =0;
                Iterator keySet = imdb.keySet().iterator();
                while(keySet.hasNext()) {
                    String imdbKey = keySet.next().toString();
                    for (int columncounterRt = 0; columncounterRt<=maxColumnRt; columncounterRt++){
                        if(rottenTomatoes.containsValue(columncounterRt)){
                            Iterator keySetRt = rottenTomatoes.keySet().iterator();
                            while (keySetRt.hasNext()){
                                String rtKey = keySet.next().toString();
                                MongeElkan mE = new MongeElkan();
                                resultME += mE.getSimilarity(imdbKey, rtKey);
                                count++;
                            }
                        }
                    }
                }
                double sim = resultME/count;
                System.out.println(sim +", " +columncounterImdb);
            }
        }


        cleanSchema();





	// write your code here
    }

    private static void cleanSchema() {
        ArrayList<String> discarded = new ArrayList<>();

        for (int i = 0; i < schema.size(); i++){
            String one = schema.get(i);
            for (int j = i+1; j < schema.size(); j++){
                String two = schema.get(j);
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
                            //schema.remove(two);
                        }

                //}


            }
        }

        schema.removeAll(discarded);
        System.out.println(schema);

    }

    private static HashMap readData(String filepath){
        HashMap dataBase = new HashMap();

        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            boolean header = true;
            String sCurrentLine;




            while ((sCurrentLine = br.readLine()) != null) {
                String[] split = sCurrentLine.split(";");
                if(!header) {
                    for (int counter = 0; counter < split.length; counter++) {
                        String attribute = split[counter].trim().toLowerCase().replace('"', ' ').replace(" ", "");
                        //header = false;
                        if(!attribute.equals("")) {
                            dataBase.put(attribute, counter);
                        }
                        //counter++;
                        //System.out.println(split[0]);
                    }
                }
                header = false;
                //System.out.println(split[0]);

            }

            System.out.println("fertig");
/**
            while ((sCurrentLine = br.readLine()) != null) {
                String[] split = sCurrentLine.split(",");
                if(!header){
                    createSchema(split);
                    //header = false;
                    column.put(split[0], counter);
                    counter++;
                    //System.out.println(split[0]);
                }
                header = false;
                //System.out.println(split[0]);

            }
*/
           //System.out.println(column.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataBase;

    }
    private static void createSchema(String[] split) {
        for(int i = 0; i < split.length; i++){
            // first cleaning:
            // lower case and remove empty fields
            String attribute = split[i].trim().toLowerCase().replace('"', ' ').replace(" ", "");
            //if(!schema.contains(attribute))
            schema.add(attribute);
        }
        System.out.println(schema);
    }
}
