import encryptdecrypt.Main;
import org.hyperskill.hstest.v6.stage.BaseStageTest;
import org.hyperskill.hstest.v6.testcase.PredefinedIOTestCase;

import java.util.List;

public class EncryptDecryptTest extends BaseStageTest {
    public EncryptDecryptTest() throws Exception {
        super(Main.class);
    }

    @Override
    public List<PredefinedIOTestCase> generate() {
        return List.of(
            new PredefinedIOTestCase(
                "enc\n" +
                    "Welcome to hyperskill!\n" +
                    "5",
                "\\jqhtrj%yt%m~ujwxpnqq&"),
            new PredefinedIOTestCase(
                "enc\n" +
                    "Hello\n" +
                    "0",
                "Hello"),
            new PredefinedIOTestCase(
                "enc\n" +
                    "012345678\n" +
                    "1",
                "123456789"),
            new PredefinedIOTestCase(
                "dec\n" +
                    "\\jqhtrj%yt%m~ujwxpnqq&\n" +
                    "5",
                "Welcome to hyperskill!"),
            new PredefinedIOTestCase(
                "dec\n" +
                    "Hello\n" +
                    "0",
                "Hello"),
            new PredefinedIOTestCase(
                "dec\n" +
                    "222233334444\n" +
                    "1",
                "111122223333")
        );
    }
}
