package converter;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int base = 8;
        int num = scanner.nextInt();
        int lastDigit = num % base;
        System.out.println(lastDigit);

    }
}
