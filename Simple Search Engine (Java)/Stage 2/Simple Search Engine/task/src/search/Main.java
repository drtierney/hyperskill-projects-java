package search;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the number of rows to be added:");
        String[] rows = new String[scanner.nextInt()];
        scanner.nextLine();
        System.out.println(String.format("Enter %d rows:", rows.length));
        for (int i = 0; i < rows.length; i++) {
            String input = scanner.nextLine();
            rows[i] = input;
        }

        System.out.println("Enter the number of search queries:");
        int searchCount = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < searchCount; i++) {
            System.out.println("Enter search query:");
            String search = scanner.nextLine();
            for (int j = 0; j < rows.length; j++) {
                if (rows[j].toUpperCase().contains(search.toUpperCase())){
                    System.out.println(rows[j].trim());
                }
            }
        }
    }
}