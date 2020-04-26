package search;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    static Boolean exit = false;
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        String[] rows = enterRows(scanner);
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

    public static String[] enterRows(Scanner scanner) {
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

    public static void printAllRows(String[] rows){
        System.out.println("=== List of rows ===");
        for (String row : rows){
            System.out.println(row);
        }
    }

    public static void exit() {
        System.out.println("Bye!");
        exit = true;
    }

    public static void menu(){
        String menu = String.join("\n",
                "=== Menu ===",
                "1. Search for matching rows",
                "2. Print all rows",
                "0. Exit"
                );
        System.out.println(menu);

    }
}