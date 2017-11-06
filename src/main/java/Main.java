import codesmell.AbstractSmell;
import codesmell.ResultsWriter;
import codesmell.TestFile;
import codesmell.TestSmellDetector;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        TestSmellDetector testSmellDetector = TestSmellDetector.createTestSmellDetector();

        /*
          Read the the folder list subfolder and build the TestFile objects
         */

        BufferedReader in = new BufferedReader(new FileReader("/Users/khalidsalmalki/Desktop/cidesmell.csv"));
        String str;

        String[] lineItem;
        TestFile testFile;
        List<TestFile> testFiles = new ArrayList<>();
        while ((str = in.readLine()) != null) {
            // use comma as separator
            lineItem = str.split(",");

            //check if the test file has an associated production file
            if(lineItem.length ==2){
                testFile = new TestFile(lineItem[0], lineItem[1]);
            }
            else{
                testFile = new TestFile(lineItem[0], lineItem[1]);
            }

            testFiles.add(testFile);
        }


        /*
          Initialize the output file - Create the output file and add the column names
         */
        ResultsWriter resultsWriter = ResultsWriter.createResultsWriter();
        List<String> columnNames;
        List<String> columnValues;

        columnNames = testSmellDetector.getTestSmellNames();
        columnNames.add(0, "App");
        columnNames.add(1, "ProductionFilePath");
        resultsWriter.writeColumnName(columnNames);

        /*
          Iterate through all test files to detect smells and then write the output
        */
        TestFile tempFile;
        for (TestFile file : testFiles) {
            System.out.println("Processing: "+file.getProductionFilePath());

            //detect smells
            tempFile = testSmellDetector.detectSmells(file);

            //write output
            columnValues = new ArrayList<>();
            columnValues.add(file.getApp());
            columnValues.add(file.getProductionFilePath());
            for (AbstractSmell smell : tempFile.getCodeSmells()) {
                try {
                    columnValues.add(String.valueOf(smell.getHasSmell()));
                }
                catch (NullPointerException e){
                    columnValues.add("");
                }
            }
            resultsWriter.writeLine(columnValues);
        }

        System.out.println("end");
    }


}
