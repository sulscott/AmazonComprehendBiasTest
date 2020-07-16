package runtest;

import com.opencsv.exceptions.CsvValidationException;
import datageneration.SentimentCSVParser;

import java.io.IOException;
import java.util.List;

public class GetResults {

    private static final double ALPHA = .10;

    public static void main(String[] args) throws IOException, CsvValidationException {

        SentimentCSVParser parser = new SentimentCSVParser();
        RunTTest runTTest = new RunTTest();

        List<double[]> compareBlackAndWhiteNames = parser.getBlackNameVsWhiteNameArrays("final.csv");
        List<double[]> compareMaleAndFemaleNames = parser.getMaleNameVsFemaleNameArrays("final.csv");

        boolean blackAndWhiteObservedPValue =
            runTTest.tTest(compareBlackAndWhiteNames.get(0), compareBlackAndWhiteNames.get(1), ALPHA);

        boolean maleAndFemaleObservedPvalue =
            runTTest.tTest(compareMaleAndFemaleNames.get(0), compareMaleAndFemaleNames.get(1), ALPHA);

        System.out.println(String.format("At an alpha level of %s, can we reject the hypothesis that black names and " +
            "white names have the same mean positive sentiment score: ", ALPHA) + blackAndWhiteObservedPValue);
        System.out.println("\n");
        System.out.println(String.format("At an alpha level of %s, can we reject the hypothesis that male names and " +
            "female names have the same mean positive sentiment score: ", ALPHA) + maleAndFemaleObservedPvalue);
    }
}
