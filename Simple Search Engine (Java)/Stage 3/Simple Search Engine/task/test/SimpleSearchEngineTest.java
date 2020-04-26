import org.hyperskill.hstest.v6.stage.BaseStageTest;
import org.hyperskill.hstest.v6.testcase.CheckResult;
import org.hyperskill.hstest.v6.testcase.TestCase;
import search.Main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

class TestClue {
    int referencesCount;
    String input;

    TestClue(int referencesCount, String input) {
        this.referencesCount = referencesCount;
        this.input = input;
    }
}

public class SimpleSearchEngineTest extends BaseStageTest<TestClue> {

    public static final String names =
        "Dwight Joseph djo@gmail.com\n" +
        "Rene Webb webb@gmail.com\n" +
        "Katie Jacobs\n" +
        "Erick Harrington harrington@gmail.com\n" +
        "Myrtle Medina\n" +
        "Erick Burgess\n";

    public static final String test1 =
        "6\n" +
        names +
        "2\n" +
        "0";

    public static final String test2 =
        "6\n" +
        names +
        "1\n" +
        "burgess\n" +
        "0";

    public static final String test3 =
        "6\n" +
        names +
        "1\n" +
        "erick\n" +
        "0";

    public static final String test4 =
        "6\n" +
        names +
        "3\n" +
        "1\n" +
        "burgess\n" +
        "2\n" +
        "2\n" +
        "1\n" +
        "erick\n" +
        "0";

    public static final String test5 =
        "6\n" +
        names +
        "2\n" +
        "1\n" +
        "@\n" +
        "1\n" +
        "this text never find some match\n" +
        "2\n" +
        "0";

    public static final String test6 =
        "6\n" +
        names +
        "0";

    public SimpleSearchEngineTest() {
        super(Main.class);
    }

    @Override
    public List<TestCase<TestClue>> generate() {

        List<TestCase<TestClue>> tests = new ArrayList<>();

        for (String input : new String[]{
            test1, test2, test3, test4, test5, test6}) {

            tests.add(new TestCase<TestClue>()
                .setAttach(new TestClue(6, input))
                .setInput(input));
        }

        return tests;
    }

    @Override
    public CheckResult check(String reply, TestClue clue) {
        String cR = "\n";
        List<String> outputLines = new LinkedList<String>(Arrays.asList(reply.split(cR)));
        String[] inputLines = clue.input.split(cR);
        String[] reference;
        String[] idealSearchResult;

        int referenceCount;

        //check count of iteration to fill search reference
        try {
            referenceCount = Integer.parseInt(inputLines[0]);
        } catch (NumberFormatException e) {
            return new CheckResult(false,
                "The number of lines to search must be a number!");
        }

        if (referenceCount != clue.referencesCount) {
            return new CheckResult(false,
                "Count of search source lines not match expected!");
        }

        reference = new String[referenceCount];

        for (int i = 0; i < referenceCount; i++) {
            reference[i] = inputLines[i + 1];
        }

        //clear the list of unnecessary lines, if any
        List<String> cleanedOutput = new ArrayList<String>();
        for (int i = 0; i < outputLines.size(); i++) {
            if (ContainsItemFromList(outputLines.get(i), reference)) {
                cleanedOutput.add(outputLines.get(i));
            }
        }

        int currentInputLine = referenceCount + 1;
        int currentOutputLine = 0;

        int actionType = -1;

        int searchIteration = 1;
        int fullOutputIteration = 1;

        while (actionType != 0) {
            try {
                actionType = Integer.parseInt(inputLines[currentInputLine]);
            } catch (NumberFormatException e) {
                return new CheckResult(false,
                    "The number of menu item must be number" +
                        " or count of search source is wrong!");
            }

            switch (actionType) {
                case 1:
                    currentInputLine++;

                    String toSearch = inputLines[currentInputLine];

                    currentInputLine++;

                    idealSearchResult = Arrays.stream(reference)
                        .filter(line -> line.toLowerCase()
                        .contains(toSearch.toLowerCase().trim()))
                        .toArray(String[]::new);

                    String[] currentSearchResult = new String[idealSearchResult.length];
                    for (int i = 0; i < currentSearchResult.length; i++) {
                        try {
                            currentSearchResult[i] = cleanedOutput.get(currentOutputLine);
                        } catch (IndexOutOfBoundsException e) {
                            return new CheckResult(false,
                                "Seems like you output less than expected. " +
                                    "Either you've lost someone in the printing of all " +
                                    "people, or you haven't printed all the necessary " +
                                    "people in the search.");
                        }
                        currentOutputLine++;
                    }

                    Arrays.sort(currentSearchResult);
                    Arrays.sort(idealSearchResult);

                    if (!Arrays.equals(currentSearchResult, idealSearchResult)) {
                        return new CheckResult(false,
                            "Search result is not equal " +
                                "to the expected search");
                    }

                    searchIteration++;
                    break;

                case 2:
                    currentInputLine++;

                    String[] currentAll = new String[reference.length];
                    for (int i = 0; i < currentAll.length; i++) {
                        try {
                            currentAll[i] = cleanedOutput.get(currentOutputLine);
                        } catch (IndexOutOfBoundsException e) {
                            return new CheckResult(false,
                                "Seems like you output less than expected. " +
                                    "Either you've lost someone in the printing of all " +
                                    "people, or you haven't printed all the necessary " +
                                    "people in the search.");
                        }
                        currentOutputLine++;
                    }

                    Arrays.sort(currentAll);
                    Arrays.sort(reference);

                    if (!Arrays.equals(currentAll, reference)) {
                        return new CheckResult(false,
                            "Looks like you're printing " +
                                "unknown people when you enter option 2.");
                    }
                    fullOutputIteration++;
                    break;
                case 0:
                    return CheckResult.TRUE;
                default:
                    currentInputLine++;
                    break;
            }
        }

        return CheckResult.TRUE;
    }

    public static boolean ContainsItemFromList(String inputStr, String[] items) {
        return Arrays.stream(items).parallel().anyMatch(inputStr::contains);
    }
}

