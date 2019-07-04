import bot.SimpleBot;
import org.hyperskill.hstest.v5.stage.BaseStageTest;
import org.hyperskill.hstest.v5.testcase.CheckResult;
import org.hyperskill.hstest.v5.testcase.TestCase;

import java.util.List;


class Clue {
    int age;
    String name;
    int count;

    Clue(String name, int age, int count) {
        this.age = age;
        this.name = name;
        this.count = count;
    }
}


public class SimpleBotTest extends BaseStageTest<Clue> {

    public SimpleBotTest() {
        super(SimpleBot.class);
    }

    @Override
    public List<TestCase<Clue>> generate() {
        return List.of(
            new TestCase<Clue>()
                .setInput("Marry\n1 0 5\n10")
                .setAttach(new Clue("Marry", 40, 10))
        );
    }

    @Override
    public CheckResult check(String reply, Clue clue) {

        String[] lines = reply.trim().split("\n");

        int length = 9 + clue.count + 1;

        if (lines.length != length) {
            return CheckResult.FALSE(
                "You should output " + length + " lines " +
                "(for the count number " + clue.count +").\n" +
                "Lines found: " + lines.length + "\n" +
                "Your output:\n" +
                reply
            );
        }

        String lineWithName = lines[3].toLowerCase();
        String name = clue.name.toLowerCase();

        if (!lineWithName.contains(name)) {
            return CheckResult.FALSE(
                "The name was " + clue.name + "\n" +
                "But the 4-th line was:\n" +
                "\"" + lines[3] + "\"\n\n" +
                "4-th line should contain a name of the user"
            );
        }

        String lineWithAge = lines[6].toLowerCase();
        String age = Integer.toString(clue.age);

        if (!lineWithAge.contains(age)) {
            return CheckResult.FALSE(
                "Can't find a correct age " +
                "in the last line of output! " +
                "Maybe you calculated the age wrong?\n\n" +
                "Your last line: \n" + "\"" + lines[6] + "\""
            );
        }

        for (int i = 0; i < clue.count + 1; i++) {
            String numLine = lines[i + 8];
            String actualNum = i + "!";

            if (!numLine.equals(actualNum)) {
                return CheckResult.FALSE(
                    "Expected " + (i+8) + "-th line: \n" +
                    "\"" + actualNum + "\"\n" +
                    "Your "+ (i+8) + "-th line: \n" +
                    "\"" + numLine + "\""
                );
            }
        }

        return CheckResult.TRUE;
    }

}
