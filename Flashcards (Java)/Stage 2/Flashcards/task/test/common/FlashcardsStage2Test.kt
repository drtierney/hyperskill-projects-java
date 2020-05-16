package common

import flashcards.Main

import org.hyperskill.hstest.stage.StageTest
import org.hyperskill.hstest.testcase.CheckResult
import org.hyperskill.hstest.testcase.TestCase
import common.FlashcardsStage2Answer.RIGHT
import common.FlashcardsStage2Answer.WRONG

data class FlashcardsStage2Clue(
        val consoleInput: String,
        val answer: FlashcardsStage2Answer,
        val revealTest: Boolean = false
)

enum class FlashcardsStage2Answer(val word: String) {
    RIGHT("right"),
    WRONG("wrong");
}

abstract class FlashcardsStage2Test : StageTest<FlashcardsStage2Clue>(Main::class.java) {

    override fun generate(): List<TestCase<FlashcardsStage2Clue>> {
        return listOf(
                createTestCase("a purring animal\ncat\ncat\n", RIGHT, true),
                createTestCase("a purring animal\ncat\n????\n", WRONG, true),
                createTestCase("a barking animal\ndog\ncat\n", WRONG),
                createTestCase("a barking animal\ndog\ndog\n", RIGHT)
        )
    }

    override fun check(reply: String, clue: FlashcardsStage2Clue): CheckResult {
        val words = reply.lowerWords()

        val hasRight = RIGHT.word.toLowerCase() in words
        val hasWrong = WRONG.word.toLowerCase() in words

        if ((hasRight || !hasWrong) && clue.answer == WRONG) {
            return if (clue.revealTest) {
                CheckResult(
                        false,
                        revealRawTest(clue.consoleInput, reply) + "Expected the \"${WRONG.word}\" word."
                )
            } else {
                CheckResult(false)
            }
        }

        if ((hasWrong || !hasRight) && clue.answer == RIGHT) {
            return if (clue.revealTest) {
                CheckResult(
                        false,
                        revealRawTest(clue.consoleInput, reply) + "Expected the \"${RIGHT.word}\" word."
                )
            } else {
                CheckResult(false)
            }
        }

        return CheckResult(true)
    }

    companion object {
        private fun createTestCase(
                consoleInput: String,
                answer: FlashcardsStage2Answer,
                revealTest: Boolean = false
        ): TestCase<FlashcardsStage2Clue> {
            return TestCase<FlashcardsStage2Clue>()
                    .setInput(consoleInput)
                    .setAttach(FlashcardsStage2Clue(consoleInput, answer, revealTest))
        }
    }
}

fun revealRawTest(consoleInput: String, reply: String): String {
    return "Input:\n$consoleInput\nYour output:\n$reply\n\n"
}

fun String.lowerWords(): Set<String> {
    val lowerReply = this.trim().toLowerCase()

    val onlyLetters = lowerReply.map { if (it.isLetter()) it else ' ' }.joinToString("")

    return onlyLetters.split(" ").filter { it.isNotEmpty() }.toSet()
}


