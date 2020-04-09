import encryptdecrypt.Main;
import org.hyperskill.hstest.v6.stage.BaseStageTest;
import org.hyperskill.hstest.v6.testcase.CheckResult;
import org.hyperskill.hstest.v6.testcase.TestCase;

import java.util.List;


class Attach {
    String original;
    int shift;
    String output;

    public Attach(String original, int shift, String output) {
        this.original = original;
        this.shift = shift;
        this.output = output;
    }
}

public class EncryptDecryptTest extends BaseStageTest<Attach> {
    public EncryptDecryptTest() {
        super(Main.class);
    }

    @Override
    public List<TestCase<Attach>> generate() {
        return List.of(
            new TestCase<Attach>()
                .setInput("welcome to hyperskill\n5")
                .setAttach(new Attach(
                    "welcome to hyperskill",
                    5,
                    "bjqhtrj yt mdujwxpnqq")),

            new TestCase<Attach>()
                .setInput("treasure\n10")
                .setAttach(new Attach(
                    "treasure",
                    10,
                    "dbokcebo"
                )),

            new TestCase<Attach>()
                .setInput("qdvdqvrxqwxrxwpvrxspvxiqgdiqarairpbiqqid\n12")
                .setAttach(new Attach(
                    "qdvdqvrxqwxrxwpvrxspvxiqgdiqarairpbiqqid",
                    12,
                    "cphpchdjcijdjibhdjebhjucspucmdmudbnuccup"
                )),

            new TestCase<Attach>()
                .setInput("y\n10")
                .setAttach(new Attach(
                    "y",
                    10,
                    "i"
                ))
        );
    }

    @Override
    public CheckResult check(String reply, Attach attach) {
        String clue = attach.output;
        reply = reply.trim();
        clue = clue.trim();
        boolean isCorrect = reply.equals(clue);
        if (isCorrect) {
            return CheckResult.TRUE;
        }
        if (reply.length() != clue.length()) {
            return CheckResult.FALSE(
                "You should output a line with length " +
                    clue.length() + ". " + "You output a " +
                    "line with length " + reply.length()
            );
        }
        for (int i = 0; i < clue.length(); i++) {
            if (reply.charAt(i) != clue.charAt(i)) {
                return CheckResult.FALSE(
                    "Your " + (i+1) + "-th character '" + reply.charAt(i) + "'" +
                        " is incorrect. \n" +
                    "The right one is '" + clue.charAt(i) + "'. \n" +
                        "Key is " + attach.shift
                );
            }
        }
        return CheckResult.TRUE;
    }
}
