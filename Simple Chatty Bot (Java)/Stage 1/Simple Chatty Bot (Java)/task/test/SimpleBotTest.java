import bot.SimpleBot;
import org.hyperskill.hstest.v5.testcase.CheckResult;
import org.hyperskill.hstest.v5.stage.BaseStageTest;
import org.hyperskill.hstest.v5.testcase.TestCase;

import java.util.List;


public class SimpleBotTest extends BaseStageTest {

    public SimpleBotTest() {
        super(SimpleBot.class);
    }

    @Override
    public List<TestCase> generate() {
        return List.of(
            new TestCase<>()
        );
    }

    @Override
    public CheckResult check(String reply, Object clue) {

        String[] lines = reply.trim().split("\n");

        if (lines.length != 2) {
            return CheckResult.FALSE(
                "You should output exactly 2 lines!\n" +
                "Lines found: " + lines.length
            );
        }

        String secondLine = lines[1];

        if (!secondLine.matches(".*\\d.*")) {
            return CheckResult.FALSE(
                "The second line should contain a year!\n" +
                "Your second line: \"" + secondLine + "\""
            );
        }

        return CheckResult.TRUE;
    }
}
