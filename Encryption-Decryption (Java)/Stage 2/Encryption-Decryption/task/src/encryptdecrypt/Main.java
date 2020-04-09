package encryptdecrypt;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String str = scanner.nextLine();

        int shift = scanner.nextInt();
        StringBuilder shiftedString = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isLetter(c)) {
                c = (char) (str.charAt(i) + shift);
                if ((Character.isLowerCase(str.charAt(i)) && (c > 'z'))
                        || (Character.isUpperCase(str.charAt(i)) && (c > 'Z'))) {
                    c = (char) (str.charAt(i) - (26 - shift));
                }
            }
            shiftedString.append(c);
        }

        System.out.println(shiftedString);
    }
}
