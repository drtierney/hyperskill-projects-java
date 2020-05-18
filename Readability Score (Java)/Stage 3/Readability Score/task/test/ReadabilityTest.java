import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.TestCase;
import readability.Main;

import java.util.List;

import static java.lang.Math.abs;


class TestClue {

    int words;
    int sentences;
    int characters;

    double score;
    String age;

    TestClue(int words, int chars, int sentences, double score, String age) {
        this.words = words;
        this.sentences = sentences;
        this.characters = chars;
        this.score = score;
        this.age = age;
    }
}


public class ReadabilityTest extends StageTest<TestClue> {

    public ReadabilityTest() {
        super(Main.class);
    }

    @Override
    public List<TestCase<TestClue>> generate() {
        return List.of(
            new TestCase<TestClue>().setAttach(new TestClue(
                108,
                580,
                6,
                12.86,
                "18-24"))
                .addFile("input.txt",
                "Readability is " +
                    "the ease with which a reader can " +
                    "understand a written text. In natural " +
                    "language, the readability of text depends " +
                    "on its content and its presentation. " +
                    "Researchers have used various factors " +
                    "to measure readability. Readability is " +
                    "more than simply legibility, which is a " +
                    "measure of how easily a reader can distinguish " +
                    "individual letters or characters from each other. " +
                    "Higher readability eases reading effort and speed " +
                    "for any reader, but it is especially important for " +
                    "those who do not have high reading comprehension. " +
                    "In readers with poor reading comprehension, raising " +
                    "the readability level of a text from mediocre to good " +
                    "can make the difference between success and failure")
                .addArguments("input.txt"),

            new TestCase<TestClue>().setAttach(new TestClue(
                100,
                476,
                10,
                5.98,
                "11-12"))
                .addFile("in.txt",
                "This is the page of the Simple English Wikipedia. " +
                    "A place where people work together to write encyclopedias " +
                    "in different languages. That includes children and adults " +
                    "who are learning English. There are 142,262 articles on the " +
                    "Simple English Wikipedia. All of the pages are free to use. " +
                    "They have all been published under both the Creative Commons" +
                    " License 3 and the GNU Free Documentation License. " +
                    "You can help here! You may change these pages and make new " +
                    "pages. Read the help pages and other good pages to learn " +
                    "how to write pages here. You may ask questions at Simple talk.")
                .addArguments("in.txt"),

            new TestCase<TestClue>().setAttach(new TestClue(
                180,
                982,
                13,
                11.19,
                "17-18"))
                .addFile("in.txt",
                "Gothic architecture are building designs, " +
                    "as first pioneered in Western Europe in the Middle Ages. " +
                    "It began in France in the 12th century. The Gothic style " +
                    "grew out of Romanesque architecture. It lasted until the " +
                    "16th century. By that time the Renaissance style of " +
                    "architecture had become popular. The important features " +
                    "of Gothic architecture are the pointed arch, the ribbed " +
                    "vault, the flying buttress, and stained glass windows " +
                    "which are explained below. Gothic architecture is best " +
                    "known as the style of many " +
                    "of the great cathedrals, abbeys and churches of Europe. " +
                    "It is also the architecture of many castles, palaces, " +
                    "town halls, universities, and also some houses. " +
                    "Many church buildings still remain from this period. " +
                    "Even the smallest Gothic churches are often very beautiful, " +
                    "while many of the larger churches and cathedrals are " +
                    "thought to be priceless works of art. Many are listed " +
                    "with the United Nations Educational, Scientific and " +
                    "Cultural Organization (UNESCO) as World Heritage Sites. " +
                    "In the 19th century, the Gothic style became popular " +
                    "again, particularly for building churches and universities. " +
                    "This style is called Gothic Revival architecture.")
                .addArguments("in.txt")
        );
    }

    @Override
    public CheckResult check(String reply, TestClue clue) {

        boolean foundWords = false;
        boolean foundSentences = false;
        boolean foundChars = false;
        boolean foundScore = false;
        boolean foundAge = false;

        for (Object lineObj : reply.lines().toArray()) {
            String line = (String) lineObj;
            line = line.toLowerCase();
            if (line.contains("words:")) {
                foundWords = true;
                if (!line.contains(String.valueOf(clue.words))) {
                    return new CheckResult(false, "Wrong number of words");
                }
            }
            if (line.contains("sentences:")) {
                foundSentences = true;
                if (!line.contains(String.valueOf(clue.sentences))) {
                    return new CheckResult(false, "Wrong number of sentences");
                }
            }
            if (line.contains("characters:")) {
                foundChars = true;
                if (!line.contains(String.valueOf(clue.characters))) {
                    return new CheckResult(false, "Wrong number of characters");
                }
            }
            if (line.contains("score is:")) {
                foundScore = true;
                double actualScore = Double.parseDouble(line.split(":")[1]);
                if (abs(actualScore - clue.score) > 0.2) {
                    return new CheckResult(false, "Wrong score");
                }
            }
            if (line.contains("year olds")) {
                foundAge = true;
                if (!line.contains(clue.age)) {
                    return new CheckResult(false, "Wrong age");
                }
            }
        }

        if (!foundWords) {
            return new CheckResult(false, "There is no words amount");
        }

        if (!foundSentences) {
            return new CheckResult(false, "There is no sentences amount");
        }

        if (!foundChars) {
            return new CheckResult(false, "There is no characters amount");
        }

        if (!foundScore) {
            return new CheckResult(false, "There is no score in output");
        }

        if (!foundAge) {
            return new CheckResult(false, "There is no age in output");
        }

        return CheckResult.correct();
    }
}
