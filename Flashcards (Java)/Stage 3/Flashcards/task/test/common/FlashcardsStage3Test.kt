package common


import org.hyperskill.hstest.stage.StageTest
import org.hyperskill.hstest.testcase.CheckResult
import org.hyperskill.hstest.testcase.TestCase
import flashcards.Main

class CardsClue(
        val cardCount: Int,
        val cards: List<String>,
        val defs: List<String>,
        val answers: List<String>
) {
    init {
        require(cardCount == cards.size)
        require(cards.size == defs.size)
        require(defs.size == answers.size)
    }

    fun makeText() = listOf(
            listOf(cardCount.toString()),
            cards.zip(defs).flatMap { (c, d) -> listOf(c, d) },
            answers
    )
            .flatten()
            .joinToString("\n", postfix = "\n")
}

abstract class FlashcardsStage3Test : StageTest<CardsClue>(Main::class.java) {

    override fun generate() = listOf(
            CardsClue(2,
                    cards = listOf("black", "white"),
                    defs = listOf("white", "black"),
                    answers = listOf("white", "blue")
            ),
            CardsClue(5,
                    cards = listOf("a", "2", "3", "4", "5"),
                    defs = listOf("a", "2", "3", "4", "5"),
                    answers = listOf("a", "2", "3", "4", "5")
            ),
            CardsClue(5,
                    cards = listOf("1", "2", "3", "4", "5"),
                    defs = listOf("1", "2", "3", "4", "5"),
                    answers = listOf("5", "4", "3", "2", "1")
            ),
            CardsClue(4,
                    cards = listOf("11", "12", "13", "14"),
                    defs = listOf("21", "22", "23", "24"),
                    answers = listOf("21", "22", "333333", "34")
            ),
            CardsClue(2,
                    cards = listOf("a brother of one's parent", "a part of the body where the foot and the leg meet"),
                    defs = listOf("uncle", "ankle"),
                    answers = listOf("ankle", "??")
            )

    ).map { clue ->
        TestCase<CardsClue>()
                .setInput(clue.makeText())
                .setAttach(clue)
    }

    override fun check(reply: String, clue: CardsClue): CheckResult {
        val userLines = reply.lines().filter { it.isNotEmpty() }


        if (userLines.size < clue.cardCount * 2) {
            return CheckResult.wrong("Your output should contain at least ${clue.cardCount * 2} lines, but contains only ${userLines.size}. " +
                    "Check, that you output your lines with println, not print.")
        }

        val askLines = userLines.takeLast(clue.cardCount * 2)
        println("askLines:\n${askLines.joinToString("\n")}")


        val questToAns = askLines.windowed(2, 2)
        for (i in 0 until clue.cardCount) {
            val (quest, feedback) = questToAns[i]
            val card = clue.cards[i]
            val def = clue.defs[i]
            val ans = clue.answers[i] // user answer

            if (!quest.contains("\"$card\"")) {
                return CheckResult.wrong("Question \"$quest\" should contain the card \"$card\" in quotes.")
            }

            if (ans == def) {
                // should be correct
                if (!feedback.toLowerCase().contains("correct answer")) {
                    return CheckResult.wrong("Feedback \"$feedback\" should be positive for card \"$card\" and answer \"$ans\".")
                }
            } else {
                // should be wrong
                if (!feedback.toLowerCase().contains("wrong answer") || !feedback.contains("\"$def\"")) {
                    return CheckResult.wrong("Feedback \"$feedback\" should be negative for the card \"$card\" " +
                            "and contain the right definition \"$def\" in quotes.")
                }
            }

        }

        return CheckResult.correct()
    }
}
