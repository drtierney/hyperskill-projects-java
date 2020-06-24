import converter.Main;
import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testcase.TestCase;

import java.util.ArrayList;
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

    public static TestCase<Clue> iToTest(final int i, final boolean provideAnswer) {
        final String octal = Integer.toString(i, 8);
        final String octalLast = octal.substring(octal.length() - 1);
        final String input = Integer.toString(i);

        return new TestCase<Clue>()
            .setAttach(new Clue(input, octalLast, provideAnswer))
            .setInput(input);
    }

    @Override
    public List<TestCase<Clue>> generate() {
        final List<TestCase<Clue>> tests = new ArrayList<>();

        /* Tests with a hint: */
        for (int i = 0; i <= 10; ++i) {
            tests.add(iToTest(i, true));
        }

        /* Tests without a hint: */
        for (int i = 2340; i <= 2350; ++i) {
            tests.add(iToTest(i, false));
        }

        return tests;
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
