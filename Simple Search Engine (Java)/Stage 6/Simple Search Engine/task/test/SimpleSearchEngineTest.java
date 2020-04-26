import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testcase.TestCase;
import search.Main;

import java.util.*;

class TestClue {
    String input;

    TestClue(String input) {
        this.input = input;
    }
}

public class SimpleSearchEngineTest extends StageTest<TestClue> {
    public SimpleSearchEngineTest() {
        super(Main.class);
    }

    @Override
    public List<TestCase<TestClue>> generate() {
        return Arrays.asList(
            new TestCase<TestClue>().setAttach(
                new TestClue("1\n" +
                "ANY\n" +
                "bob gallien@evilcorp.com\n" +
                "2\n" +
                "1\n" +
                "NONE\n" +
                "bob gallien@evilcorp.com\n" +
                "0")).setInput("1\n" +
                "ANY\n" +
                "bob gallien@evilcorp.com\n" +
                "2\n" +
                "1\n" +
                "NONE\n" +
                "bob gallien@evilcorp.com\n" +
                "0")
                .addArguments("--data", "names.txt")
                .addFile("names.txt", SearchEngineTests.NAMES),


            new TestCase<TestClue>().setAttach(new TestClue("2\n" +
                "1\n" +
                "ALL\n" +
                "this text never gonna be matched\n" +
                "2\n" +
                "0")).setInput("2\n" +
                "1\n" +
                "ALL\n" +
                "this text never gonna be matched\n" +
                "2\n" +
                "0")
                .addArguments("--data", "names.txt")
                .addFile("names.txt", SearchEngineTests.NAMES)
        );
    }

    @Override
    public CheckResult check(String reply, TestClue clue) {
        String cR = "\n";
        List<String> outputLines = new LinkedList<String>(Arrays.asList(reply.split(cR)));
        String[] inputLines = clue.input.split(cR);
        String[] reference;

        reference = SearchEngineTests.NAMES.split("\n");

        //clear the list of unnecessary lines, if any
        List<String> cleanedOutput = new ArrayList<String>();
        for (int i = 0; i < outputLines.size(); i++) {
            if (containsItemFromList(outputLines.get(i), reference)) {
                cleanedOutput.add(outputLines.get(i).toLowerCase());
            }
        }

        int currentInputLine = 0;
        int currentOutputLine = 0;

        int actionType = -1;

        while (actionType != 0) {
            try {
                actionType = Integer.parseInt(inputLines[currentInputLine]);
            } catch (NumberFormatException e) {
                return new CheckResult(false,
                    "The number of menu item must be number!");
            }

            switch (actionType) {
                case 1:
                    currentInputLine++;

                    List<Integer> result = new ArrayList<>();

                    List<String> intendedResult = new ArrayList<>();

                    while (true) {
                        String mode = inputLines[currentInputLine].trim().toLowerCase();
                        currentInputLine++;

                        String toSearch = "";

                        if (mode.equalsIgnoreCase("all")) {
                            toSearch = inputLines[currentInputLine].trim().toLowerCase();
                            currentInputLine++;
                            String[] allChecks = toSearch.split(" ");

                            for (String s : reference) {
                                s = s.toLowerCase();

                                boolean isPassedChecks = true;

                                for (String currCheck : allChecks) {
                                    if (!(s.contains(" " + currCheck + " ")
                                        || s.startsWith(currCheck + " ")
                                        || s.endsWith(" " + currCheck))) {

                                        isPassedChecks = false;
                                        break;
                                    }
                                }

                                if (isPassedChecks) {
                                    intendedResult.add(s);
                                }
                            }
                            break;
                        } else if (mode.equalsIgnoreCase("any")) {
                            toSearch = inputLines[currentInputLine].trim().toLowerCase();
                            currentInputLine++;
                            String[] allChecks = toSearch.split(" ");

                            for (String s : reference) {
                                s = s.toLowerCase();

                                boolean isPassedChecks = false;

                                for (String currCheck : allChecks) {
                                    if (s.contains(" " + currCheck + " ")
                                        || s.startsWith(currCheck + " ")
                                        || s.endsWith(" " + currCheck)) {

                                        isPassedChecks = true;
                                        break;
                                    }
                                }

                                if (isPassedChecks) {
                                    intendedResult.add(s);
                                }
                            }
                            break;
                        } else if (mode.equalsIgnoreCase("none")) {
                            toSearch = inputLines[currentInputLine].trim().toLowerCase();
                            currentInputLine++;
                            String[] allChecks = toSearch.split(" ");

                            for (String s : reference) {
                                s = s.toLowerCase();

                                boolean isPassedChecks = true;

                                for (String currCheck : allChecks) {
                                    if (s.contains(" " + currCheck + " ")
                                        || s.startsWith(currCheck + " ")
                                        || s.endsWith(" " + currCheck)) {

                                        isPassedChecks = false;
                                        break;
                                    }
                                }

                                if (isPassedChecks) {
                                    intendedResult.add(s);
                                }
                            }
                            break;
                        }
                    }

                    String[] userResult = new String[intendedResult.size()];
                    for (int i = 0; i < intendedResult.size(); i++) {
                        try {
                            userResult[i] = cleanedOutput.get(currentOutputLine++);
                        } catch (IndexOutOfBoundsException e) {
                            return new CheckResult(false,
                                "Seems like you output less than expected. " +
                                    "Either you've lost someone in the printing of all " +
                                    "people, or you haven't printed all the necessary " +
                                    "people in the search.");
                        }
                    }

                    String[] correctOutput = intendedResult.toArray(String[]::new);

                    Arrays.sort(correctOutput);
                    Arrays.sort(userResult);

                    if (!Arrays.equals(correctOutput, userResult)) {
                        return new CheckResult(false,
                            "Search result is not equal " +
                                "to the expected search");
                    }
                    break;
                case 2:
                    currentInputLine++;

                    List<String> intendedResultAll = new ArrayList<>();

                    for (String s : reference) {
                        s = s.toLowerCase();
                        intendedResultAll.add(s);
                    }

                    String[] userResultAll = new String[intendedResultAll.size()];
                    for (int i = 0; i < intendedResultAll.size(); i++) {
                        try {
                            userResultAll[i] = cleanedOutput.get(currentOutputLine++);
                        } catch (IndexOutOfBoundsException e) {
                            return new CheckResult(false,
                                "Seems like you output less than expected. " +
                                    "Either you've lost someone in the printing of all " +
                                    "people, or you haven't printed all the necessary " +
                                    "people in the search.");
                        }
                    }

                    String[] correctOutputAll = intendedResultAll.toArray(String[]::new);

                    Arrays.sort(correctOutputAll);
                    Arrays.sort(userResultAll);

                    if (!Arrays.equals(correctOutputAll, userResultAll)) {
                        return new CheckResult(false,
                            "Looks like you're printing " +
                                "unknown people when you enter option 2.");
                    }
                    break;
                case 0:
                    return CheckResult.correct();
                default:
                    currentInputLine++;
                    break;
            }
        }

        return CheckResult.correct();
    }

    private static boolean containsItemFromList(String inputStr, String[] items) {
        return Arrays.stream(items).parallel().anyMatch(inputStr::contains);
    }
}
