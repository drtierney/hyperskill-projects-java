package common

import flashcards.Main
import org.hyperskill.hstest.stage.StageTest
import org.hyperskill.hstest.testcase.CheckResult
import org.hyperskill.hstest.testcase.TestCase

abstract class FlashcardsStage1Test : StageTest<Nothing?>(Main::class.java) {

    override fun generate(): List<TestCase<Nothing?>> {
        return listOf(
                TestCase()
        )
    }

    override fun check(reply: String, clue: Nothing?): CheckResult {
        val lines = reply.lines().filter { it.isNotEmpty() }

        if (lines.size != LINE_COUNT) {
            return CheckResult(
                    false,
                    "Your program prints ${lines.size} ${lineOrLines(lines.size)}.\n" +
                            "$LINE_COUNT lines were expected."
            )
        }

        val firstLine = lines[0].trim()

        if (firstLine != FIRST_LINE) {
            return CheckResult(
                    false,
                    "Your first line is \"$firstLine\" but \"$FIRST_LINE\" was expected."
            )
        }

        val thirdLine = lines[2].trim()

        if (thirdLine != THIRD_LINE) {
            return CheckResult(
                    false,
                    "Your third line is \"$thirdLine\" but \"$THIRD_LINE\" was expected."
            )
        }

        return CheckResult(true)
    }

    private fun lineOrLines(lineCount: Int): String {
        if (lineCount == 1) {
            return "line"
        }
        return "lines"
    }

    companion object {
        private const val LINE_COUNT = 4
        private const val FIRST_LINE = "Card:"
        private const val THIRD_LINE = "Definition:"
    }
}
