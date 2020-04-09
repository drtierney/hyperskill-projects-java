package encryptdecrypt.crypto;

class ShiftCipher implements IEncoder {
    @Override
    public String encode(String input, int key) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (Character.isLetter(c)) {
                c = (char) (input.charAt(i) + key);
                if ((Character.isLowerCase(input.charAt(i)) && (c > 'z'))
                        || (Character.isUpperCase(input.charAt(i)) && (c > 'Z'))) {
                    c = (char) (input.charAt(i) - (26 - key));
                }
            }
            output.append(c);
        }
        return output.toString();
    }

    @Override
    public String decode(String input, int key) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (Character.isLetter(c)) {
                c = (char) (input.charAt(i) - key);
                if ((Character.isLowerCase(input.charAt(i)) && c < 'a')
                        || (Character.isUpperCase(input.charAt(i)) && c < 'A')) {
                    c = (char) (input.charAt(i) + (26 - key));
                }
            }
            output.append(c);
        }
        return output.toString();
    }
}
