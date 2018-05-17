import uk.ac.shef.wit.simmetrics.similaritymetrics.Levenshtein;
import uk.ac.shef.wit.simmetrics.similaritymetrics.MongeElkan;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/*
* Instance-based matching
*/

public class InstanceBasedMatching2 {
    // holding the schemas
    private static List<String> schemaSource = new ArrayList<String>();
    private static List<String> schemaTarget = new ArrayList<>();
    // Hash Map for 1) both complete sets, 2) for a reduced set
    private static HashMap imdb = new HashMap<String, HashSet<Integer>>();
    private static HashMap rottenTomatoes = new HashMap<String, HashSet<Integer>>();
    private static HashMap imdb1 = new HashMap<String, HashSet<Integer>>();
    private static HashMap rottenTomatoes1 = new HashMap<String, HashSet<Integer>>();
    static int maxColumnSource;
    static int maxColumnTarget;

    public static void main(String[] args) {

        long timeStart = System.currentTimeMillis();
        //small sets
        String sourcePath = "C:\\Users\\Ariane\\git\\Gorilla\\src\\main\\resources\\randomImdbres.csv";
        String targetPath = "C:\\Users\\Ariane\\git\\Gorilla\\src\\main\\resources\\randomRTres.csv";
        imdb = readData(sourcePath,true);
        rottenTomatoes = readDataBig(targetPath, false);
        // System.out.println(imdb.size()); 21458 entries
        // System.out.println(rottenTomatoes.size()); 8067 entries

        // big sets
        String sourcePath1 = "C:\\Users\\Ariane\\git\\Gorilla\\src\\main\\resources\\imdbtest3.csv";
        String targetPath1 = "C:\\Users\\Ariane\\git\\Gorilla\\src\\main\\resources\\rotten_tomatoestest3.csv";
        imdb1 = readDataBig(sourcePath1,true);
        rottenTomatoes1 = readData(targetPath1, false);

        HashMap match = new HashMap<Integer, HashMap<Integer,Integer>>( );
        HashMap finalMatch = new HashMap<Integer, HashMap<Integer,Integer>>( );
        List<Integer> assignColRT = new ArrayList<>();
        List<Integer> assignColImdb = new ArrayList<>();

        //------- Phase 1 ---- exact matches of big sets
        Iterator keySetImbd = imdb1.keySet().iterator( );
        while (keySetImbd.hasNext()) {
            String imdbKey = keySetImbd.next( ).toString( );
            if (rottenTomatoes1.containsKey(imdbKey)) {
                for(Integer col : ((HashSet<Integer>)imdb1.get(imdbKey))){
                    HashMap<Integer, Integer> aMatch = (HashMap<Integer, Integer>) match.getOrDefault(col, new HashMap<>());
                    for(Integer dummy : (HashSet<Integer>) rottenTomatoes1.get(imdbKey) ){
                        int counter = aMatch.getOrDefault(dummy, 0);
                        counter++;
                        aMatch.put(dummy, counter);
                        match.put(col, aMatch);
                        if(counter > 400){
                            HashMap<Integer, Integer> result = new HashMap<Integer,Integer>();
                            result.put(dummy, counter);
                            finalMatch.put(col, result);
                            if(!assignColImdb.contains(col))assignColImdb.add(col);
                            if(!assignColRT.contains(dummy))assignColRT.add(dummy);
                        }
                    }
                }
            }
        }
        // extract knowledge base
        for (int columncounterImdb = 0; columncounterImdb < maxColumnSource; columncounterImdb++) {
            if (match.containsKey(columncounterImdb)) {
                HashMap<Integer, Integer> aMatch = (HashMap<Integer, Integer>) match.get(columncounterImdb);
                if (aMatch.size( ) == 1) {
                    finalMatch.put(columncounterImdb, aMatch);
                    if(!assignColImdb.contains(columncounterImdb)) assignColImdb.add(columncounterImdb);
                    assignColRT.addAll(aMatch.keySet());
                }
            }
        }

        System.out.println(" Previously known matches: " + match);
        System.out.println(" Previously known Finalmatches: " + finalMatch);
        System.out.println(" Already assign colImdb: " + assignColImdb);
        System.out.println(" Already assign colRT: " + assignColRT);

        // second phase
        for (int columncounterImdb = 0; columncounterImdb < maxColumnSource; columncounterImdb++) {

            if (!assignColImdb.contains(columncounterImdb)) {
                float resultME = 0f;
                float resultL = 0f;
                int count = 0;
                double simFinalME = 0.0;
                double simFinalL = 0.0;
                int colCounterME = 0;
                int colCounterL = 0;
                boolean colFinished = false;

                Iterator keySetImdb = imdb.keySet( ).iterator( );
                // here we check for each word of the dictionary
                while (keySetImdb.hasNext( ) && !colFinished) {
                    //current MapEntry imdb
                    String pairImdb = keySetImdb.next( ).toString( );

                    if (imdb.get(pairImdb).toString( ).contains(columncounterImdb + "")) {

                        for (int columncounterRt = 1; columncounterRt < maxColumnTarget; columncounterRt++) {

                            if (!assignColRT.contains(columncounterRt)) {
                                Iterator keySetRt = rottenTomatoes.keySet( ).iterator( );
                                while (keySetRt.hasNext( )) {
                                    //current MapEntry rt
                                    String pairRt = keySetRt.next().toString();

                                    if (rottenTomatoes.get(pairRt).toString().contains(columncounterRt + "")) {
                                        MongeElkan mE = new MongeElkan( );
                                        Levenshtein metricL = new Levenshtein( );
                                        resultME += mE.getSimilarity(pairImdb, pairRt);
                                        resultL += metricL.getSimilarity(pairImdb, pairRt);
                                        count++;
                                    }
                                }

                                double simME = resultME / (double) count;
                                double simL = resultL / (double) count;

                                if (simME > simFinalME) {
                                    simFinalME = simME;
                                    colCounterME = columncounterRt;
                                }
                                if (simL > simFinalL) {
                                    simFinalL = simL;
                                    colCounterL = columncounterRt;
                                }

                                if(count > 100000){

                                    if(count > 100000 && simFinalL >= 0.11){
                                        HashMap<Integer, Integer> result = new HashMap<Integer, Integer>( );
                                        result.put(colCounterL, 0);
                                        assignColRT.add(colCounterL);
                                        assignColImdb.add(columncounterImdb);
                                        finalMatch.put(columncounterImdb, result);
                                        colFinished = true;
                                    }
                                    if(count > 100000 && simFinalL < 0.11){
                                        // no match for this col
                                        colFinished = true;
                                    }

                                    break;
                                }
                            }
                            //System.out.println("----FINISHED colRT " + columncounterRt + " for col Imdb " +columncounterImdb );
                            //System.out.println("----SimME for Col " + colCounterME + " with " + simFinalME + " SimL for Col " + colCounterL + " with " +simFinalL);
                        }
                    }// for coloumnRT
                }

                HashMap<Integer, Integer> result = new HashMap<Integer, Integer>( );
                System.out.println("------------" + columncounterImdb + "--------------");
                System.out.println("simME " + simFinalME + ", " + colCounterME);
                System.out.println("simL " + simFinalL + ", " + colCounterL);

                if (simFinalME > 0.21 && simFinalL > 0.15) {

                        result.put(colCounterL, 0);
                        assignColRT.add(colCounterL);
                        assignColImdb.add(columncounterImdb);
                        finalMatch.put(columncounterImdb, result);
                        colFinished = true;

                }
                //System.out.println("The final match : " + finalMatch);
            }
            long currentTime = System.currentTimeMillis();
            System.out.println("----FINISHED col Imdb " + columncounterImdb + " time so far : " + ((currentTime-timeStart)/1000) + "s");
        }
        long timeEnd = System.currentTimeMillis();

        System.out.println(finalMatch);
        System.out.println("TOTAL TIME " + (timeEnd-timeStart)/1000 + "s");
       }


    private static HashMap readData(String filepath, boolean source){
        HashMap dataBase = new HashMap();
        boolean header = true;
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {

            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {

                String[] split = sCurrentLine.split(";");
                if(source) maxColumnSource = split.length;
                else maxColumnTarget = split.length;

                if(!header) {
                    for (int counter = 0; counter < split.length; counter++) {
                        String attribute = split[counter].trim().toLowerCase().replace('"', ' ').replaceAll(" ", "").replaceAll("_", "").replaceAll("-","");

                        if(!attribute.equals("") && !attribute.equals(" ")) {
                            if(attribute.contains(",")){
                                String[] split2 = attribute.split(",");
                                for (int counter2 = 0; counter2 < split2.length; counter2++) {
                                    String att = split2[counter2].trim( ).toLowerCase( ).replace('"', ' ').replaceAll(" ", "").replaceAll("_", "").replaceAll("-", "");
                                    if(!att.equals("") && !att.equals(" ")) {
                                        HashSet<Integer> aMatch = (HashSet<Integer>) dataBase.getOrDefault(att, new HashSet<>( ));
                                        aMatch.add(counter);
                                        dataBase.put(att, aMatch);
                                    }
                                }
                            }
                            else{
                                HashSet<Integer> aMatch = (HashSet<Integer>) dataBase.getOrDefault(attribute, new HashSet<>());
                                aMatch.add(counter);
                                dataBase.put(attribute, aMatch);
                            }}
                    }
                }
                else{
                    header = false;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataBase;

    }

    private static HashMap readDataBig(String filepath, boolean source){
        HashMap dataBase = new HashMap();
        boolean header = true;
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {

            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {

                String[] split = sCurrentLine.split(";");

                if(!header) {
                    for (int counter = 0; counter < split.length; counter++) {

                        String attribute = split[counter].trim().toLowerCase().replace('"', ' ').replaceAll(" ", "").replaceAll("_", "").replaceAll("-","");

                        if(!attribute.equals("") && !attribute.equals(" ")) {
                            HashSet<Integer> aMatch = (HashSet<Integer>) dataBase.getOrDefault(attribute, new HashSet<>());
                            aMatch.add(counter);
                            dataBase.put(attribute, aMatch);
                        }
                    }
                }
                else{
                    createSchema(split);
                }
                header = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataBase;

    }


    private static void createSchema(String[] split) {
        boolean source = schemaSource.isEmpty();
        if(source) maxColumnSource = split.length;
        else maxColumnTarget = split.length;
        //System.out.println(maxColumnSource);
        //System.out.println(maxColumnTarget);
        for(int i = 0; i < split.length; i++){
            // first cleaning:
            // lower case and remove empty fields
            String attribute = split[i].trim().toLowerCase().replace('"', ' ').replace(" ", "");
            if(!source) {
                schemaTarget.add(attribute);
            }
            else schemaSource.add(attribute);
        }

    }
}