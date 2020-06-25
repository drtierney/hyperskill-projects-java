package converter;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int input = Integer.parseInt(scanner.nextLine());
        int radix = Integer.parseInt(scanner.nextLine());
        String prefix = "";

        switch (radix) {
            case 2:
                prefix = "0b";
                break;
            case 8:
                prefix = "0";
                break;
            case 16:
                prefix = "0x";
                break;
        }
        System.out.printf("%s%s", prefix, Long.toString(input, radix));
    }
}
