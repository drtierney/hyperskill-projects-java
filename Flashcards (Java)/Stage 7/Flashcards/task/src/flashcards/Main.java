package flashcards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    static Boolean exit = false;
    static Scanner scanner = new Scanner(System.in);
    static Map<String, String> flashcards = new LinkedHashMap<>();
    static Map<String, Integer> mistakes = new LinkedHashMap<>();
    static LinkedList<String> logger = new LinkedList<>();
    static String importFile = "";
    static String exportFile = "";

    public static void main(String[] args) {
        flashcards.clear();
        argsParse(args);
        if (!importFile.isBlank()){
            importFromFile(importFile);
        }
        do {
            menu();
            String action = scanner.nextLine();
            logger.add(action);
            switch (action.toLowerCase()) {
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
                case "log":
                    loggerOutToFile();
                    break;
                case "hardest card":
                    hardestCard();
                    break;
                case "reset stats":
                    resetStats();
                    break;
                case "exit":
                    exit();
                    break;
                default:
                    System.out.println("Unknown action.");
            }
        } while (!exit);

    }

    private static void argsParse(String[] args) {
        for (int i = 0; i < args.length; i++){
            switch (args[i]){
                case "-import":
                    importFile = args[i + 1];
                    break;
                case "-export":
                    exportFile = args[i + 1];
                    break;
            }
        }
    }

    private static void addCard() {
        String term;
        String definition;

        System.out.println("The card:");
        logger.add("The card:");
        term = scanner.nextLine();
        logger.add(term);
        if (flashcards.containsKey(term)) {
            String output = String.format("The card \"%s\" already exists.\n", term);
            System.out.print(output);
            logger.add(output);
            return;
        }

        System.out.println("The definition of the card:");
        logger.add("The definition of the card:");
        definition = scanner.nextLine();
        logger.add(definition);
        if (flashcards.containsValue(definition)) {
            String output = String.format("The definition \"%s\" already exists.\n", definition);
            System.out.print(output);
            logger.add(output);
            return;
        }

        String output = String.format("The pair (\"%s\":\"%s\") has been added.\n", term, definition);
        System.out.print(output);
        flashcards.put(term, definition);
        mistakes.put(term, 0);
        }

    private static void removeCard() {
        System.out.println("The card:");
        logger.add("The card:");
        String cardToRemove = scanner.nextLine();
        logger.add(cardToRemove);
        if(flashcards.containsKey(cardToRemove)){
            flashcards.remove(cardToRemove);
            mistakes.remove(cardToRemove);
            System.out.println("The card has been removed.");
            logger.add("The card has been removed.");
        } else {
            String output = String.format("Can't remove \"%s\": there is no such card.\n", cardToRemove);
            System.out.print(output);
        }
    }

    private static void importFromFile() {
        System.out.println("File name:");
        logger.add("File name:");
        String infile = scanner.nextLine();
        logger.add(infile);
        File file = new File(infile);
        int updateCount = 0;
        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNext()) {
                String[] line;
                line = fileScanner.nextLine().split(":");
                var term = line[0];
                var definition = line[1];
                var mistakeCount = Integer.parseInt(line[2]);
                flashcards.put(term, definition);
                mistakes.put(term, mistakeCount);
                updateCount++;
            }
            String output = String.format("%d cards have been loaded.\n", updateCount);
            System.out.print(output);
            logger.add(output);
        } catch (FileNotFoundException e) {
            String output = "File not found.";
            System.out.println(output);
            logger.add(output);
        }
    }

    private static void importFromFile(String importFile){
        File file = new File(importFile);
        int updateCount = 0;
        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNext()) {
                String[] line;
                line = fileScanner.nextLine().split(":");
                var term = line[0];
                var definition = line[1];
                var mistakeCount = Integer.parseInt(line[2]);
                flashcards.put(term, definition);
                mistakes.put(term, mistakeCount);
                updateCount++;
            }
            String output = String.format("%d cards have been loaded.\n", updateCount);
            System.out.print(output);
            logger.add(output);
        } catch (FileNotFoundException e) {
            String output = "File not found.";
            System.out.println(output);
            logger.add(output);
        }
    }

    private static void exportToFile() {
        System.out.println("File name:");
        logger.add("File name:");
        String outfile = scanner.nextLine();
        logger.add(outfile);
        File file = new File(outfile);
        try (PrintWriter printWriter = new PrintWriter(file)) {
            for (var entry : flashcards.entrySet()){
                int mistakeCount = mistakes.get(entry.getKey());
                String line = entry.getKey() + ":" + entry.getValue() + ":" + mistakeCount;
                printWriter.println(line);
            }
        } catch (IOException e) {
            String error = String.format("An exception occurs %s", e.getMessage());
            System.out.print(error);
            logger.add(error);
        }
        String output = String.format("%d cards have been saved.\n", flashcards.size());
        System.out.print(output);
    }

    private static void exportToFile(String exportFile){
        File file = new File(exportFile);
        try (PrintWriter printWriter = new PrintWriter(file)) {
            for (var entry : flashcards.entrySet()){
                int mistakeCount = mistakes.get(entry.getKey());
                String line = entry.getKey() + ":" + entry.getValue() + ":" + mistakeCount;
                printWriter.println(line);
            }
        } catch (IOException e) {
            String error = String.format("An exception occurs %s", e.getMessage());
            System.out.print(error);
            logger.add(error);
        }
        String output = String.format("%d cards have been saved.\n", flashcards.size());
        System.out.print(output);
    }

    private static void askForCardDefinition() {
        Object[] terms = flashcards.keySet().toArray();
        Random random = new Random();
        System.out.println("How many times to ask?");
        logger.add("How many times to ask?");
        int t = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < t; i++) {
            String term = String.valueOf(terms[random.nextInt(terms.length)]);
            String definition = flashcards.get(term);
            String output = String.format("Print the definition of \"%s\":\n", term);
            System.out.print(output);
            logger.add(output);
            String answer = scanner.nextLine();
            logger.add(answer);
            if(answer.equalsIgnoreCase(definition)){
                String correct = "Correct answer.";
                System.out.println(correct);
                logger.add(correct);
            } else {
                mistakes.put(term, mistakes.get(term) + 1);
                String wrong = String.format(flashcards.containsValue(answer) ?
                        "Wrong answer. The correct one is \"%1$s\", you've just written the definition of \"%2$s\"\n" :
                        "Wrong answer. The correct one is \"%1$s\".\n", definition, getKeyFromValue(flashcards, answer));
                System.out.print(wrong);
                logger.add(wrong);
            }
        }
    }

    private static void loggerOutToFile() {
        System.out.println("File name:");
        logger.add("File name:");
        String logfile = scanner.nextLine();
        logger.add(logfile);
        File file = new File(logfile);
        try (PrintWriter printWriter = new PrintWriter(file)) {
            for (var line : logger) {
                printWriter.println(line);
            }
        } catch (IOException e) {
            String error = String.format("An exception occurs %s", e.getMessage());
            System.out.print(error);
            logger.add(error);
        }
        String output = "The log has been saved.\n";
        logger.add(output);
        System.out.print(output);
    }

    private static void hardestCard() {
        int count = 0;
        for (var mistakeCount : mistakes.values()){
            if (mistakeCount > count){
                count = mistakeCount;
            }
        }
        //System.out.println(count);
        if (count > 0){
            Set<String> keys = new HashSet<>();
            for (var entry : mistakes.entrySet()) {
                if (entry.getValue().equals(count)) {
                    keys.add(entry.getKey());
                }
            }
            boolean moreThanOneCard = keys.size() > 1;
            if (moreThanOneCard){
                String result = keys.stream()
                        .map(s -> "\"" + s + "\"")
                        .collect(Collectors.joining(", "));
                String output = String.format("The hardest cards are %s. You have %d errors answering them.\n", result, count);
                System.out.print(output);
                logger.add(output);
            } else {
                String result = keys.stream()
                        .map(s -> "\"" + s + "\"")
                        .collect(Collectors.joining(""));
                String output = String.format("The hardest card is %s. You have %d errors answering it.\n", result, count);
                System.out.print(output);
                logger.add(output);
            }
        } else {
            String output = "There are no cards with errors.";
            System.out.println(output);
            logger.add(output);
        }
    }

    private static void resetStats() {
        mistakes.replaceAll((k, v) -> 0);
        String output = "Card statistics has been reset.";
        System.out.println(output);
        logger.add(output);
    }

    public static void exit() {
        System.out.println("Bye!");
        if(!exportFile.isBlank()){
            exportToFile(exportFile);
        }
        exit = true;
    }

    public static void menu() {
        String menu = "Input the action (add, remove, import, export, ask, log, hardest card, reset stats, exit):";
        System.out.println(menu);
        logger.add(menu);
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
