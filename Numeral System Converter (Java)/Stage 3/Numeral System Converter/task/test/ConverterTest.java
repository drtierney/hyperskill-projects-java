import converter.Main;
import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testcase.TestCase;

import java.util.ArrayList;
import java.util.List;

class Clue {
    // you can store here any variables you need for test

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

    static String prefix(final int base) {
        if (base == 2) {
            return "0b";
        } else if (base == 8) {
            return "0";
        } else {
            return "0x";
        }
    }

    public static List<TestCase<Clue>> iToTest(final int i, final boolean provideAnswer) {
        final List<TestCase<Clue>> tests = new ArrayList<>();

        for (final int base : new int[]{16, 8, 2}) {
            final String answer = prefix(base) + Integer.toString(i, base);
            final String input = i + "\n" + base;

            tests.add(new TestCase<Clue>()
                .setAttach(new Clue(input, answer, provideAnswer))
                .setInput(input)
            );
        }

        return tests;
    }

    @Override
    public List<TestCase<Clue>>  generate() {
        final List<TestCase<Clue>> tests = new ArrayList<>();

        /* Tests with a hint: */
        tests.addAll(iToTest(11, true));
        tests.addAll(iToTest(8, true));
        tests.addAll(iToTest(0, true));

        /* Tests without a hint: */
        for (int i = 101; i <= 104; ++i) {
            tests.addAll(iToTest(i, true));
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
