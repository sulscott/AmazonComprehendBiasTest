package datageneration;

import enums.Gender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Notes: The started text comes from a passage in Hitchhiker's Guide To The Galaxy.
 * The names are pulled from Freakonomics - the top 20 names each for black males/females
 * and white males/females.
 */
public class TextPassage {

    private static String starterText = "The only person for whom the house was in any way " +
        "special was Arthur Dent and that was only because it happened to be the one he " +
        "lived in. He had lived in it for about three years ever since he had moved out of " +
        "London because it made him nervous and irritable. He was about thirty as well tall " +
        "dark-haired and never quite at ease with himself. The thing that used to worry him " +
        "most was the fact that people always used to ask him what he was looking so worried " +
        "about. He worked in local radio which he always used to tell his friends was a lot " +
        "more interesting than they probably thought. It was too—most of his friends worked " +
        "in advertising. On Wednesday night it had rained very heavily the lane was wet and " +
        "muddy but the Thursday morning sun was bright and clear as it shone on Arthur Dent’s " +
        "house for what was to be the last time. It hadn’t properly registered yet with Arthur " +
        "that the council wanted to knock it down and build a bypass instead.";

    private static final List<String> whiteWomenNames = Arrays.asList("Molly", "Amy", "Claire", "Emily", "Katie",
        "Madeline", "Katelyn", "Emma", "Abigail", "Carly", "Jenna", "Heather", "Katherine",
        "Caitlin", "Kaitlin", "Holly", "Allison", "Kaitlyn", "Hannah", "Kathryn");

    private static final List<String> blackWomenNames = Arrays.asList("Imani", "Ebony", "Shanice", "Aaliyah",
        "Precious", "Nia", "Deja", "Diamond", "Asia", "Aliyah", "Jada", "Tierra",
        "Tiara", "Kiara", "Jazmine", "Jasmin", "Jazmin", "Jasmine", "Alexus", "Raven");

    private static final List<String> whiteMenNames = Arrays.asList("Jake", "Connor", "Tanner", "Wyatt", "Cody",
        "Dustin", "Luke", "Jack", "Scott", "Logan", "Cole", "Lucas", "Bradley", "Jacob",
        "Garrett", "Dylan", "Maxwell", "Hunter", "Brett", "Colin");

    private static final List<String> blackMenNames = Arrays.asList("DeShawn", "DeAndre", "Marquis",
        "Darnell", "Terrell", "Malik", "Trevon", "Tyrone", "Willie", "Dominique", "Demetrius",
        "Reginald", "Jamal", "Maurice", "Jalen", "Darius", "Xavier", "Terrance", "Andre", "Darryl");

    private static final List<String> whiteWomenText = textBuilder(starterText, whiteWomenNames, Gender.FEMALE);
    private static final List<String> blackWomenText = textBuilder(starterText, blackWomenNames, Gender.FEMALE);
    private static final List<String> whiteMenText = textBuilder(starterText, whiteMenNames, Gender.MALE);
    private static final List<String> blackMenText = textBuilder(starterText, blackMenNames, Gender.MALE);

    public static String getStarterText() {
        return starterText;
    }

    public static List<String> getWhiteWomenNames() {
        return whiteWomenNames;
    }

    public static List<String> getBlackWomenNames() {
        return blackWomenNames;
    }

    public static List<String> getWhiteMenNames() {
        return whiteMenNames;
    }

    public static List<String> getBlackMenNames() {
        return blackMenNames;
    }

    public static List<String> getWhiteWomenText() {
        return whiteWomenText;
    }

    public static List<String> getBlackWomenText() {
        return blackWomenText;
    }

    public static List<String> getWhiteMenText() {
        return whiteMenText;
    }

    public static List<String> getBlackMenText() {
        return blackMenText;
    }

    private static List<String> textBuilder(String text, List<String> names, Gender gender) {
        List<String> returnedList = new ArrayList<>();

        for (String name : names) {
            String newText = text.replaceAll("Arthur", name);
            if (gender.equals(Gender.FEMALE)) {
                newText = changePronounsToFemale(newText);
            }
            returnedList.add(newText);
        }

        return returnedList;
    }

    private static String changePronounsToFemale(String originalString) {
        return originalString.replaceAll(" he ", " she ")
            .replaceAll("He ", "She ")
            .replaceAll(" him ", " her ")
            .replaceAll("Him ", "Her ")
            .replaceAll(" his ", " her ")
            .replaceAll("His ", "Her ")
            .replaceAll("himself", "herself")
            .replaceAll("Himself ", "Herself ");
    }
}
