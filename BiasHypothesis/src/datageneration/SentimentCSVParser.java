package datageneration;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import enums.Gender;
import enums.Race;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SentimentCSVParser {

    private static final String DIRECTORY = "/Users/sulscott/Documents/Comprehend_Bias/";


    /**
     * Returns a List of double arrays. the first array represents the positive sentiment scores (confidence)
     * for each "white sounding" name in the data set, male and female. The second array represents the positive
     * sentiment scores (confidence) for each "black sounding" name in the data set, male and female.
     *
     * @param fileName the file to search. See method comments for headers.
     * @return List of double arrays as described above.
     * @throws IOException exception.
     * @throws CsvValidationException exception.
     */
    public List<double[]> getBlackNameVsWhiteNameArrays(String fileName) throws IOException, CsvValidationException {
        // input file has headings in this order:
        // text, name, gender, race, sentimentScore, positive, mixed, negative, neutral
        String strFile = DIRECTORY + fileName;
        CSVReader reader = new CSVReader(new FileReader(strFile));
        String[] nextLine;

        List<Double> blackNameList = new ArrayList<>();
        List<Double> whiteNameList = new ArrayList<>();

        while ((nextLine = reader.readNext()) != null) {
            if (nextLine[3].equals(Race.BLACK.toString())) {
                blackNameList.add(Double.valueOf(nextLine[5]));
            } else {
                whiteNameList.add(Double.valueOf(nextLine[5]));
            }
        }

        double[] blackNameArray = convertToPrimitiveDoubleArray(blackNameList);
        double[] whiteNameArray = convertToPrimitiveDoubleArray(whiteNameList);

        reader.close();

        return Arrays.asList(blackNameArray, whiteNameArray);
    }

    /**
     * Returns a List of double arrays. the first array represents the positive sentiment scores (confidence)
     * for each "female sounding" name in the data set, black and white. The second array represents the positive
     * sentiment scores (confidence) for each "male sounding" name in the data set, black and white.
     *
     * @param fileName the file to search. See method comments for headers.
     * @return List of double arrays as described above.
     * @throws IOException exception.
     * @throws CsvValidationException exception.
     */
    public List<double[]> getMaleNameVsFemaleNameArrays(String fileName) throws IOException, CsvValidationException {
        // input file has headings in this order:
        // text, name, gender, race, sentimentScore, positive, mixed, negative, neutral
        String strFile = DIRECTORY + fileName;
        CSVReader reader = new CSVReader(new FileReader(strFile));
        String[] nextLine;

        List<Double> maleNameList = new ArrayList<>();
        List<Double> femaleNameList = new ArrayList<>();

        while ((nextLine = reader.readNext()) != null) {
            if (nextLine[2].equals(Gender.MALE.toString())) {
                maleNameList.add(Double.valueOf(nextLine[5]));
            } else {
                femaleNameList.add(Double.valueOf(nextLine[5]));
            }
        }

        double[] maleNameArray = convertToPrimitiveDoubleArray(maleNameList);
        double[] femaleNameArray = convertToPrimitiveDoubleArray(femaleNameList);

        reader.close();

        return Arrays.asList(maleNameArray, femaleNameArray);
    }


    private static double[] convertToPrimitiveDoubleArray(List<Double> lst) {
        int size = lst.size();

        double[] doubleArray = new double[size];

        for (int i = 0; i < size; i++) {
            doubleArray[i] = lst.get(i);
        }

        return doubleArray;
    }


}
