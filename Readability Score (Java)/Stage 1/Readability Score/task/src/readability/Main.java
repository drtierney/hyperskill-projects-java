package readability;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        System.out.println(calculateTextDifficulty(input));
    }

    private static String calculateTextDifficulty(String input) {
        return input.length() > 100 ? "HARD" : "EASY";
    }

}
