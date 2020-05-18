package readability;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static java.util.Map.entry;

public class Main {
    public static void main(String[] args) throws IOException {
        String fileName = args[0];

        System.out.println(fileName);
        String text = new String(Files.readAllBytes(Paths.get(fileName)));

        System.out.println("The text is:");
        System.out.println(text);
        int wordCount = calculateWordCount(text);
        int sentenceCount = calculateSentenceCount(text);
        int characterCount = calculateCharacterCount(text);
        double score = calculateAutomatedReadabilityIndexScore(wordCount, sentenceCount, characterCount);
        String age = calculateAutomatedReadabilityIndexAge(score);
        System.out.printf("Words: %d\n", wordCount);
        System.out.printf("Sentences: %d\n", sentenceCount);
        System.out.printf("Characters: %d\n", characterCount);
        System.out.printf("The score is: %.2f\n", score);
        System.out.printf("This text should be understood by %s year olds.", age);
    }

    private static int calculateWordCount(String text){
        return text.split("[\\S]+").length;
    }

    private static int calculateSentenceCount(String text){
        return text.split("[.!?]").length;
    }

    private static int calculateCharacterCount(String text){
        return text.replaceAll("\\s","").length();
    }

    private static double calculateAutomatedReadabilityIndexScore(double wordCount, double sentenceCount, double characterCount){
        return 4.71 * (characterCount / wordCount) + 0.5 * (wordCount / sentenceCount) - 21.43;
    }

    private static String calculateAutomatedReadabilityIndexAge(double score){
        //https://en.wikipedia.org/wiki/Automated_readability_index
        Map<Integer, String> ages = Map.ofEntries(
                entry(1, "5-6"),
                entry(2, "6-7"),
                entry(3, "7-9"),
                entry(4, "9-10"),
                entry(5, "10-11"),
                entry(6, "11-12"),
                entry(7, "12-13"),
                entry(8, "13-14"),
                entry(9, "14-15"),
                entry(10, "15-16"),
                entry(11, "16-17"),
                entry(12, "17-18"),
                entry(13, "18-24"),
                entry(14, "24+")
        );
        return ages.get((int) Math.ceil(score));
    }

}
