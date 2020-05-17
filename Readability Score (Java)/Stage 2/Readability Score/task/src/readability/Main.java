package readability;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        System.out.println(calculateTextDifficulty(input));
    }

    private static String calculateTextDifficulty(String input) {
        String[] sentences = input.split("[.!?]");
        String[] words = input.split("[\\S]+");
        float avgSentenceLen = (float) words.length / sentences.length;

        return avgSentenceLen > 10.0 ? "HARD" : "EASY";
    }

}
