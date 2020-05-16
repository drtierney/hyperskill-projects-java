package common

import org.hyperskill.hstest.testcase.CheckResult
import org.hyperskill.hstest.testcase.TestCase


// ---- DialogClue ----

/** May be several lines or just one. */
interface Phrase {
    fun toPhraseLines(): List<PhraseLine>
}

/** list of input and output lines, combined together. */
fun compositePhrase(block: () -> List<Phrase>) = object : Phrase {
    override fun toPhraseLines() = block().flatMap { it.toPhraseLines() }
}

fun compositePhrase(vararg phrases: Phrase) = object : Phrase {
    override fun toPhraseLines() = phrases.flatMap { it.toPhraseLines() }
}

/** Either user (input) or output phrase. Each phrase should be a line. */
sealed class PhraseLine : Phrase {
    override fun toPhraseLines() = listOf(this)
}

/** Some mutable data, that is passed across dialog phrases. */
class Context {
    val rawData = mutableMapOf<String, Any>()
}

class UserLine(val text: String, val updateContext: CtxUpdate = {}) : PhraseLine() {

}
typealias CtxUpdate = (ctx: Context) -> Unit

class OutputLine(val checker: (text: String, ctx: Context) -> CheckResult) : PhraseLine()

/** This function creates a line with user input (our test input).
 *
 * Unfortunately we can't access the Context, when we adding user text.
 * This occurs because of HS test framework limitations:
 * we need to pass all inputs first, and then start checking outputs. */
fun user(text: String, updateContext: (ctx: Context) -> Unit = {}) = UserLine(text, updateContext)

fun anyLine(updateContext: CtxUpdate = {}) = OutputLine { _, ctx -> CheckResult.correct().also { updateContext(ctx) } }

fun containing(
        vararg parts: String,
        ignoreCase: Boolean = true,
        hint: String? = null,
        updateContext: CtxUpdate = {}
) = OutputLine { line, context ->
    fun buildFeedback(): String {
        val feedback = StringBuilder()
        feedback.append("Your line\n`$line`\nshould contain ${parts.joinToString("`, `", "`", "`")}")
        if (ignoreCase) {
            feedback.append(" (ignoring case)")
        }
        feedback.append(".")
        if (hint != null) {
            feedback.append("\n$hint")
        }
        return feedback.toString()
    }

    var startIndex = 0
    for (part in parts) {
        startIndex = line.indexOf(part, startIndex, ignoreCase)
        if (startIndex == -1) {
            return@OutputLine CheckResult.wrong(buildFeedback())
        }
    }
    updateContext(context) // everything is correct, update context
    CheckResult.correct();
}

class DialogClue(private val phrases: List<PhraseLine>) {

    private val inputPhrases = phrases.filter { it is UserLine }.map { it as UserLine }
    private val outputPhrases = phrases.filter { it is OutputLine }.map { it as OutputLine }

    fun generateInput() = inputPhrases
            .joinToString("\n", postfix = "\n") { it.text }

    fun checkOutput(output: String): CheckResult {
        val lines = output.lines()
                .filter { it.isNotBlank() }

        fun wrongOutputSizeFeedback() = CheckResult.wrong("The number of lines in your output is ${lines.size}, " +
                "but it should be ${outputPhrases.size}. " +
                "Check, that you output your lines with println, not print. And there are no extra outputs.")

// here we store some mutable data from phrase to phrase
        val context = Context()

        val lineIter = lines.listIterator()
        phrases.forEach { phraseLine ->
            when (phraseLine) {
                is UserLine -> phraseLine.updateContext(context)
                is OutputLine -> {
                    if (!lineIter.hasNext()) {
                        return wrongOutputSizeFeedback()
                    }
                    val result = phraseLine.checker(lineIter.next(), context)
                    if (!result.isCorrect) {
                        return CheckResult.wrong(result.feedback)
                    }
                }
            }
        }

        if (lineIter.hasNext()) {
            return wrongOutputSizeFeedback()
        }

        return CheckResult.correct();
    }
}

fun dialogTest(vararg phrases: Phrase, consoleArgs: Array<String> = emptyArray()): TestCase<DialogClue> {
    val dialogClue = DialogClue(phrases.flatMap { it.toPhraseLines() })
    return TestCase<DialogClue>()
            .setInput(dialogClue.generateInput())
            .setAttach(dialogClue)
            .addArguments(*consoleArgs)
}
