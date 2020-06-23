package readability;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;

import static java.util.Map.entry;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String fileName = args[0];

        System.out.println(fileName);
        String text = new String(Files.readAllBytes(Paths.get(fileName)));

        System.out.println("The text is:");
        System.out.println(text);
        System.out.println();
        int wordCount = calculateWordCount(text);
        int sentenceCount = calculateSentenceCount(text);
        int characterCount = calculateCharacterCount(text);
        int syllableCount = calculateSyllableCount(text);
        int polySyllableCount = calculatePolySyllableCount(text);

        System.out.printf("Words: %d\n", wordCount);
        System.out.printf("Sentences: %d\n", sentenceCount);
        System.out.printf("Characters: %d\n", characterCount);
        System.out.printf("Syllables: %d\n", syllableCount);
        System.out.printf("Polysyllables: %d\n", polySyllableCount);

        double ariScore = calculateARIndexScore(wordCount, sentenceCount, characterCount);
        double fkScore = calculateFKIndexScore(wordCount, sentenceCount, syllableCount);
        double smogScore = calculateSMOGIndexScore(sentenceCount, polySyllableCount);
        double clScore = calculateCLIndexScore(sentenceCount, wordCount, characterCount);

        int ariAge = calculateAgeFromIndexScore(ariScore);
        int fkAge = calculateAgeFromIndexScore(fkScore);
        int smogAge = calculateAgeFromIndexScore(smogScore);
        int clAge = calculateAgeFromIndexScore(clScore);

        double avgAge = (double) (ariAge + fkAge + smogAge + clAge) / 4;

        System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
        String selection = scanner.nextLine().trim();
        System.out.println("");
        switch(selection){
            case "ARI":
                System.out.printf("Automated Readability Index: %.2f (about %d year olds).%n", ariScore, ariAge);
                break;
            case "FK":
                System.out.printf("Flesch–Kincaid readability tests: %.2f (about %d year olds).%n", fkScore, fkAge);
                break;
            case "SMOG":
                System.out.printf("Simple Measure of Gobbledygook: %.2f (about %d year olds).%n", smogScore, smogAge);
                break;
            case "CL":
                System.out.printf("Coleman–Liau index: %.2f (about %d year olds).%n", clScore, clAge);
                break;
            case "all":
                System.out.printf("Automated Readability Index: %.2f (about %d year olds).%n", ariScore, ariAge);
                System.out.printf("Flesch–Kincaid readability tests: %.2f (about %d year olds).%n", fkScore, fkAge);
                System.out.printf("Simple Measure of Gobbledygook: %.2f (about %d year olds).%n", smogScore, smogAge);
                System.out.printf("Coleman–Liau index: %.2f (about %d year olds).%n", clScore, clAge);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + selection);
        }

        System.out.printf("This text should be understood in average by %.2f year olds.", avgAge);
    }

    private static int calculateWordCount(String text) {
        return text.split(" ").length;
    }

    private static int calculateSentenceCount(String text) {
        return text.split("[.!?]").length;
    }

    private static int calculateCharacterCount(String text) {
        return text.replaceAll("\\s","").length();
    }

    private static int calculateSyllableCount(String text) {
        String[] words = text.split(" ");
        int syllableCount = 0;

        for (String word : words) {
            syllableCount += syllablesPerWord(word);
        }

        return syllableCount;
    }

    private static int calculatePolySyllableCount(String text) {
        String[] words = text.split(" ");
        int polySyllableCount = 0;

        for (String word : words){
            int syllables = syllablesPerWord(word);
            if (syllables > 2)
                polySyllableCount++;
        }

        return polySyllableCount;
    }

    private static int syllablesPerWord(String word) {
        int count; // count of vowels
        count = word
                .replaceAll("[aeiouy]{2,}", "a") //replace double vowels
                .replaceAll("e$", "") // replace word ending in e
                .replaceAll("[^aeiouy]", "") // replace non-vowels
                .length();

        return Math.max(1, count);
    }

    private static double calculateARIndexScore(double wordCount, double sentenceCount, double characterCount) {
        return 4.71 * (characterCount / wordCount) + 0.5 * (wordCount / sentenceCount) - 21.43;
    }

    private static double calculateFKIndexScore(int wordCount, int sentenceCount, int syllableCount) {
        return 0.39 * wordCount / sentenceCount + 11.8 * syllableCount / wordCount - 15.59;
    }

    private static double calculateSMOGIndexScore(int sentenceCount, int polySyllableCount) {
        return 1.043 * Math.sqrt(polySyllableCount * 30 / sentenceCount) + 3.1291;
    }

    private static double calculateCLIndexScore(int sentenceCount, int wordCount, int characterCount) {
        return 0.0588 * (characterCount * 100 / (double) wordCount) - 0.296 * (sentenceCount * 100 / (double) wordCount) - 15.8;
    }

    private static int calculateAgeFromIndexScore(double score){
        //https://en.wikipedia.org/wiki/Automated_readability_index
        int scoreCeil = (int) Math.ceil(score);
        Map<Integer, Integer> ages = Map.ofEntries(
                entry(1, 6),
                entry(2, 7),
                entry(3, 9),
                entry(4, 10),
                entry(5, 11),
                entry(6, 12),
                entry(7, 13),
                entry(8, 14),
                entry(9, 15),
                entry(10, 16),
                entry(11, 17),
                entry(12, 18),
                entry(13, 24),
                entry(14, 25)
        );
        return ages.getOrDefault((int) scoreCeil, 25);
    }
}
