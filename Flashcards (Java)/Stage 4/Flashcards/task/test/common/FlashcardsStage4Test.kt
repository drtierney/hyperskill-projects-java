package common

import flashcards.Main
import org.hyperskill.hstest.stage.StageTest
import org.hyperskill.hstest.testcase.CheckResult

abstract class FlashcardsStage4Test : StageTest<DialogClue>(Main::class.java) {

    override fun generate() = listOf(
            dialogTest(
                    anyLine(), user("2"),
                    anyLine(), user("black"),
                    anyLine(), user("white"),
                    anyLine(), user("black"),
                    containing("The card \"black\" already exists"),
                    user("red"),
                    anyLine(), user("white"),
                    containing("The definition \"white\" already exists"),
                    user("green"),
                    // asking
                    askCorrect("black", "white"),
                    askCorrect("red", "green")
            ),
            dialogTest(
                    inputNum(2),
                    inputCard("a brother of one's parent", "uncle"),
                    inputCard("a part of the body where the foot and the leg meet", "ankle"),
                    // asking
                    askWrong("a brother of one's parent", userAns = "ankle", rightAns = "uncle",
                            isDefFor = "a part of the body where the foot and the leg meet"),

                    askWrong("a part of the body where the foot and the leg meet", "???", "ankle")
            ),
            dialogTest(
                    inputNum(4),
                    inputCard("c1", "d1"),
                    inputCard("c2", "d2"),
                    inputCard("c3", "d3"),
                    // repeating three times
                    anyLine(),
                    user("c3"), containing("The card \"c3\" already exists"),
                    user("c2"), containing("The card \"c2\" already exists"),
                    user("c1"), containing("The card \"c1\" already exists"),
                    user("c4"),
                    anyLine(),
                    user("d2"), containing("The definition \"d2\" already exists"),
                    user("d3"), containing("The definition \"d3\" already exists"),
                    user("d1"), containing("The definition \"d1\" already exists"),
                    user("d4"),

                    askCorrect("c1", "d1"),
                    askWrong("c2", userAns = "d1", rightAns = "d2", isDefFor = "c1"),
                    askWrong("c3", "d3 ddd3", "d3"),
                    askWrong("c4", "???", "d4")
            )
    )

    /** Asks with a correct answer. */
    private fun askCorrect(quest: String, userAns: String) = compositePhrase {
        listOf(containing("\"$quest\"", hint = "This line should ask the definition of `$quest`."),
                user(userAns), containing("Correct answer"))
    }

    /** Asks with a wrong answer. */
    private fun askWrong(quest: String, userAns: String, rightAns: String, isDefFor: String? = null) = compositePhrase {
        val result = mutableListOf(
                containing("\"$quest\"", hint = "This line should ask the definition of `$quest`."),
                user(userAns))
        if (isDefFor == null) {
            result += containing("Wrong answer", "The correct one is \"$rightAns\"")
        } else {
            result += containing("Wrong answer", "The correct one is \"$rightAns\"",
                    "you've just written the definition of \"$isDefFor\"")
        }
        result
    }

    private fun inputNum(number: Int) = compositePhrase {
        listOf(anyLine(), user(number.toString()))
    }

    private fun inputCard(card: String, def: String) = compositePhrase {
        listOf(anyLine(), user(card), anyLine(), user(def))
    }


    override fun check(reply: String, clue: DialogClue): CheckResult {
        return clue.checkOutput(reply)
    }
}
