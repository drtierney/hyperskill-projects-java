package encryptdecrypt.crypto;

public interface IEncoder {
    String encode(String input, int key);
    String decode(String input, int key);
}
