package converter;

import java.util.Scanner;

import static java.lang.System.*;

public class Main {
    public static void main(String[] args) {
            String sourceBaseString = "";
            String numberString = "";
            String targetBaseString = "";

            Scanner scanner = new Scanner(System.in);
            if(scanner.hasNextLine()) {
                sourceBaseString = scanner.nextLine();
                if (testBase(sourceBaseString)) {
                    errorExit();
                }
            } else {
                errorExit();
                }

            if(scanner.hasNextLine()) {
                numberString = scanner.nextLine();
                if (testExists(numberString)) {
                    errorExit();
                }
            } else {
                errorExit();
                }

            if(scanner.hasNextLine()) {
            targetBaseString = scanner.nextLine();
            if (testBase(targetBaseString)) {
                errorExit();
            }
        } else {
                errorExit();
            }

            String[] numberSplit = numberString.split("\\.");
            int sourceBase = Integer.parseInt(sourceBaseString);
            int targetBase = Integer.parseInt(targetBaseString);

            if (numberSplit.length == 1 || sourceBase == 1) {
                System.out.println(convertIntegerPart(numberSplit[0], sourceBase, targetBase));
            } else {
                String integerPart = convertIntegerPart(numberSplit[0], sourceBase, targetBase);
                String fractionalPart = convertFractionalPart(numberSplit[1], sourceBase, targetBase);
                System.out.printf("%s.%s", integerPart, fractionalPart);
            }
    }

    private static String convertIntegerPart(String sourceInteger, int sourceBase, int targetBase) {
        int number;
        if (sourceBase != 10) {
            if (sourceBase == 1) {
                number = sourceInteger.length();
            } else {
                number = Integer.parseInt(sourceInteger, sourceBase);
            }
        } else {
            number = Integer.parseInt(sourceInteger);
        }

        if (targetBase == 1) {
            return "1".repeat(Math.max(0, number));
        }
        return Integer.toString(number, targetBase);
    }

    private static String convertFractionalPart(String sourceFractional, int sourceBase, int targetBase) {
        double decimalValue = 0.0;
        if (sourceBase == 10) {
            decimalValue = Double.parseDouble("0." + sourceFractional);
        } else {
            char c;
            for (int i = 0; i < sourceFractional.length(); i++) {
                c = sourceFractional.charAt(i);
                decimalValue += Character.digit(c, sourceBase) / Math.pow(sourceBase, i + 1);
            }
        }

        StringBuilder result = new StringBuilder();
        int decimal;
        for (int i = 0; i < 5; i++) {
            double aux = decimalValue * targetBase;
            decimal = (int) aux;
            result.append(Long.toString(decimal, targetBase));
            decimalValue = aux - decimal;
        }
        return result.toString();
    }

    private static boolean testBase(String base){
        if(base.matches("[0-9]+")){
            int n = Integer.parseInt(base);
            return n <= 0 || n > Character.MAX_RADIX;
        }
        return true;
    }

    private static boolean testExists(String str){
        return str == null || str.length() == 0;
    }

    private static void errorExit() {
        System.out.println("error");
        exit(0);
    }
}
