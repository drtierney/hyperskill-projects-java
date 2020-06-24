import converter.Main;
import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testcase.TestCase;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class Clue {
    String feedback;
    Clue(String feedback) {
        this.feedback = feedback;
    }
}

public class ConverterTest extends StageTest<Clue> {

    public ConverterTest() {
        super(Main.class);
    }

    static final String BINARY_PREFIX = "0b";

    @Override
    public List<TestCase<Clue>> generate() {
        return List.of(
            new TestCase<>()
        );
    }

    @Override
    public CheckResult check(String reply, Clue clue) {
        final String[] lines = reply
            .lines()
            .filter(line -> !line.isEmpty())
            .toArray(String[]::new);

        if (lines.length != 1) {
            return new CheckResult(
                false,
                String.format(
                    "Your program doesn't print exactly one line. A number of lines found: %d.",
                    lines.length
                )
            );
        }

        final Set<String> words = Arrays
            .stream(lines[0].split(" "))
            .filter(word -> !word.isEmpty())
            .collect(Collectors.toSet());

        final String[] binaries = words
            .stream()
            .filter(word -> word.startsWith(BINARY_PREFIX))
            .map(word -> word.split(BINARY_PREFIX)[1])
            .filter(word -> word.chars().mapToObj(i -> (char) i).allMatch(c -> c == '1' || c == '0'))
            .toArray(String[]::new);

        if (binaries.length != 1) {
            return new CheckResult(
                false,
                String.format(
                    "Your responsesFromClient doesn't contain exactly one binary. Binaries have been found: %s.",
                    Arrays.toString(binaries)
                )
            );
        }

        final String[] numbers = words
            .stream()
            .filter(word -> word.chars().mapToObj(i -> (char) i).allMatch(Character::isDigit))
            .toArray(String[]::new);

        if (numbers.length != 1) {
            return new CheckResult(
                false,
                String.format(
                    "Your responsesFromClient doesn't contain exactly one number. Numbers have been found: %s.",
                    Arrays.toString(binaries)
                )
            );
        }


        final BigInteger binary = new BigInteger(binaries[0], 2);
        final BigInteger number = new BigInteger(numbers[0], 10);

        if (!binary.equals(number)) {
            return new CheckResult(
                false,
                String.format("%s%s != %s", BINARY_PREFIX, binaries[0], numbers[0])
            );
        }

        return new CheckResult(true);
    }
}
