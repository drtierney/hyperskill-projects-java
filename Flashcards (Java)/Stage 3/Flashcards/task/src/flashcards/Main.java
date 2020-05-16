package flashcards;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<String, String> flashcards = new LinkedHashMap<>();

        System.out.print("Input the number of cards:\n");
        int cards = Integer.parseInt(scanner.nextLine());

        int card = 0;
        while (card < cards) {
            System.out.printf("The card #%d:\n", card + 1);
            String term = scanner.nextLine();
            System.out.printf("The definition of card #%d:\n", card + 1);
            String definition = scanner.nextLine();
            flashcards.putIfAbsent(term, definition);
            card++;
        }

        for (String term : flashcards.keySet()){
            String definition = flashcards.get(term);
            System.out.printf("Print the definition of \"%s\":\n", term);
            String answer = scanner.nextLine();
            System.out.printf(answer.equalsIgnoreCase(definition) ?
                    "Correct answer.\n" : "Wrong answer. The correct one is \"%s\".\n", definition);
        }
    }
}
