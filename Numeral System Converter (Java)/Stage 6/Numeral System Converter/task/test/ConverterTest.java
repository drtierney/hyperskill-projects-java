import converter.Main;
import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testcase.TestCase;

import java.util.List;

class Clue {
    final String input;
    String answer;
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
            testToAnswer("a b\n\n\n", null, true),
            testToAnswer("11\nabc\n\n", null, true),
            testToAnswer("11\n1\ndas\n", null, true),
            testToAnswer("0\n1\ndas\n", null, true),
            testToAnswer("37\n1\ndas\n", null, true),
            testToAnswer("36\n1\n0\n", null, true),
            testToAnswer("36\n1\n37\n", null, true),

            /* Tests without a hint: */
            testToAnswer("c\n\n\n", null, false),
            testToAnswer("12\nbc\n\n", null, false),
            testToAnswer("12\n1\nhf\n", null, false),
            testToAnswer("0\n2\nhf\n", null, false),
            testToAnswer("37\n2\nhf\n", null, false),
            testToAnswer("14\n2\n-1\n", null, false),
            testToAnswer("14\n2\n38\n", null, false),

            /* Tests from previous stage (with a hint): */
            testToAnswer("10\n0.234\n7", "0.14315", true),
            testToAnswer("10\n10.234\n7", "13.14315", true),
            testToAnswer("6\n2.555\n1", "11", true),
            testToAnswer("35\naf.xy\n17", "148.g88a8", true),
            testToAnswer("10\n11\n2", "1011", true),
            testToAnswer("16\naaaaa.0\n24", "22df2.00000", true),
            testToAnswer("16\n0.cdefb\n24", "0.j78da", true),
            testToAnswer("16\naaaaa.cdefb\n24", "22df2.j78da", true),

            /* Tests from previous stage (without a hint): */
            testToAnswer("10\n0.2340\n7", "0.14315", false),
            testToAnswer("10\n10.2340\n7", "13.14315", false),
            testToAnswer("6\n2.5550\n1", "11", false),
            testToAnswer("35\naf.xy0\n17", "148.g88a8", false),
            testToAnswer("10\n12\n2", "1100", false),
            testToAnswer("16\naaaaa.00\n24", "22df2.00000", false),
            testToAnswer("16\n0.cdefb0\n24", "0.j78da", false),
            testToAnswer("16\naaaaa.cdefb0\n24", "22df2.j78da", false),

            /* Tests from previous stage (with a hint): */
            testToAnswer("10\n11\n2\n", "1011", true),
            testToAnswer("1\n11111\n10\n", "5", true),
            testToAnswer("10\n1000\n36\n", "rs", true),
            testToAnswer("21\n4242\n6\n", "451552", true),
            testToAnswer("7\n12\n11\n", "9", true),
            testToAnswer("5\n300\n10\n", "75", true),
            testToAnswer("1\n11111\n5\n", "10", true),
            testToAnswer("10\n4\n1\n", "1111", true),

            /* Tests from previous stage (without a hint): */
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

        String answer = lines[lines.length - 1];

        if (clue.answer == null) {
            if (answer.toLowerCase().contains("error")) {
                return new CheckResult(true);
            } else if (clue.provideAnswer) {
                return new CheckResult(
                    false,
                    "Your program doesn't say about an error.\n" +
                        "This is a sample test so we give you a hint.\n" +
                        "Input: " + clue.input + "\n" +
                        "Your answer: " + answer
                );
            } else {
                return new CheckResult(
                    false,
                    "Your program doesn't say about an error."
                );
            }
        }

        answer = answer.replaceAll("[^\\p{Graph}]", "");
        clue.answer = clue.answer.replaceAll("[^\\p{Graph}]", "");

        answer = removeEndZeros(answer);
        clue.answer = removeEndZeros(clue.answer);

        if (!answer.equals(clue.answer)) {
            if (clue.provideAnswer) {
                return new CheckResult(
                    false,
                    "Your program gives a wrong answer when there is no error in the input.\n" +
                        "This is a sample test so we give you a hint.\n" +
                        "Input: " + clue.input + "\n" +
                        "Your answer: " + answer + "\n" +
                        "Correct answer: " + clue.answer
                );
            } else {
                return new CheckResult(
                    false,
                    "Your program gives a wrong answer when there is no error in the input."
                );
            }
        }

        return new CheckResult(true);
    }

    private String removeEndZeros(String number) {
        if (!number.contains(".")) {
            return number;
        }
        while (number.endsWith("0")) {
            number = number.substring(0, number.length() - 1);
        }
        if (number.endsWith(".")) {
            number = number.substring(0, number.length() - 1);
        }
        return number;
    }
}
