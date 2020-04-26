import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testcase.TestCase;
import tictactoe.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


class Attach {
    String input;
    String result;

    Attach(String result) {
        this.result = result;
    }
}

enum FieldState {
    X, O, FREE;

    static FieldState get(char symbol) {
        switch (symbol) {
            case 'X': return X;
            case 'O': return O;
            case ' ':
            case '_':
                return FREE;
            default: return null;
        }
    }
}

class TicTacToeField {

    final FieldState[][] field;

    TicTacToeField(FieldState[][] field) {
        this.field = new FieldState[3][3];
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                this.field[row][col] = field[row][col];
            }
        }
    }

    TicTacToeField(String str) {
        field = new FieldState[3][3];
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                field[row][col] =
                    FieldState.get(str.charAt(((2 - row) * 3 + col)));
            }
        }
    }

    boolean equalTo(TicTacToeField other) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (field[i][j] != other.field[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    boolean hasNextAs(TicTacToeField other) {
        boolean improved = false;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (field[i][j] != other.field[i][j]) {
                    if (field[i][j] == FieldState.FREE && !improved) {
                        improved = true;
                    }
                    else {
                        return false;
                    }
                }
            }
        }
        return improved;
    }

    boolean differByOne(TicTacToeField other) {
        boolean haveSingleDifference = false;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (field[i][j] != other.field[i][j]) {
                    if (haveSingleDifference) {
                        return false;
                    }
                    haveSingleDifference = true;
                }
            }
        }

        return haveSingleDifference;
    }

    boolean isCloseTo(TicTacToeField other) {
        return equalTo(other)
            || hasNextAs(other)
            || other.hasNextAs(this);
    }

    static TicTacToeField parse(String fieldStr) {

        try {
            List<String> lines = fieldStr
                .lines()
                .map(String::strip)
                .filter(e ->
                    e.startsWith("|")
                        && e.endsWith("|"))
                .collect(Collectors.toList());

            for (String line : lines) {
                for (char c : line.toCharArray()) {
                    if (c != 'X'
                        && c != 'O'
                        && c != '|'
                        && c != ' '
                        && c != '_') {
                        return null;
                    }
                }
            }

            FieldState[][] field = new FieldState[3][3];

            int y = 2;
            for (String line : lines) {
                char[] cols = new char[] {
                    line.charAt(2),
                    line.charAt(4),
                    line.charAt(6)
                };

                int x = 0;
                for (char c : cols) {
                    FieldState state = FieldState.get(c);
                    if (state == null) {
                        return null;
                    }
                    field[y][x] = state;
                    x++;
                }
                y--;
            }

            return new TicTacToeField(field);
        } catch (Exception ex) {
            return null;
        }
    }


    static List<TicTacToeField> parseAll(String output) {
        List<TicTacToeField> fields = new ArrayList<>();

        List<String> lines = output
            .lines()
            .map(String::strip)
            .filter(e -> e.length() > 0)
            .collect(Collectors.toList());

        String candidateField = "";
        boolean insideField = false;
        for (String line : lines) {
            if (line.contains("----") && !insideField) {
                insideField = true;
                candidateField = "";
            } else if (line.contains("----") && insideField) {
                TicTacToeField field = TicTacToeField.parse(candidateField);
                if (field != null) {
                    fields.add(field);
                }
                insideField = false;
            }

            if (insideField && line.startsWith("|")) {
                candidateField += line + "\n";
            }
        }

        return fields;
    }

}

public class TicTacToeTest extends StageTest<Attach> {
    public TicTacToeTest() {
        super(Main.class);
    }

    @Override
    public List<TestCase<Attach>> generate() {
        List<TestCase<Attach>> tests = List.of(
            new TestCase<Attach>()
                .setInput("XXXOO  O ")
                .setAttach(new Attach("X wins")),

            new TestCase<Attach>()
                .setInput("XOXOXOXXO")
                .setAttach(new Attach("X wins")),

            new TestCase<Attach>()
                .setInput("XOOOXOXXO")
                .setAttach(new Attach("O wins")),

            new TestCase<Attach>()
                .setInput("XOXOOXXXO")
                .setAttach(new Attach("Draw")),

            new TestCase<Attach>()
                .setInput("XO OOX X ")
                .setAttach(new Attach("Game not finished")),

            new TestCase<Attach>()
                .setInput("XO XO XOX")
                .setAttach(new Attach("Impossible")),

            new TestCase<Attach>()
                .setInput(" O X  X X")
                .setAttach(new Attach("Impossible")),

            new TestCase<Attach>()
                .setInput(" OOOO X X")
                .setAttach(new Attach( "Impossible"))
        );

        for (TestCase<Attach> test : tests) {
            test.setInput(test.getInput().replace(" ", "_"));
            test.getAttach().input = test.getInput();
        }

        return tests;
    }

    @Override
    public CheckResult check(String reply, Attach clue) {

        List<TicTacToeField> fields = TicTacToeField.parseAll(reply);

        if (fields.size() == 0) {
            return new CheckResult(false,
                "Can't parse the field! " +
                    "Check if you output a field in format like in the example.");
        }

        if (fields.size() > 1) {
            return new CheckResult(false,
                "There are more than one field in the output! " +
                    "You should output a single field.");
        }

        TicTacToeField userField = fields.get(0);
        TicTacToeField inputField = new TicTacToeField(clue.input);

        if (!userField.equalTo(inputField)) {
            return new CheckResult(false,
                "Your field doesn't match expected field");
        }

        List<String> lines = reply
            .strip()
            .lines()
            .map(String::strip)
            .filter(e -> e.length() > 0)
            .collect(Collectors.toList());

        String lastLine = lines.get(lines.size() - 1);

        if (! (lastLine.equals("X wins")
            || lastLine.equals("O wins")
            || lastLine.equals("Draw")
            || lastLine.equals("Game not finished")
            || lastLine.equals("Impossible")
        )) {
            return new CheckResult(false,
                "Can't parse result, " +
                    "should be one of the outcomes mentioned in description. " +
                    "Your last line: \"" + lastLine + "\"");
        }

        if (!lastLine.equals(clue.result)) {
            return new CheckResult(false,
                "The result is incorrect. " +
                    "Should be: \"" + clue.result + "\", " +
                    "found: \"" + lastLine + "\". " +
                    "Check if your program works correctly in test examples in description.");
        }

        return CheckResult.correct();
    }
}
