package search;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Task {
    private static boolean exit;
    public SearchEngine searchEngine;

    public Task(String[] args) throws IOException {
        String inputFile = argsParse(args);
        searchEngine = new SearchEngine(importRows(inputFile));
    }

    public void runTask() {
        Scanner scanner = new Scanner(System.in);

        do {
            menu();
            int action = Integer.parseInt(scanner.nextLine());
            switch (action) {
                case 1:
                    search(scanner);
                    break;
                case 2:
                    System.out.println(searchEngine.toString());
                    break;
                case 0:
                    exit();
                    break;
                default:
                    System.out.println("Incorrect option! Try again.\n");
            }
        } while (!exit);
    }

    private String argsParse(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--data")) {
                return args[i + 1];
            }
        }
        return "";
    }

    public static List<String> importRows(String filename) throws IOException {

        FileReader fileReader = new FileReader(filename);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        List<String> rows = new ArrayList<String>();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            rows.add(line);
        }
        bufferedReader.close();

        return rows;
    }

    private static void menu() {
        String menu = String.join("\n",
                "=== Menu ===",
                "1. Search for matching rows",
                "2. Print all rows",
                "0. Exit"
        );
        System.out.println(menu);
    }

    private void search(Scanner scanner) {
        System.out.println("\nSelect a search strategy: ALL, ANY, NONE");
        String strategy = scanner.nextLine();
        switch (strategy) {
            case "ALL":
                searchEngine.setSearchStrategy(new SearchAll());
                break;
            case "ANY":
                searchEngine.setSearchStrategy(new SearchAny());
                break;
            case "NONE":
                searchEngine.setSearchStrategy(new SearchNone());
                break;
            default:
                System.out.println("\nMatching strategy not found.");
                return;
        }
        System.out.println("\nEnter a query:");
        String query = scanner.nextLine();
        System.out.println(searchEngine.search(query));
    }

    private static void exit() {
        System.out.println("Bye!");
        exit = true;
    }
}
