package flashcards;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String definition = scanner.nextLine();
        String term = scanner.nextLine();
        String answer = scanner.nextLine();

        System.out.println(answer.equalsIgnoreCase(term) ? "right" : "wrong");
    }
}
