package datageneration;

import enums.Gender;
import enums.Race;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenerateSampleDataReport {

    private static final String DIRECTORY = "/Users/sulscott/Documents/Comprehend_Bias/";

    /**
     * Generates a csv report using the text from datageneration.TextPassage and saves it to specified file location.
     * This file will be given to Comprehend for sentiment analysis.
     * @param fileName location to be saved.
     * @throws IOException exception.
     */
    public static void generateCSVReport(String fileName) throws IOException {
        FileWriter csvWriter = new FileWriter(generateFilePath(fileName));
        csvWriter.append("Text");
        csvWriter.append(",");
        csvWriter.append("Name");
        csvWriter.append(",");
        csvWriter.append("enums.Gender");
        csvWriter.append(",");
        csvWriter.append("enums.Race");
        csvWriter.append("\n");

        for (List<String> rowData : generateBlackMenData()) {
            csvWriter.append(String.join(",", rowData));
            csvWriter.append("\n");
        }

        for (List<String> rowData : generateWhiteMenData()) {
            csvWriter.append(String.join(",", rowData));
            csvWriter.append("\n");
        }

        for (List<String> rowData : generateWhiteWomenData()) {
            csvWriter.append(String.join(",", rowData));
            csvWriter.append("\n");
        }

        for (List<String> rowData : generateBlackWomenData()) {
            csvWriter.append(String.join(",", rowData));
            csvWriter.append("\n");
        }

        csvWriter.flush();
        csvWriter.close();
    }

    private static String generateFilePath(String fileLocation) {
        return DIRECTORY + fileLocation;
    }

    private static List<List<String>> generateBlackMenData() {
        List<List<String>> rows = new ArrayList<>();
        for (int i = 0; i < TextPassage.getBlackMenNames().size(); i++) {
            String text = TextPassage.getBlackMenText().get(i);
            String name = TextPassage.getBlackMenNames().get(i);
            rows.add(Arrays.asList(text, name, Gender.MALE.toString(), Race.BLACK.toString()));
        }
        return rows;
    }

    private static List<List<String>> generateWhiteMenData() {
        List<List<String>> rows = new ArrayList<>();
        for (int i = 0; i < TextPassage.getWhiteMenNames().size(); i++) {
            String text = TextPassage.getWhiteMenText().get(i);
            String name = TextPassage.getWhiteMenNames().get(i);
            rows.add(Arrays.asList(text, name, Gender.MALE.toString(), Race.WHITE.toString()));
        }
        return rows;
    }

    private static List<List<String>> generateWhiteWomenData() {
        List<List<String>> rows = new ArrayList<>();
        for (int i = 0; i < TextPassage.getWhiteWomenNames().size(); i++) {
            String text = TextPassage.getWhiteWomenText().get(i);
            String name = TextPassage.getWhiteWomenNames().get(i);
            rows.add(Arrays.asList(text, name, Gender.FEMALE.toString(), Race.WHITE.toString()));
        }
        return rows;
    }

    private static List<List<String>> generateBlackWomenData() {
        List<List<String>> rows = new ArrayList<>();
        for (int i = 0; i < TextPassage.getBlackWomenNames().size(); i++) {
            String text = TextPassage.getBlackWomenText().get(i);
            String name = TextPassage.getBlackWomenNames().get(i);
            rows.add(Arrays.asList(text, name, Gender.FEMALE.toString(), Race.BLACK.toString()));
        }
        return rows;
    }

    public static void main(String[] args) throws IOException {
        generateCSVReport("text_and_identifiers.csv");
    }
}
