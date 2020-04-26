import org.hyperskill.hstest.v6.stage.BaseStageTest;
import org.hyperskill.hstest.v6.testcase.CheckResult;
import org.hyperskill.hstest.v6.testcase.TestCase;
import search.Main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

class TestClue {
    int referencesCount, requestsCount;
    String input;

    TestClue(int referencesCount, int requestsCount, String input) {
        this.referencesCount = referencesCount;
        this.requestsCount = requestsCount;
        this.input = input;
    }
}

public class SimpleSearchEngineTest extends BaseStageTest<TestClue> {

    private static String testCaseInput1 = "1\n" +
        "John\n" +
        "1\n" +
        "this text will definitely be no match";

    private static String testCaseInput2 = "3\n"
        + "john smith smith@gmail.com\n"
        + "John lock john_lock@somecompany.com\n"
        + "help me iamaprogrammer@gmail.com\n"
        + "4\n"
        + "john\n"
        + "Somecompany\n"
        + "@\n"
        + "this text will definitely be no match";

    public SimpleSearchEngineTest() {
        super(Main.class);
    }

    @Override
    public List<TestCase<TestClue>> generate() {
        return Arrays.asList(
            new TestCase<TestClue>()
                .setAttach(new TestClue(1, 1,
                testCaseInput1)).setInput(testCaseInput1),

            new TestCase<TestClue>()
                .setAttach(new TestClue(3, 4,
                testCaseInput2)).setInput(testCaseInput2)

        );
    }

    @Override
    public CheckResult check(String reply, TestClue clue) {
        String cR = "\n";
        List<String> outputLines = new LinkedList<String>(Arrays.asList(reply.split(cR)));
        String[] inputLines = clue.input.split(cR);
        String[] reference;
        String[] searchResult;

        int referenceCount, requestsCount;

        //check count of iteration to fill search reference
        try {
            referenceCount = Integer.parseInt(inputLines[0]);
        } catch (NumberFormatException e) {
            return new CheckResult(false, "The number of lines to search must be a number!");
        }

        reference = new String[referenceCount];

        for (int i = 0; i < referenceCount; i++) {
            reference[i] = inputLines[i + 1];
        }

        //check count of iteration to search some string;
        try {
            requestsCount = Integer.parseInt(inputLines[referenceCount + 1]);
        } catch (NumberFormatException e) {
            return new CheckResult(false,
                "The number of requests to search must be a number or " +
                "count of reference lines doesn't match input data!");
        }

        //clear the list of unnecessary lines, if any
        List<String> cleanedOutput = new ArrayList<String>();
        for (int i = 0; i < outputLines.size(); i++) {
            if (ContainsItemFromList(outputLines.get(i), reference)) {
                cleanedOutput.add(outputLines.get(i));
            }
        }

        //check found matches accuracy
        int actualTotalMatches = cleanedOutput.size();
        int requiredTotalMatches = 0;
        for (int j = 0; j < requestsCount; j++) {
            String toSearch = inputLines[referenceCount + 2 + j];
            searchResult = Arrays.stream(reference).filter(line -> line.toLowerCase()
                .contains(toSearch.toLowerCase().trim()))
                .toArray(String[]::new);

            requiredTotalMatches += searchResult.length;
            List<String> searchedFromOutput;

            try {
                searchedFromOutput = cleanedOutput
                    .subList(0, searchResult.length);
            } catch (IndexOutOfBoundsException ex) {
                return new CheckResult(false, "Can't parse your output. " +
                    "Please, make sure your output format matches with one in the example.");
            }


            if (!Arrays.equals(searchedFromOutput.toArray(), searchResult)) {
                return new CheckResult(false,
                    "Search result is not equal to the expected in search iteration " + j);
            }

            cleanedOutput.subList(0, searchResult.length).clear();
        }
        if (actualTotalMatches != requiredTotalMatches) {
            return new CheckResult(false, "wrong number of found matches!");
        }

        if(referenceCount != clue.referencesCount){
            return new CheckResult(false, "Reference count is incorrect");
        }
        else if(requestsCount != clue.requestsCount){
            return new CheckResult(false, "Search requests count is incorrect");
        }
        else {
            return CheckResult.TRUE;
        }
    }

    public static boolean ContainsItemFromList(String inputStr, String[] items) {
        return Arrays.stream(items).parallel().anyMatch(inputStr::contains);
    }
}
