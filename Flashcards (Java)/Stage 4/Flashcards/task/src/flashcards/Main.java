package flashcards;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<String, String> flashcards = new LinkedHashMap<>();
        String aTerm = "";
        String aDefinition = "";

        System.out.print("Input the number of cards:\n");
        int cards = Integer.parseInt(scanner.nextLine());

        int card = 0;
        while (card < cards) {
            System.out.printf("The card #%d:\n", card + 1);
            do {
                aTerm = scanner.nextLine();
                if (flashcards.containsKey(aTerm)){
                    System.out.printf("The card \"%s\" already exists. Try again:\n", aTerm);
                }
            } while (flashcards.containsKey(aTerm) || aTerm.equals(""));

            System.out.printf("The definition of card #%d:\n", card + 1);
            do {
                aDefinition = scanner.nextLine();
                if(flashcards.containsValue(aDefinition)){
                    System.out.printf("The definition \"%s\" already exists. Try again:\n", aDefinition);
                }
            } while (flashcards.containsValue(aDefinition) || aDefinition.equals(""));

            flashcards.putIfAbsent(aTerm, aDefinition);
            card++;
        }

        for (String term : flashcards.keySet()){
            String definition = flashcards.get(term);
            System.out.printf("Print the definition of \"%s\":\n", term);
            String answer = scanner.nextLine();
            System.out.printf(answer.equalsIgnoreCase(definition) ? "Correct answer.\n" :
            flashcards.containsValue(answer) ?
            "Wrong answer. The correct one is \"%1s\", you've just written the definition of \"%2s\" \n" :
            "Wrong answer. The correct one is \"%1s\".\n",
            definition, getKeyFromValue(flashcards, answer));
        }

    }

    public static String getKeyFromValue(Map<String, String> map, String value) {
        for (var entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
