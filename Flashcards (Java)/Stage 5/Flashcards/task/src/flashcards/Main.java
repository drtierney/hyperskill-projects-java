package flashcards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Main {
    static Boolean exit = false;
    static Scanner scanner = new Scanner(System.in);
    static Map<String, String> flashcards = new LinkedHashMap<>();

    public static void main(String[] args) {
        flashcards.clear();
        do {
            menu();
            String action = scanner.nextLine();
            switch (action) {
                case "add":
                    addCard();
                    break;
                case "remove":
                    removeCard();
                    break;
                case "import":
                    importFromFile();
                    break;
                case "export":
                    exportToFile();
                    break;
                case "ask":
                    askForCardDefinition();
                    break;
                case "exit":
                    exit();
                    break;
                default:
                    System.out.println("Unknown action.");
            }
        } while (!exit);

    }


    private static void addCard() {
        String term = "";
        String definition = "";

        System.out.println("The card:");
        term = scanner.nextLine();
        if (flashcards.containsKey(term)) {
            System.out.printf("The card \"%s\" already exists.\n", term);
            return;
        }

        System.out.println("The definition of the card:");
        definition = scanner.nextLine();
        if (flashcards.containsValue(definition)) {
            System.out.printf("The definition \"%s\" already exists.\n", definition);
            return;
        }

        System.out.printf("The pair (\"%s\":\"%s\") has been added.\n", term, definition);
        flashcards.put(term, definition);
        }

    private static void removeCard() {
        System.out.println("The card:");
        String cardToRemove = scanner.nextLine();
        if(flashcards.containsKey(cardToRemove)){
            flashcards.remove(cardToRemove);
            System.out.println("The card has been removed.");
        } else {
            System.out.printf("Can't remove \"%s\": there is no such card.\n", cardToRemove);
        }
    }

    private static void importFromFile() {
        System.out.println("File name:");
        File file = new File(scanner.nextLine());
        int updateCount = 0;
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                String[] line;
                line = scanner.nextLine().split(":");
                var term = line[0];
                var definition = line[1];
                flashcards.put(term, definition);
                updateCount++;
            }
            System.out.printf("%d cards have been loaded.\n", updateCount);
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
    }

    private static void exportToFile() {
        System.out.println("File name:");
        File file = new File(scanner.nextLine());
        try (PrintWriter printWriter = new PrintWriter(file)) {
            for (var entry : flashcards.entrySet()){
                String line = entry.getKey() + ":" + entry.getValue();
                printWriter.println(line);
            }
        } catch (IOException e) {
            System.out.printf("An exception occurs %s", e.getMessage());
        }
        System.out.printf("%d cards have been saved.\n", flashcards.size());
    }

    private static void askForCardDefinition() {
        Object[] terms = flashcards.keySet().toArray();
        Random random = new Random();
        System.out.println("How many times to ask?");
        int t = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < t; i++) {
            String term = String.valueOf(terms[random.nextInt(terms.length)]);
            String definition = flashcards.get(term);
            System.out.printf("Print the definition of \"%s\":\n", term);
            String answer = scanner.nextLine();
            System.out.printf(answer.equalsIgnoreCase(definition) ? "Correct answer.\n" : flashcards.containsValue(answer) ?
                    "Wrong answer. The correct one is \"%1$s\", you've just written the definition of \"%2$s\"\n" :
                    "Wrong answer. The correct one is \"%1$s\".\n", definition, getKeyFromValue(flashcards, answer));
        }
    }

    public static void exit() {
        System.out.println("Bye!");
        exit = true;
    }

    public static void menu() {
        System.out.println("Input the action (add, remove, import, export, ask, exit):");
    }

    private static String getKeyFromValue(Map<String, String> map, String value) {
        for (var entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
