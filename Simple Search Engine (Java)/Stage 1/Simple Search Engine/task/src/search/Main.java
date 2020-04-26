package search;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int counter = 0;
        Scanner scanner = new Scanner(System.in);
        String[] words = scanner.nextLine().split("\\W+");
        String search = scanner.nextLine();
        for (int i = 0; i < words.length; i++) {
            if (search.equals(words[i])) {
                counter = i + 1;
                break;
            }
        }
        System.out.println(counter > 0 ? counter : "Not found");
        }
    }