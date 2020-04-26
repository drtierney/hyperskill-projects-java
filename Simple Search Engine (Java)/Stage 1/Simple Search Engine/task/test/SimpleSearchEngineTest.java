import org.hyperskill.hstest.v6.stage.BaseStageTest;
import org.hyperskill.hstest.v6.testcase.CheckResult;
import org.hyperskill.hstest.v6.testcase.TestCase;
import search.Main;

import java.util.Arrays;
import java.util.List;

class TestClue {
    boolean correctness;
    int position;

    TestClue(boolean correct, int pos) {
        correctness = correct;
        position = pos;
    }
}

public class SimpleSearchEngineTest extends BaseStageTest<TestClue> {

    public SimpleSearchEngineTest() throws Exception {
        super(Main.class);
    }

    @Override
    public List<TestCase<TestClue>> generate() {
        return Arrays.asList(
            new TestCase<TestClue>()
                .setAttach(new TestClue(true, 2))
                .setInput("hello my name is alex\nmy"),

            new TestCase<TestClue>()
                .setAttach(new TestClue(true, 5))
                .setInput("hello my name is alex\nalex"),

            new TestCase<TestClue>()
                .setAttach(new TestClue(true, 1))
                .setInput("what a beautiful place\nwhat"),

            new TestCase<TestClue>()
                .setAttach(new TestClue(true, 1))
                .setInput("hi\nhi"),

            new TestCase<TestClue>()
                .setAttach(new TestClue(false, 0))
                .setInput("hi\nhello"),

            new TestCase<TestClue>()
                .setAttach(new TestClue(false, 0))
                .setInput("twenty one pilots white stripes queen system of a down\nhello"),

            new TestCase<TestClue>()
                .setAttach(new TestClue(true, 9))
                .setInput("twenty one pilots white stripes queen system of a down\na"),

            new TestCase<TestClue>()
                .setAttach(new TestClue(false, 0))
                .setInput("one two three\nfour")
        );
    }

    @Override
    public CheckResult check(String reply, TestClue clue) {
        String[] lines = reply.split("\n");
        String lastLine = lines[lines.length - 1].trim().toLowerCase();

        if (lines.length == 1) {
            boolean correct = clue.correctness;
            int index = clue.position;
            if (correct) {
                return new CheckResult(lastLine.contains(Integer.toString(index)));
            } else {
                return new CheckResult(lastLine.contains("not found"));
            }
        } else {
            return new CheckResult(false, "You should have responsesFromClient only one line instead of " +
                lines.length);
        }
    }
}

