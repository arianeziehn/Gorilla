package Assign3;

import uk.ac.shef.wit.simmetrics.similaritymetrics.MongeElkan;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;

public class DuplicateFinder {

    private static List<ArrayList<String>> input = new ArrayList<>();
    public static String sourcePath = "./src/main/output/csvOutput3.csv";

    public static void main(String[] args) throws IOException {

        findDuplicates();

        String csvOutput2 = "./src/main/output/csvOutput4.csv";
        FileWriter fileWriter = new FileWriter(csvOutput2);

        fileWriter.write("");
        fileWriter.write("tuple_id_1, tuple_id_2\n");


        for (ArrayList<String> anInput : input) {
            Iterator iterator = anInput.iterator();
            while (iterator.hasNext()) {
                fileWriter.append(iterator.next().toString());
                if(iterator.hasNext()) {
                    fileWriter.append(",");
                }
            }
            fileWriter.append("\n");
        }

        fileWriter.flush();
        fileWriter.close();





    }
    public static void findDuplicates() throws IOException {
        String line;
        HashMap<String, String> tuples = new HashMap<>();

        ArrayList<String> row = new ArrayList<>(2);

        MongeElkan mongeElkan = new MongeElkan();

        BufferedReader bufferedReader = new BufferedReader(new FileReader(sourcePath));
        bufferedReader.readLine();

        while (bufferedReader.ready() && (line = bufferedReader.readLine()) != null){
            tuples.put((line.split(",")[0]),line);
        }
        HashMap<String, String> clone = new HashMap<>(tuples);

        int rowCount = 0;

        tuples.forEach((s, s2) -> {
            if (s.matches(clone.get(0))){
                row.add(rowCount, s);
            }
        });

    }
}
