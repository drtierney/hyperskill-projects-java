package tictactoe;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().replace("\"", "");
        char[] cells = input.toCharArray();
        System.out.println("---------");
        System.out.println("|" + " " + cells[0] + " " + cells[1] + " " + cells[2] + " " + "|");
        System.out.println("|" + " " + cells[3] + " " + cells[4] + " " + cells[5] + " " + "|");
        System.out.println("|" + " " + cells[6] + " " + cells[7] + " " + cells[8] + " " + "|");
        System.out.println("---------");
    }
}