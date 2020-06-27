package converter;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int sourceBase = Integer.parseInt(scanner.nextLine());
        String number   = scanner.nextLine();
        int targetBase = Integer.parseInt(scanner.nextLine());

        String[] numberSplit = number.split("\\.");

        if (numberSplit.length == 1 || sourceBase == 1){
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
}
