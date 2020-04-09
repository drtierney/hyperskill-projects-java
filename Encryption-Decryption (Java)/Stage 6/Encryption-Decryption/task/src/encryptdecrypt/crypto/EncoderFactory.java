package encryptdecrypt.crypto;

public class EncoderFactory {
    public IEncoder create(String alg){
        alg = alg.toUpperCase();

        switch (alg) {
            case "SHIFT": return new ShiftCipher();
            case "UNICODE": return new UnicodeCipher();
            default:
                throw new IllegalStateException("Unexpected value: " + alg);
        }
    }
}
