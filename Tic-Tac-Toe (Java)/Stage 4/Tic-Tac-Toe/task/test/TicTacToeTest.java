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


class Clue {
    int x, y;
    String input;
    Clue(String input, int x, int y) {
        this.input = input;
        this.x = x;
        this.y = y;
    }
}

public class TicTacToeTest extends StageTest<Clue> {
    public TicTacToeTest() {
        super(Main.class);
    }

    static final String[] inputs = new String[] {
        "1 1", "1 2", "1 3",
        "2 1", "2 2", "2 3",
        "3 1", "3 2", "3 3"
    };

    String iterateCells(String initial) {
        int index = -1;
        for (int i = 0; i < inputs.length; i++) {
            if (initial.equals(inputs[i])) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            return "";
        }
        String fullInput = "";
        for (int i = index; i < index + 9; i++) {
            fullInput += inputs[i % inputs.length] + "\n";
        }
        return fullInput;
    }

    @Override
    public List<TestCase<Clue>> generate() {

        List<TestCase<Clue>> tests = new ArrayList<>();

        int i = 0;

        for (String startField : new String[] {
            "_XXOO_OX_",
            "_________",
            "X_X_O____"
        }) {

            for (String input : inputs) {
                String fullInput = iterateCells(input);

                String[] strNums = input.split("\\s+");
                int x = Integer.parseInt(strNums[0]);
                int y = Integer.parseInt(strNums[1]);

                if (i % 2 == 1) {
                    // mix with incorrect data
                    fullInput = "4 " + i + "\n" + fullInput;
                }

                tests.add(new TestCase<Clue>()
                    .setInput(startField + "\n" + fullInput)
                    .setAttach(new Clue(startField, x, y)));

                i++;
            }

        }

        return tests;
    }

    @Override
    public CheckResult check(String reply, Clue clue) {

        List<TicTacToeField> fields = TicTacToeField.parseAll(reply);

        if (fields.size() != 2) {
            return new CheckResult(false,
                "Can't find two fields inside output");
        }

        TicTacToeField curr = fields.get(0);
        TicTacToeField next = fields.get(1);

        TicTacToeField correctCurr = new TicTacToeField(clue.input);
        TicTacToeField correctNext = new TicTacToeField(correctCurr.field);

        String[] numInputs = iterateCells(clue.x + " " + clue.y).split("\n");
        for (String input : numInputs) {
            String[] strNums = input.split(" ");
            int x = Integer.parseInt(strNums[0]);
            int y = Integer.parseInt(strNums[1]);
            if (correctNext.field[y - 1][x - 1] == FieldState.FREE) {
                correctNext.field[y - 1][x - 1] = FieldState.X;
                break;
            }
        }

        if (!curr.equalTo(correctCurr)) {
            return new CheckResult(false,
                "The first field is not equal to the input field");
        }

        if (!next.equalTo(correctNext)) {
            return new CheckResult(false,
                "The first field is correct, but the second is not");
        }

        return CheckResult.correct();
    }
}
