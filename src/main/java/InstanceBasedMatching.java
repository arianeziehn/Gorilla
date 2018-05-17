import uk.ac.shef.wit.simmetrics.similaritymetrics.Levenshtein;
import uk.ac.shef.wit.simmetrics.similaritymetrics.MongeElkan;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class InstanceBasedMatching {

    private static List<String> schemaSource = new ArrayList<String>();
    private static List<String> schemaTarget = new ArrayList<>();
    private static HashMap imdb = new HashMap<String, HashSet<Integer>>();
    private static HashMap rottenTomatoes = new HashMap<String, HashSet<Integer>>();
    private static HashMap imdb1 = new HashMap<String, HashSet<Integer>>();
    private static HashMap rottenTomatoes1 = new HashMap<String, HashSet<Integer>>();
    static int maxColumnSource;
    static int maxColumnTarget;

    public static void main(String[] args) {

        long timeStart = System.currentTimeMillis();
        String sourcePath = "C:\\Users\\Ariane\\git\\Gorilla\\src\\main\\resources\\randomImdbres.csv";
        String targetPath = "C:\\Users\\Ariane\\git\\Gorilla\\src\\main\\resources\\randomRTres.csv";
        String sourcePath1 = "C:\\Users\\Ariane\\git\\Gorilla\\src\\main\\resources\\imdbtest3.csv";
        String targetPath1 = "C:\\Users\\Ariane\\git\\Gorilla\\src\\main\\resources\\rotten_tomatoestest3.csv";
        imdb1 = readDataBig(sourcePath1,true);
        rottenTomatoes1 = readDataBig(targetPath1, false);
        imdb = readData(sourcePath,false);
        rottenTomatoes = readData(targetPath, false);

        System.out.println(imdb);

        HashMap match = new HashMap<Integer, HashMap<Integer,Integer>>( );
        HashMap finalMatch = new HashMap<Integer, HashMap<Integer,Integer>>( );
        List<Integer> assignColRT = new ArrayList<>();
        List<Integer> assignColImdb = new ArrayList<>();

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
                if(counter >= 400){
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

        for (int columncounterImdb = 0; columncounterImdb <= maxColumnSource; columncounterImdb++) {

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

        Iterator keySetImdb = imdb.keySet( ).iterator( );
        // here we check for each word of the dictionary
        while (keySetImdb.hasNext( )) {

        for (int columncounterImdb = 0; columncounterImdb <= maxColumnSource; columncounterImdb++) {


                float resultME = 0f;
                float resultL = 0f;
                int count = 0;
                double simFinalME = 0.0;
                double simFinalL = 0.0;
                int colCounterME = 0;
                int colCounterL = 0;


                    //current MapEntry
                    String pairImdb = keySetImdb.next( ).toString( );

                    if (imdb.get(pairImdb).toString( ).contains(columncounterImdb + "") && !assignColImdb.contains(columncounterImdb)) {
                        //System.out.println("GOT IT"+imdb.get(pairImdb).toString()+ ","+(columncounterImdb+""));

                        for (int columncounterRt = 1; columncounterRt <= maxColumnTarget; columncounterRt++) {
                            if (!assignColRT.contains(columncounterRt)) {

                                Iterator keySetRt = rottenTomatoes.keySet( ).iterator( );
                                while (keySetRt.hasNext( )) {

                                    String pairRt = keySetRt.next( ).toString( );

                                    if (rottenTomatoes.get(pairRt).toString( ).contains(columncounterRt + "")) {
                                     //   System.out.println("imb " + pairImdb);
                                     //   System.out.println("pairRT " + pairRt);

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

                            }
                            long currentTime2 = System.currentTimeMillis();
                            System.out.println("----FINISHED colRT" + columncounterRt + " for colImdb " +columncounterImdb +" in " + (currentTime2-timeStart));
                        }

                        HashMap<Integer, Integer> result = new HashMap<Integer, Integer>( );
                        System.out.println("------------" + columncounterImdb + "--------------");
                        System.out.println("simME " + simFinalME + ", " + colCounterME);
                        System.out.println("simL " + simFinalL + ", " + colCounterL);
                        long currentTime = System.currentTimeMillis();
                        if (simFinalME > 0.25 && simFinalL > 0.15) {
                            if(simFinalME > simFinalL) {
                                result.put(colCounterME, 0);
                                assignColRT.add(colCounterME);
                                finalMatch.put(columncounterImdb, result);
                            }
                            else{
                                result.put(colCounterL, 0);
                                assignColRT.add(colCounterL);
                                finalMatch.put(columncounterImdb, result);
                            }

                        }

                    }// for coloumnRT





            long currentTime1 = System.currentTimeMillis();
            System.out.println("----FINISHED col " + columncounterImdb + "in " + (currentTime1-timeStart));
        }// if columm is not already assigned
        }
        long timeEnd = System.currentTimeMillis();
        System.out.println(finalMatch);
        System.out.println(timeEnd-timeStart);
        /*

        current output for small sets from Henrik && it takes very long for colimdb 6, the rest is relativly fast
        besseres umgehen mit den sim werten

        wanted result with test2 datasets: (1 id) 2->2 (name), 3->3 (year), 4->4 (releaseDate),5->5 (Director),6->6(Creator)
        7->8(cast), 8->11(duration), 9->12(ratingvalue), 13->17 (description)
        !!! Achtung: Hier entfällt immer col 0 in 4 for schleifen, weil da noch eine Zeilennummer ist

        wanted result with test2 datasets: (0 id) 1->1 (name), 2->2 (year), 3->3 (releaseDate),4->4 (Director),5->5(Creator)
        6->7(cast), 7->10(duration), 8->11(ratingvalue), 12->16 (description)
        !!! Achtung: Hier entfällt immer col 0 in 4 for schleifen, weil da noch eine Zeilennummer ist
         */

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
                            /*if(attribute.contains(",")){
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
                            else{*/
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
        System.out.println(maxColumnSource);
        System.out.println(maxColumnTarget);
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
