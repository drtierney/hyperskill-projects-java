package encryptdecrypt;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String flag = scanner.nextLine();
        String str = scanner.nextLine();
        int shift = scanner.nextInt();

        String result = "";

        switch (flag) {
            case "enc":
                result = encrypt(str, shift);
                break;
            case "dec":
                result = decrypt(str, shift);
                break;
        }
        System.out.println(result);
    }

    public static String encrypt(String str, int shift) {
        StringBuilder shiftedString = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = (char) (str.charAt(i) + shift);
            shiftedString.append(c);
        }

        return shiftedString.toString();
    }

    public static String decrypt(String str, int shift) {
        StringBuilder shiftedString = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            char c = (char) (str.charAt(i) - shift);
            shiftedString.append(c);
        }

        return shiftedString.toString();
    }
}
