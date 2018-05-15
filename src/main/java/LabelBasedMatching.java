import uk.ac.shef.wit.simmetrics.similaritymetrics.Levenshtein;
import uk.ac.shef.wit.simmetrics.similaritymetrics.MongeElkan;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LabelBasedMatching {

    private static List<String> schema = new ArrayList<String>();
    private static List<String> target = new ArrayList<String>();

    public static void main(String[] args) {

        long timeStart = System.currentTimeMillis();
        // path of source and target file
        String sourcePath = "C:\\Users\\Ariane\\git\\Gorilla\\src\\main\\resources\\imdb.csv";
        String targetPath = "C:\\Users\\Ariane\\git\\Gorilla\\src\\main\\resources\\rotten_tomatoes.csv";

        // create both global schemas
        readSchema(sourcePath);
        readSchema(targetPath);
        // merge of both lists
        List<String> matches = cleanSchema();

        System.out.println("Identified Matches between Source and Target :" + matches);
        System.out.println("Number of Identified Matches between Source and Target : " + matches.size()/2);
        // clean the matches from the target header
        target.removeAll(matches);
        System.out.println("Distinct Attributes of Target :" + target);
        ArrayList<String> schemaDistinct = new ArrayList<String>();
        schemaDistinct.addAll(schema);
        schemaDistinct.removeAll(matches);
        System.out.println("Distinct Attributes of Source :" + schemaDistinct);
        // add the leftover distinct targets to the base schema
        schema.addAll(target);
        System.out.println("FINAL SCHEMA : " + schema);
        long timeEnd = System.currentTimeMillis();
        System.out.println(timeEnd-timeStart + " ms for schema matching");
        }

    private static List<String> cleanSchema() {
        ArrayList<String> discarded = new ArrayList<>();

        for (int i = 0; i < schema.size(); i++){
            String one = schema.get(i);
            for (int j = 0; j < target.size(); j++){
                String two = target.get(j);

                    if(one.equals(two)){
                        discarded.add(two);
                        discarded.add(one);
                    }
                    else {
                        MongeElkan metricME = new MongeElkan( );
                        Levenshtein metricL = new Levenshtein( );
                        float resultME = metricME.getSimilarity(one, two);
                        float resultL = metricL.getSimilarity(one, two);

                        if (resultME > 0.8 && resultL > 0.4) {
                            discarded.add(two);
                            discarded.add(one);
                        }
                    }
            }
        }
        return discarded;

    }

    private static void readSchema(String filepath){

        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            boolean header = true;
            String sCurrentLine;
            int counter = 0;
            while ((sCurrentLine = br.readLine()) != null && counter < 1) {
                String[] split = sCurrentLine.split(",");
                if(header){
                    createSchema(split);
                    header = false;
                }
                counter++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void createSchema(String[] split) {

        boolean targetFile = schema.isEmpty();
        for(int i = 0; i < split.length; i++){
            String attribute = split[i].trim().toLowerCase().replace('"', ' ').replace(" ", "");
            if(!targetFile) target.add(attribute);
            else schema.add(attribute);
        }
    }
}
