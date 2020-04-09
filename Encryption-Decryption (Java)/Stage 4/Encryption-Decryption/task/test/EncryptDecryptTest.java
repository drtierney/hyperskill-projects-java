import encryptdecrypt.Main;
import org.hyperskill.hstest.v6.stage.BaseStageTest;
import org.hyperskill.hstest.v6.testcase.CheckResult;
import org.hyperskill.hstest.v6.testcase.TestCase;

import java.util.List;

public class EncryptDecryptTest extends BaseStageTest<String> {
    public EncryptDecryptTest() throws Exception {
        super(Main.class);
    }

    @Override
    public List<TestCase<String>> generate() {
        return List.of(
            new TestCase<String>()
                .addArguments(
                    "-mode", "enc",
                    "-key", "5",
                    "-data", "Welcome to hyperskill!"
                )
                .setAttach("\\jqhtrj%yt%m~ujwxpnqq&"),

            new TestCase<String>()
                .addArguments(
                    "-key", "0",
                    "-mode", "enc",
                    "-data", "Hello"
                )
                .setAttach("Hello"),

            new TestCase<String>()
                .addArguments(
                    "-key", "1",
                    "-data", "012345678",
                    "-mode", "enc"
                )
                .setAttach("123456789"),

            new TestCase<String>()
                .addArguments(
                    "-mode", "dec",
                    "-data", "\\jqhtrj%yt%m~ujwxpnqq&",
                    "-key", "5"
                )
                .setAttach("Welcome to hyperskill!"),

            new TestCase<String>()
                .addArguments(
                    "-mode", "dec",
                    "-key", "0",
                    "-data", "Hi"
                )
                .setAttach("Hi"),

            new TestCase<String>()
                .addArguments(
                    "-mode", "dec",
                    "-key", "1",
                    "-data", "222233334444"
                )
                .setAttach("111122223333")
        );
    }

    @Override
    public CheckResult check(String reply, String clue) {
        return new CheckResult(reply.trim().equals(clue.trim()));
    }
}
