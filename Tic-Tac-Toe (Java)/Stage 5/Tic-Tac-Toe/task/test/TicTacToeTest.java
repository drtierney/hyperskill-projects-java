import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testcase.TestCase;
import tictactoe.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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

    FieldState get(int x, int y) {
        return field[y - 1][x - 1];
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

    boolean isWinning(FieldState side) {
        if (side == FieldState.FREE) {
            return false;
        }

        if (get(1, 1) == side &&
            get(1, 2) == side &&
            get(1, 3) == side) {
            return true;
        }

        if (get(2, 1) == side &&
            get(2, 2) == side &&
            get(2, 3) == side) {
            return true;
        }

        if (get(3, 1) == side &&
            get(3, 2) == side &&
            get(3, 3) == side) {
            return true;
        }

        if (get(1, 1) == side &&
            get(2, 1) == side &&
            get(3, 1) == side) {
            return true;
        }

        if (get(1, 2) == side &&
            get(2, 2) == side &&
            get(3, 2) == side) {
            return true;
        }

        if (get(1, 3) == side &&
            get(2, 3) == side &&
            get(3, 3) == side) {
            return true;
        }

        if (get(1, 1) == side &&
            get(2, 2) == side &&
            get(3, 3) == side) {
            return true;
        }

        if (get(1, 3) == side &&
            get(2, 2) == side &&
            get(3, 1) == side) {
            return true;
        }

        return false;
    }

    boolean isDraw() {
        if (isWinning(FieldState.X) || isWinning(FieldState.O)) {
            return false;
        }
        for (int x = 1; x <= 3; x++) {
            for (int y = 1; y <= 3; y++) {
                if (get(x, y) == FieldState.FREE) {
                    return false;
                }
            }
        }
        return true;
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
    Clue(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

public class TicTacToeTest extends StageTest<Clue> {
    public TicTacToeTest() throws Exception {
        super(Main.class);
    }

    static String[] inputs = new String[] {
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
        for (String input : inputs) {

            Random random = new Random();
            String randomInput = "";
            for (int j = 0; j < 10; j++) {
                int randX = random.nextInt(4) + 1;
                int randY = random.nextInt(4) + 1;
                randomInput += randX + " " + randY + "\n";
            }

            String fullMoveInput = randomInput
                + iterateCells(input) + iterateCells(input);

            String[] strNums = input.split(" ");
            int x = Integer.parseInt(strNums[0]);
            int y = Integer.parseInt(strNums[1]);

            if (i % 2 == 1) {
                // mix with incorrect data
                fullMoveInput = "4 " + i + "\n" + fullMoveInput;
            }

            String fullGameInput = "";
            for (int j = 0; j < 9; j++) {
                fullGameInput += fullMoveInput;
            }

            tests.add(new TestCase<Clue>()
                .setInput(fullGameInput)
                .setAttach(new Clue(x, y)));

            i++;
        }

        return tests;
    }

    @Override
    public CheckResult check(String reply, Clue clue) {

        List<TicTacToeField> fields = TicTacToeField.parseAll(reply);

        if (fields.size() == 0) {
            return new CheckResult(false, "No fields found");
        }

        for (int i = 1; i < fields.size(); i++) {
            TicTacToeField curr = fields.get(i - 1);
            TicTacToeField next = fields.get(i);

            if (!(curr.equalTo(next) || curr.hasNextAs(next))) {
                return new CheckResult(false,
                    "For two fields following each " +
                        "other one is not a continuation " +
                        "of the other (they differ more than in two places).");
            }
        }

        List<String> lines = reply
            .strip()
            .lines()
            .map(String::strip)
            .filter(e -> e.length() > 0)
            .collect(Collectors.toList());

        String lastLine = lines.get(lines.size() - 1);

        if (! (lastLine.contains("X wins")
            || lastLine.contains("O wins")
            || lastLine.contains("Draw")
        )) {
            return new CheckResult(false,
                "Can't parse final result, " +
                    "should contain \"Draw\", \"X wins\" or \"O wins\".\n" +
                    "Your last line: \"" + lastLine + "\"");
        }

        if (lastLine.contains("X wins") && lastLine.contains("O wins")) {
            return new CheckResult(false,
                "Your final result contains \"X wins\" and \"O wins\" " +
                    "at the same time. This is impossible.\n" +
                    "Your last line: \"" + lastLine + "\"");
        }

        if (lastLine.contains("X wins") && lastLine.contains("Draw")) {
            return new CheckResult(false,
                "Your final result contains \"X wins\" and \"Draw\" " +
                    "at the same time. This is impossible.\n" +
                    "Your last line: \"" + lastLine + "\"");
        }

        if (lastLine.contains("O wins") && lastLine.contains("Draw")) {
            return new CheckResult(false,
                "Your final result contains \"O wins\" and \"Draw\" " +
                    "at the same time. This is impossible.\n" +
                    "Your last line: \"" + lastLine + "\"");
        }

        TicTacToeField lastField = fields.get(fields.size() - 1);

        if (lastField.isWinning(FieldState.X) && !lastLine.contains("X wins")) {
            return new CheckResult(false,
                "Your last field shows that X wins, " +
                    "and your last line should contain \"X wins\".\n" +
                    "Your last line: \"" + lastLine + "\"");
        }

        if (lastField.isWinning(FieldState.O) && !lastLine.contains("O wins")) {
            return new CheckResult(false,
                "Your last field shows that O wins, " +
                    "and your last line should contain \"O wins\".\n" +
                    "Your last line: \"" + lastLine + "\"");
        }

        if (lastField.isDraw() && !lastLine.contains("Draw")) {
            return new CheckResult(false,
                "Your last field shows that there is a draw, " +
                    "and your last line should contain \"Draw\".\n" +
                    "Your last line: \"" + lastLine + "\"");
        }

        if (lastField.isWinning(FieldState.X) ||
            lastField.isWinning(FieldState.O) ||
            lastField.isDraw()) {
            return CheckResult.correct();
        }

        return CheckResult.wrong(
            "Your last field contains unfinished game, the game should be finished!"
        );
    }
}
