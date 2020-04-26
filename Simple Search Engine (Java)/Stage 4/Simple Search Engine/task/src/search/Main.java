package search;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    static Boolean exit = false;
    static Scanner scanner = new Scanner(System.in);
    static String[] rows;

    public static void main(String[] args) throws IOException {
        String inputFile = "";
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--data")) {
                inputFile = args[i + 1];
            }

            rows = importRows(inputFile);
        }

        do {
            menu();
            int action = Integer.parseInt(scanner.nextLine());
            switch (action){
                case 1:
                    searchRows(scanner, rows);
                    break;
                case 2:
                    printAllRows(rows);
                    break;
                case 0:
                    exit();
                    break;
                default:
                    System.out.println("Incorrect option! Try again.\n");
            }
        } while (!exit);

    }

    public static String[] importRows(String filename) throws IOException {

        FileReader fileReader = new FileReader(filename);

        BufferedReader bufferedReader = new BufferedReader(fileReader);
        List<String> lines = new ArrayList<String>();
        String line = null;

        while ((line = bufferedReader.readLine()) != null)
        {
            lines.add(line);
        }

        bufferedReader.close();

        return lines.toArray(new String[lines.size()]);
    }

    public static String[] enterRows(Scanner Scanner) {
        System.out.println("Enter the number of rows to be added:");
        String[] rows = new String[scanner.nextInt()];
        scanner.nextLine();
        System.out.println(String.format("Enter %d rows:", rows.length));
        int i = 0;
        while (i < rows.length) {
            String input = scanner.nextLine();
            rows[i] = input;
            i++;
        }
        return rows;
    }

    public static void searchRows(Scanner scanner, String[] rows) {
        System.out.println("Enter search query:");
        String search = scanner.nextLine();
        for (String row : rows) {
            if (row.toUpperCase().contains(search.toUpperCase())) {
                System.out.println(row.trim());
            }
        }
    }

    public static void printAllRows(String[] rows) {
        System.out.println("=== List of rows ===");
        for (String row : rows) {
            System.out.println(row);
        }
    }

    public static void exit() {
        System.out.println("Bye!");
        exit = true;
    }

    public static void menu() {
        String menu = String.join("\n",
                "=== Menu ===",
                "1. Search for matching rows",
                "2. Print all rows",
                "0. Exit"
        );
        System.out.println(menu);

    }
}