package encryptdecrypt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        boolean encode = true;
        String message = "";
        String data = "";
        int shift = 0;
        String inFile = "";
        String inFileData = "";
        String outFile = "";
        String result;


        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-mode":
                    encode = "enc".equals(args[i + 1]);
                    break;
                case "-key":
                    shift = Integer.parseInt(args[i + 1]);
                    break;
                case "-data":
                    data = args[i + 1];
                    break;
                case "-in":
                    inFile = args[i + 1];
                    break;
                case "-out":
                    outFile = args[i + 1];
                    break;
            }

            // Check if inFile exists and parse input
            if (!inFile.isBlank()) {
                try (Scanner scanner = new Scanner(new File(inFile))) {
                    inFileData = scanner.nextLine();
                } catch (FileNotFoundException e) {
                    System.out.println("File not found");
                    break;
                }
            }

            // if -data and -in both exist, prioritise -data
            message = !data.equals("") ? data : inFileData;

        }
        if (encode) {
            result = encode(message, shift);
        } else {
            result = decode(message, shift);
        }

        //Check if -out file has been included, if not go to std out
        if (outFile.isBlank()) {
            System.out.println(result);
        } else {
            try (FileWriter fileWriter = new FileWriter(new File(outFile))) {
                fileWriter.write(result);
                fileWriter.flush();
            } catch (IOException e) {
                System.out.println("Error writing result");
            }
        }
    }

    public static String encode(String str, int shift) {
        StringBuilder result = new StringBuilder();
        for (char c : str.toCharArray()) {
            result.append((char) (c + shift));
        }
        return result.toString();
    }

    public static String decode(String str, int shift) {
        StringBuilder result = new StringBuilder();
        for (char c : str.toCharArray()) {
            result.append((char) (c - shift));
        }
        return result.toString();
    }
}
