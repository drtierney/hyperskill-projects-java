package encryptdecrypt;

public class Main {
    public static void main(String[] args) {
        String mode = "enc";
        int key = 0;
        String data = "";

        for(int i = 0; i < args.length; i += 2){
            switch (args[i]){
                case "-mode":
                    mode = args[i+1];
                    break;
                case "-key":
                    key = Integer.parseInt(args[i+1]);
                    break;
                case "-data":
                    data = args[i+1];
                    break;
            }
        }

        String result = "";

        switch (mode) {
            case "enc":
                result = encrypt(data, key);
                break;
            case "dec":
                result = decrypt(data, key);
                break;
            default:
                System.out.println("Unknown operation");
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
