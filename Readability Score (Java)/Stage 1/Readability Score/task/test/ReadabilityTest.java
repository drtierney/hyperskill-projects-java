import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testcase.TestCase;
import readability.Main;

import java.util.List;


public class ReadabilityTest extends StageTest<String> {

    public ReadabilityTest() {
        super(Main.class);
    }

    @Override
    public List<TestCase<String>> generate() {

        List<TestCase<String>> tests = List.of(
            new TestCase<String>()
                .setInput("This text is simple to read!"),

            new TestCase<String>()
                .setInput("This text is hard to read. " +
                    "It contains a lot of sentences as well as a lot of words in each sentence."),

            new TestCase<String>()
                .setInput("1".repeat(99)),

            new TestCase<String>()
                .setInput(" ".repeat(100)),

            new TestCase<String>()
                .setInput("q".repeat(101))
        );

        for (TestCase<String> test : tests) {
            test.setAttach(test.getInput());
        }

        return tests;
    }

    @Override
    public CheckResult check(String reply, String clue) {
        String solution = solve(clue);
        boolean isSuccess = reply.strip().equals(solution.strip());
        return new CheckResult(isSuccess);
    }

    private String solve(String input) {
        return input.length() > 100 ? "HARD" : "EASY";
    }
}
