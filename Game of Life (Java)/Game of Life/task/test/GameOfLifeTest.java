import life.Main;
import org.hyperskill.hstest.v6.stage.BaseStageTest;
import org.hyperskill.hstest.v6.testcase.PredefinedIOTestCase;

import java.util.List;

public class GameOfLifeTest extends BaseStageTest {
    public GameOfLifeTest() throws Exception {
        super(Main.class);
    }

    @Override
    public List<PredefinedIOTestCase> generate() {
        return List.of(
            new PredefinedIOTestCase(
                "4 4",
                "OOOO\n" +
                    "O O \n" +
                    "   O\n" +
                    "OOO "
            ),

            new PredefinedIOTestCase(
                "4 120",
                "OO  \n" +
                    "O  O\n" +
                    " OO \n" +
                    " O  "
            ),

            new PredefinedIOTestCase(
                "10 10",
                "O    O  O \n" +
                    " OOOO   O \n" +
                    " O   OO O \n" +
                    "OO OO  OO \n" +
                    "   O      \n" +
                    "OO OOOOOOO\n" +
                    "OO O O  OO\n" +
                    "    O O  O\n" +
                    "OO       O\n" +
                    "OOOO    O "
            ),

            new PredefinedIOTestCase(
                "50 0",
                "OO OO O OO   OOOO O      O  O OO  O   OOOO OOOOOO \n" +
                    "O OOOO O  O OOO      O O O OOOOOOOO     O   O OOOO\n" +
                    "  O OO O OO OOO O   O OOO OO    O OOOOOO   O      \n" +
                    "O O OOOO   OOO     O OOO O OO O  OOOOOO  OO   O O \n" +
                    "   OO    OOOOOO  O  O  O O  OO    O  O  O OOOOO O \n" +
                    " OOOO O  OOOO OOO O  OO    OO OO OO  O  OO OO O O \n" +
                    " O O  O         O  O OOOOO OOO   OOO OO O OO  O OO\n" +
                    "  O  O OOO O   O OOOOO  O       OO  OOOOO    O OOO\n" +
                    "   OO O O     OOO  O    OOO     O OOOOO  O OOOO  O\n" +
                    "OOOOOOOO O O  OOO O  OO OO   OO      O   O  O    O\n" +
                    " OOO O  OOO   O O O    OOO   OOO OOO       O O OOO\n" +
                    "OO OO  O  O O  O OO   O OO OO OOOO O     O   O O O\n" +
                    "OOO  OO  OO  OO    OO      OOO OO OOO    O   OO OO\n" +
                    "O OO    OO O OO OO O   O  OOO    OO    OO  OO  OOO\n" +
                    "O  O  O  O O OO   O  OOOO OOO         OOOOO O O OO\n" +
                    "      OOOO  O  O OOOO  O OOOOOOO O  OO  O  OOO OOO\n" +
                    "OOO O   OO  O O    OO OO OO  O O OO OO  OO OO    O\n" +
                    "O OOO   OOOO    O  O  O      O   O O  O   OOOOO OO\n" +
                    "   O OOO OO  O  OO  O  O  O  O  OOO O     OOO   OO\n" +
                    "  OO OOOOO OOOOOOO  O         OO OO  OO      OOO  \n" +
                    "  O   OOOOO  OO O OOO O O O     OO   O O  OO  OOO \n" +
                    " O O    OOOOO OOOO OO   O  O O     O   O OOO O OO \n" +
                    " OO  OO O    OO    O  OO  O OOOO O      O OOO OOO \n" +
                    "OO OOOOOOO O    OO  O  O OO O OO O     OOOOOO  O O\n" +
                    "   OO  OO OO   O  O  O  O OO  O  O  O  O O   OO  O\n" +
                    "O  OO O  OO   OOO  OOOO  OO  OO OO  O   O OO    O \n" +
                    "OO O OOO    OOOO     O     O  OOOOO  O     OOOO   \n" +
                    "  O O OOO   OOO O  OOO O OOO   O     O OOOOO  OO O\n" +
                    "  OOOO     OOOOOOOO    O O  OOO    O OOO O OOOO OO\n" +
                    " O  OOOOO   OOO    OOOOOO OOOO   O O OOOOOOO     O\n" +
                    "O  OO OO OO O  OOOOO    O OOO  OOO O   O  OOO OO O\n" +
                    " OO OOO  OOO    O OOO OOO O OOO    O O  OO  O OO  \n" +
                    "OO     O  OO OOO   OOOO    OOOO O O OOOO OO   O O \n" +
                    "O OOOO O  OO   OO O  O    O     O   O   OO OOOOOO \n" +
                    " OO O OOO    O  OOOO OOO    O O O     O   OO  OOO \n" +
                    "O   O OOOOOOOO  O OO O   OOO  OOO  O   O   O O  O \n" +
                    "O     OOO  O   O  OOOOOOO  OOOO OOO O OOOO    O  O\n" +
                    "O          OO O  O  O  OO O    OO O  O    OOOOOOOO\n" +
                    "O  OO O   OO   O       OO  OOOO OO OO OO  OOO OOOO\n" +
                    " O    O OO O OO     O   O    OO OO O  OOOOOOOOO O \n" +
                    " O     OOO    OO O     O   O     OOO OOO OOOOOO   \n" +
                    "OOO OO O    O    O O  OOOO O O O O OO OOO OO O   O\n" +
                    "OOOOO OO   OOOO  OO O OO OO  O   O O  O   OOO     \n" +
                    "  OOO O    O     O     O    O  O  OO  OOO O  O O  \n" +
                    " OOO O O OOOOOOOOOO O  O  OO      OOO O O O OO OOO\n" +
                    "  OO    O  OO      OO   O OOO    OOO  OOO OOO   O \n" +
                    "OOOO OO OO OO OOOOOO  OOOO  OOOOOOOOO OO     OO O \n" +
                    "O OO  O  O O  O OOO    OOOO    OO OOOOOOO  OOO    \n" +
                    " OOOOO O OO O  O  OO   O OOOO OO O OO OOOOOOOOO  O\n" +
                    " O OOO OOO O  OO    O  O       OO O      OOO OO O "
            ),

            new PredefinedIOTestCase(
                "50 2018",
                " O  O    OOOO OOOO OOO O OO  O  O OO OOOO  OO O    \n" +
                    "O   OO OOO OO  O O  O  O  OO OOOO  OO   O         \n" +
                    "O   O O   OOOO O       O OOO  O       O    O O   O\n" +
                    "OOO OOO  O O O   O O O    O OOOOO  O O  O O  O O  \n" +
                    "      OO O  O O O O   O  O  O O O O    OO   O OOOO\n" +
                    " OO O OOO O   O OOO O OO  OOO O   OOO OOO OOOO   O\n" +
                    " OOO OOO OO  OOO O     OOO O  OOOO  O OO O  O OOO \n" +
                    " O    O  OO  OOOO O O  O O    O  O O     OO      O\n" +
                    "    OO OOOO    OOOO O OO  OO OOO  O  OOOO OOO OOOO\n" +
                    "  OO OO OO O O    O O O    OOO  O   O O   OOOOOOOO\n" +
                    "  O O   OOO     OO   OO    O  O O O   OO    O OOOO\n" +
                    "  OOOOO OO     O O OO OO      O OO OOO O   OO O   \n" +
                    " OOO O  OO  O OOO O OO  O OO O  O OO OO  OOO O  OO\n" +
                    "O O O O  O OOO O  OO    O O O  OO    OO OO O      \n" +
                    " O O   OOOOO O  O  O OO      O  OOO O  OO  OOOOOO \n" +
                    "   O    O   O O   OOO O OO OOO OO     O O  O  OOOO\n" +
                    "O    OO OO  O    OOOO  OO   O O  O OOO  OOOO     O\n" +
                    "  O  OOO   O O  O  O O  OOOO   O OO    O OO  O  O \n" +
                    "  O OOO OO  OOO    OOOOOOO  O  OOOOOO O O     OOOO\n" +
                    " O  OO   OO OOOO OOO  OOOOOOO        O  O   OOO  O\n" +
                    "OOOOO OOOO OO  O O O  O OO    O   O  OO OOO OO O  \n" +
                    " O O O OO OO OOOOO  OO  OOO OOOOOO OO  OOO OO O   \n" +
                    " OOO O  OO   O  OOOO  O OO     OOOO  O O  OO OOOO \n" +
                    "OOOOO OOOO OOOOOOOO  O OOOOO    O   OOO OO OOOOOO \n" +
                    "O   O O O  O      OO     OOOO    OOO   O  OOO   OO\n" +
                    "    O    O  OO  O  O   OOOOO O O    O OOO   O   O \n" +
                    " OO OOO O      OOOOO  OOOOO OO OO OOO  OOO O O    \n" +
                    "O  OOOOOOO   O OO OO OOOO  OOOOO  O  OOOO  O OOO  \n" +
                    "OOO    OO    OO OOOO OO   O O O OO  OOOO  OOO     \n" +
                    "OO    O  OO  O O OOOO O O  OO OO O          OO  O \n" +
                    "O  O OO O O    OO    OOO OO OO   OO  O  OO O OO OO\n" +
                    "     OO     O OO    OOO   OO O OO OO    O O O OO  \n" +
                    "O   OOO OO  O OO   O   OO     O OOOO  O OO OOO  O \n" +
                    " OOOO     O O O   OO OOOO OOOO O O  O  O O  O OO  \n" +
                    "OO OOOO O O  O    O  O OOO  OO  O O  OO  OO OOOOOO\n" +
                    "OOOOOOO OO   O     O   OOOO    O   O   O O O OO   \n" +
                    " OOOOOOOO  OO O     O OO O  O O  O O O  O  OO O  O\n" +
                    "O  O OO OO OOOOO O OO OO   OOOOOO OOO OO       O  \n" +
                    "OOO OO   O OOOO     O OO   O O  OOO O      O OO  O\n" +
                    "O       O OOO O O  OOOO OO  O O  OOOOO  OOO O OOO \n" +
                    "O O    O  O  O O O   O OO   OO OO  O OOOOOOOOO  OO\n" +
                    " OO  OO   O     OO O OO  O   O  OOOOO  O     O  O \n" +
                    "O  OOO OO   O  O  OOO OO O     O   O  O   O   O  O\n" +
                    "O OOO OOO OO OO O OO OO O O O O O O   OOO OOO  OO \n" +
                    " OO  O  O  O OOO O OOO   O  O  OO OOOOO OOO  OOO O\n" +
                    " OO    OO  OO OOO    O  O  OO OO OOO O O OOO   O  \n" +
                    "OOO OOOO OOO   OO OO   OOOO      O  O   OO O    OO\n" +
                    " OO OO O  O  OO  O OOOO  OO O OOO  OOO O  O OOO  O\n" +
                    "OOO OO   OOO OOOOO   OOOO O OOOO OO O  O   OO   O \n" +
                    " O   OOOOOO  O   O OOO OOO O  OOO   OO   O  OO O  "
            ),

            new PredefinedIOTestCase(
                "50 -128",
                " OOOO     O    OO O  O   O   O OO O OOO  O    OOO \n" +
                    " O O O O OOOOO  O O O OO OOOO OOO  OOO O    OOOO  \n" +
                    "  OO O OO  O  O    OO    O O  O OO   O O    OOO  O\n" +
                    "OO   OOOO   OO OO O O O   OOO O O O O OOOO OOO OOO\n" +
                    "O O OO O  OO   O   O   O  OOOO    O  OO   O  O O O\n" +
                    "OO OO   O OOOOOO OOO  O OO    OOO     OOO OO O  OO\n" +
                    "  O   OOO   O O   O  OOO O O  O O  O OO OOOOOOO OO\n" +
                    "  OO OOOOOO OO   OOO OOOOOOOOOOO OO  OO   OOOO O  \n" +
                    "OOO OO OOO       O    O O   O O  OOO   O    O OOO \n" +
                    "OOOOO O OO OO  O OOO  OO  OOOOOO O O O    OO O OOO\n" +
                    "O OOO O O     OO  OO OOOO  OO OOO O   OO   OOOO OO\n" +
                    "OO OOOO  OO OO  O      O O        O  OOOO O OOO   \n" +
                    "OOOOOOOO  OO OOOO  O O O O  OO  O OOOO O OOO O OOO\n" +
                    "O  OO  OO   O  OO  O OOO  O OOOOOOO   OO OOO   OOO\n" +
                    " O O O O O  OOOOO OOO  OOOOOO OOOO   OOO  OO  OO O\n" +
                    "OO O O OOOO    O O O  O O   OO   OO O OOO  OO   OO\n" +
                    "OO   O  O    O       OOOO  OOO  OOO    OOO O  O  O\n" +
                    "OO  O OOO OOOO   O O OOOOO  O O    O  OO  O  OOOO \n" +
                    "O OO O OO OO O O    O  O O OO  O  O O      OOOO O \n" +
                    "OOOO  OOOOOOOOOOOO OO OO O OO   OO O O OOO   O OO \n" +
                    "   OO  OO O OOOOO OOO    O OO OO   OO  OO OO   O O\n" +
                    " OOO  O    OOO OOOOOO   O    OOO OO    OOO O   O O\n" +
                    "O O OOO  OO   OO O   OO  O OOOO  OO    O  OOO O   \n" +
                    "   O O   O   OOOO  OO OO OO OOOO    O OOOO   OO   \n" +
                    "O  OO O      OO   O O    O   OOOOO O O OO     OO O\n" +
                    "O O  O O   O  O  O O OOO  O O  OOO    O OO O   O  \n" +
                    "O  OOOO O O O     OOOOOO OOO OOO OO O OO   O  O O \n" +
                    "OO OO  O   OOOOO O OOO OO  O   OO   O O O OOOOOOO \n" +
                    "O O  O  OO  OOOO O  OOO O O OO     O O OOO  OO  O \n" +
                    "OO   OO O  OOO OOO  O OO OOO    OO OO O O  O O  OO\n" +
                    "O     OOOO O   O O OOOO OO  O OO    O O   OOO OO  \n" +
                    " O OOOO O  OO  O   O  O   OOO OO   OO OO        O \n" +
                    "OO   O  OOOO  OO OOO OO   O  O      O OO O   OOO O\n" +
                    "OO O OO O O O O OO      O   OO  OO  OOOO O O O  O \n" +
                    "OOOO  O     OO  O  O  OOOOO OO   O O  O O OOO  OO \n" +
                    "OOO OOOOOOO  O O O O OO      OO OOOOOOOO O OO O  O\n" +
                    "OO  O O  O O O OOOO OOOOO  OOO     OOO      OO  O \n" +
                    " O      O OOOOO  O OO O O   O   O  OO OO OO OO  OO\n" +
                    "O      O OO OO  OOOO OO O O   OOO      O     O    \n" +
                    "O       OOOO O OO OO OO OO OO    OO OOO   O   OOO \n" +
                    "OO O O    OOO   OOOOO O O OO OOOO O O    O   O   O\n" +
                    " O OO O   O  OO  OOOO   O O OOO   O    OOOO O OO O\n" +
                    "   O   O OOO OO O OOO   OOOO  OO   OOOO O O  OOO  \n" +
                    "  O  OO    OOOOOOO O O  O OO  O   O OO OO   O     \n" +
                    "OO O O  OOO O O     O   O    OOOO O OOO  OO O OOOO\n" +
                    "O OOO    OOO  OO  OO   O   O  O OO OOO  OO OO  OO \n" +
                    "OOOOOOOOO O    OOOO  O  O OO  OO   OO O OO OOOO OO\n" +
                    "    OOO O OO O     O OOOO   OO OO  OOOO O O   O   \n" +
                    " OOOOOOOOO OOOOOOO O     OOOO O O OOO OOOO O O OOO\n" +
                    "   O OO O  OO  O OO OOO  OOO       O  O  O OO  O  "
            )
        );
    }
}
