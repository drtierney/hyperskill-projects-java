import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.SimpleTestCase;
import readability.Main;

import java.util.List;


public class ReadabilityTest extends StageTest {

    public ReadabilityTest() {
        super(Main.class);
    }

    @Override
    public List<SimpleTestCase> generate() {
        return List.of(
            new SimpleTestCase("This text is simple to read! " +
                "It has on average less than 10 words per sentence.",
                "EASY"),

            new SimpleTestCase("This text is hard to read. " +
                "It contains a lot of sentences as well as a lot of words in each sentence",
                "HARD"),

            new SimpleTestCase("1 ".repeat(99) + "1. 1. 1.",
                "HARD"),

            new SimpleTestCase("12, 12 13 14 14 14, " +
                "12 21 23 89! 75 12, 134 241 123, 123 123 123, 123 123 " +
                "123? 123, 123 123 123 23 123 213 123 123 123.",
                "HARD"),

            new SimpleTestCase("12, 12 13 14 14 14, 12 21 23 89! " +
                "75 12, 241 123, 123 123 123, 123 123 123? 123, " +
                "123 123 123 23 123 213 123 123 123.",
                "EASY"),

            new SimpleTestCase( "Readability is the ease with which a " +
                "reader can understand a written text. In natural language, the " +
                "readability of text depends on its content and its presentation. " +
                "Researchers have used various factors to measure readability. And that is it!",
                "EASY")
        );
    }
}
