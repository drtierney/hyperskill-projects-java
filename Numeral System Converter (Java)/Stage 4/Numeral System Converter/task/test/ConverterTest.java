import converter.Main;
import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testcase.TestCase;

import java.util.List;

class Clue {

    final String input;
    final String answer;
    final boolean provideAnswer;

    Clue(final String input, final String answer, final boolean provideAnswer) {
        this.input = input;
        this.answer = answer;
        this.provideAnswer = provideAnswer;
    }

    Clue(final String input, final String answer) {
        this(input, answer, false);
    }

    Clue(final String input) {
        this(input, null, false);
    }
}


public class ConverterTest extends StageTest<Clue> {

    public ConverterTest() {
        super(Main.class);
    }

    static TestCase<Clue> testToAnswer(final String input, final String answer, final boolean provideAnswer) {
        return new TestCase<Clue>()
            .setAttach(new Clue(input, answer, provideAnswer))
            .setInput(input);
    }

    @Override
    public List<TestCase<Clue>> generate() {
        return List.of(
            /* Tests with a hint: */
            testToAnswer("10\n11\n2\n", "1011", true),
            testToAnswer("1\n11111\n10\n", "5", true),
            testToAnswer("10\n1000\n36\n", "rs", true),
            testToAnswer("21\n4242\n6\n", "451552", true),
            testToAnswer("7\n12\n11\n", "9", true),
            testToAnswer("5\n300\n10\n", "75", true),
            testToAnswer("1\n11111\n5\n", "10", true),
            testToAnswer("10\n4\n1\n", "1111", true),

            /* Tests without a hint: */
            testToAnswer("10\n12\n2\n", "1100", false),
            testToAnswer("1\n1111111\n10\n", "7", false),
            testToAnswer("10\n1001\n36\n", "rt", false),
            testToAnswer("21\n4243\n6\n", "451553", false),
            testToAnswer("7\n13\n11\n", "a", false),
            testToAnswer("5\n301\n10\n", "76", false),
            testToAnswer("1\n111111\n5\n", "11", false),
            testToAnswer("10\n5\n1\n", "11111", false)
        );
    }

    @Override
    public CheckResult check(String reply, Clue clue) {
        final String[] lines = reply
            .lines()
            .filter(line -> !line.isEmpty())
            .toArray(String[]::new);

        if (lines.length == 0) {
            return new CheckResult(
                false,
                "Your program doesn't print any line."
            );
        }

        final String answer = lines[lines.length - 1];

        if (!answer.equals(clue.answer)) {
            if (clue.provideAnswer) {
                return new CheckResult(
                    false,
                    "Your answer is wrong.\n" +
                        "This is a sample test so we give you a hint.\n" +
                        "Input: " + clue.input + "\n" +
                        "Your answer: " + answer + "\n" +
                        "Correct answer: " + clue.answer
                );
            } else {
                return new CheckResult(
                    false,
                    "Your answer is wrong."
                );
            }
        }

        return new CheckResult(true);
    }
}
