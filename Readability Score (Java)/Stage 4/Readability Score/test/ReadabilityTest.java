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
    int syllables;
    int polysyllables;

    double ari;
    double fkri;
    double smog;
    double cli;

    TestClue(int words,
             int chars,
             int sentences,
             int syllables,
             int polysyllables,
             double ari,
             double fkri,
             double smog,
             double cli) {
        this.words = words;
        this.sentences = sentences;
        this.characters = chars;
        this.syllables = syllables;
        this.polysyllables = polysyllables;
        this.ari = ari;
        this.fkri = fkri;
        this.smog = smog;
        this.cli = cli;
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
                        196,
                        20,
                        12.86,
                        12.84,
                        13.56,
                        14.13))
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
                        .addArguments("input.txt")
                        .setInput("all"),

                new TestCase<TestClue>().setAttach(new TestClue(
                        137,
                        687,
                        14,
                        210,
                        17,
                        7.08,
                        6.31,
                        9.42,
                        10.66))
                        .addFile("in.txt",
                                "This is the front page of the Simple English " +
                                        "Wikipedia. Wikipedias are places where people work " +
                                        "together to write encyclopedias in different languages. " +
                                        "We use Simple English words and grammar here. The Simple " +
                                        "English Wikipedia is for everyone! That includes children " +
                                        "and adults who are learning English. There are 142,262 " +
                                        "articles on the Simple English Wikipedia. All of the pages " +
                                        "are free to use. They have all been published under both " +
                                        "the Creative Commons License " +
                                        "and the GNU Free Documentation License. You can help here! " +
                                        "You may change these pages and make new pages. Read the help " +
                                        "pages and other good pages to learn how to write pages here. " +
                                        "If you need help, you may ask questions at Simple talk. Use " +
                                        "Basic English vocabulary and shorter sentences. This allows " +
                                        "people to understand normally complex terms or phrases.")
                        .addArguments("in.txt")
                        .setInput("all"),

                new TestCase<TestClue>().setAttach(new TestClue(
                        180,
                        982,
                        13,
                        317,
                        34,
                        11.19,
                        10.59,
                        12.37,
                        14.14))
                        .addFile("in.txt",
                                "Gothic architecture are building designs, " +
                                        "as first pioneered in Western Europe in the Middle Ages. " +
                                        "It began in France in the 12th century. The Gothic style " +
                                        "grew out of Romanesque architecture. It lasted until the " +
                                        "16th century. By that time the Renaissance style of " +
                                        "architecture had become popular. The important features " +
                                        "of Gothic architecture are the pointed arch, the ribbed " +
                                        "vault, the flying buttress, and stained glass windows " +
                                        "which are explained below. " +
                                        "Gothic architecture is best known as the style of many " +
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
                        .setInput("all")
        );
    }

    @Override
    public CheckResult check(String reply, TestClue clue) {

        boolean foundWords = false;
        boolean foundSentences = false;
        boolean foundChars = false;
        boolean foundSyllables = false;
        boolean foundPolysyllables = false;

        boolean foundARI = false;
        boolean foundFKT = false;
        boolean foundSMG = false;
        boolean foundCLI = false;

        for (Object lineObj : reply.lines().toArray()) {
            String initialLine = (String) lineObj;
            String line = initialLine.toLowerCase();
            if (line.contains("words:")) {
                foundWords = true;
                int words = (int) Double.parseDouble(line.split(":")[1].strip());
                if (abs(words - clue.words) > 5) {
                    return new CheckResult(false,
                            "Wrong number of words. " +
                                    "Should be " + clue.words + ", but found " + words);
                }
            }
            if (line.contains("sentences:")) {
                foundSentences = true;
                int sentences = (int) Double.parseDouble(line.split(":")[1].strip());
                if (abs(sentences - clue.sentences) > 1) {
                    return new CheckResult(false,
                            "Wrong number of sentences. " +
                                    "Should be " + clue.sentences + ", but found " + sentences);
                }
            }
            if (line.contains("characters:")) {
                foundChars = true;
                int characters = (int) Double.parseDouble(line.split(":")[1].strip());
                if (abs(characters - clue.characters) > 10) {
                    return new CheckResult(false,
                            "Wrong number of characters. " +
                                    "Should be " + clue.characters + ", but found " + characters);
                }
            }
            if (line.contains("polysyllables:")) {
                foundPolysyllables = true;
                int polysyllables = (int) Double.parseDouble(line.split(":")[1].strip());
                if (abs(polysyllables - clue.polysyllables) > 5) {
                    return new CheckResult(false,
                            "Wrong number of polysyllables. " +
                                    "Should be " + clue.polysyllables + ", but found " + polysyllables);
                }
            } else if (line.contains("syllables:")) {
                foundSyllables = true;
                int syllables = (int) Double.parseDouble(line.split(":")[1].strip());
                if (abs(syllables - clue.syllables) > 20) {
                    return new CheckResult(false,
                            "Wrong number of syllables. " +
                                    "Should be " + clue.syllables + ", but found " + syllables);
                }
            }

            if (line.startsWith("automated readability index")) {
                foundARI = true;
                int rounded = (int) clue.ari;
                String actual = Integer.toString(rounded);
                String before = Integer.toString(rounded - 1);
                String after = Integer.toString(rounded + 1);
                if (!(line.contains(actual)
                        || line.contains(before)
                        || line.contains(after))) {
                    return new CheckResult(false,
                            "Wrong Automated Readability Index score. " +
                                    "Should be around " + clue.ari + ", your output:\n" + initialLine);
                }
                if (!line.contains("year olds")) {
                    return new CheckResult(false,
                            "No age in Automated Readability Index");
                }
            }

            if (line.startsWith("flesch–kincaid")) {
                foundFKT = true;
                int rounded = (int) clue.fkri;
                String actual = Integer.toString(rounded);
                String before = Integer.toString(rounded - 1);
                String after = Integer.toString(rounded + 1);
                if (!(line.contains(actual)
                        || line.contains(before)
                        || line.contains(after))) {
                    return new CheckResult(false,
                            "Wrong Flesch–Kincaid score. " +
                                    "Should be around " + clue.fkri + ", your output:\n" + initialLine);
                }
                if (!line.contains("year olds")) {
                    return new CheckResult(false,
                            "No age in Flesch–Kincaid");
                }
            }

            if (line.startsWith("simple measure of gobbledygook")) {
                foundSMG = true;
                int rounded = (int) clue.smog;
                String actual = Integer.toString(rounded);
                String before = Integer.toString(rounded - 1);
                String after = Integer.toString(rounded + 1);
                if (!(line.contains(actual)
                        || line.contains(before)
                        || line.contains(after))) {
                    return new CheckResult(false,
                            "Wrong Simple Measure of Gobbledygook score. " +
                                    "Should be around " + clue.smog + ", your output:\n" + initialLine);
                }
                if (!line.contains("year olds")) {
                    return new CheckResult(false,
                            "No age in Simple Measure of Gobbledygook");
                }
            }

            if (line.startsWith("coleman–liau")) {
                foundCLI = true;
                int rounded = (int) clue.cli;
                String actual = Integer.toString(rounded);
                String before = Integer.toString(rounded - 1);
                String after = Integer.toString(rounded + 1);
                if (!(line.contains(actual)
                        || line.contains(before)
                        || line.contains(after))) {
                    return new CheckResult(false,
                            "Wrong Coleman–Liau score. " +
                                    "Should be around " + clue.cli + ", your output:\n" + initialLine);
                }
                if (!line.contains("year olds")) {
                    return new CheckResult(false,
                            "No age in Coleman–Liau");
                }
            }
        }

        if (!foundWords) {
            return new CheckResult(false,
                    "There is no words amount");
        }

        if (!foundSentences) {
            return new CheckResult(false,
                    "There is no sentences amount");
        }

        if (!foundChars) {
            return new CheckResult(false,
                    "There is no characters amount");
        }

        if (!foundSyllables) {
            return new CheckResult(false,
                    "There is no syllables in output");
        }

        if (!foundPolysyllables) {
            return new CheckResult(false,
                    "There is no polysyllables in output");
        }

        if (!foundARI) {
            return new CheckResult(false,
                    "There is no Automated Readability Index in output");
        }

        if (!foundFKT) {
            return new CheckResult(false,
                    "There is no Flesch–Kincaid readability tests in output");
        }

        if (!foundSMG) {
            return new CheckResult(false,
                    "There is no Simple Measure of Gobbledygook in output");
        }

        if (!foundCLI) {
            return new CheckResult(false,
                    "There is no Coleman–Liau index in output");
        }

        return CheckResult.correct();
    }
}
