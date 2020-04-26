import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testcase.TestCase;
import tictactoe.Main;

import java.util.List;

public class TicTacToeTest extends StageTest {
    public TicTacToeTest() {
        super(Main.class);
    }

    @Override
    public List<TestCase> generate() {
        return List.of(
            new TestCase()
        );
    }

    @Override
    public CheckResult check(String reply, Object clue) {

        reply = reply.replaceAll("\\s+", "");

        if (reply.length() > 9) {
            return new CheckResult(false,
                "You need to output no more than 9 " +
                    "symbols not counting spaces");
        }

        boolean haveX = false;
        boolean haveO = false;

        for (char c : reply.toCharArray()) {
            if (c != 'X' && c != 'O') {
                return new CheckResult(false,
                    "You need to output X and O " +
                        "symbols only not counting spaces. " +
                        "Found symbol: \'" + c + "\'");
            }
            if (c == 'X') {
                haveX = true;
            }
            if (c == 'O') {
                haveO = true;
            }
        }

        if (!haveX) {
            return CheckResult.wrong(
                "You need to output at least one X"
            );
        }

        if (!haveO) {
            return CheckResult.wrong(
                "You need to output at least one O"
            );
        }

        return CheckResult.correct();
    }
}
