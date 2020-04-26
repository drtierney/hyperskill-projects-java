import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testcase.TestCase;
import tictactoe.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

public class TicTacToeTest extends StageTest<String> {
    public TicTacToeTest() {
        super(Main.class);
    }


    @Override
    public List<TestCase<String>> generate() {
        List<TestCase<String>> tests = List.of(
            new TestCase<String>()
                .setInput("O OXXO XX"),

            new TestCase<String>()
                .setInput("OXO  X OX"),

            new TestCase<String>()
                .setInput("         "),

            new TestCase<String>()
                .setInput(" O  X   X")
        );

        for (TestCase<String> test: tests) {
            test.setInput(test.getInput().replace(" ", "_"));
            test.setAttach(test.getInput());
        }

        return tests;
    }

    @Override
    public CheckResult check(String reply, String clue) {

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
        TicTacToeField inputField = new TicTacToeField(clue);

        if (!userField.equalTo(inputField)) {
            return new CheckResult(false,
                "Your field doesn't match expected field");
        }

        return CheckResult.correct();
    }
}
