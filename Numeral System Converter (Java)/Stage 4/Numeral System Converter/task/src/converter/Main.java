package converter;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int sourceBase = Integer.parseInt(scanner.nextLine());
        String number   = scanner.nextLine();
        int targetBase = Integer.parseInt(scanner.nextLine());

        if (sourceBase == 1) {
            number = String.valueOf(number.length());
        } else {
            number = String.valueOf(Integer.parseInt(number, sourceBase));
        }

        if (targetBase == 1) {
            System.out.println("1".repeat(Integer.parseInt(number)));
        } else {
            System.out.println(Integer.toString(Integer.parseInt(number), targetBase));
        }
    }
}
