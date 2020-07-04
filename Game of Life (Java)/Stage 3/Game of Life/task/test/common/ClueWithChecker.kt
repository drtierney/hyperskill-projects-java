package common


import org.hyperskill.hstest.v6.testcase.CheckResult
import org.hyperskill.hstest.v6.testcase.TestCase

sealed class ClueWithChecker(val input: String) {
    fun toTestCase(): TestCase<ClueWithChecker> {
        return TestCase<ClueWithChecker>().setInput(input).setAttach(this)
    }
}

class ClueWithPredefinedFeedbackChecker(
        val predefinedFeedback: String,
        input: String,
        val checker: (String) -> Boolean
) : ClueWithChecker(input)

class ClueWithDynamicFeedbackChecker(
        input: String,
        val checker: (String) -> CheckResult
) : ClueWithChecker(input)

fun createDynamicFeedbackTest(input: String = "", checker: (String) -> CheckResult): TestCase<ClueWithChecker> {
    return ClueWithDynamicFeedbackChecker(
            input = input,
            checker = checker
    ).toTestCase()
}

fun checkClueWithCheckerTest(reply: String, clue: ClueWithChecker): CheckResult {
    return try {
        when (clue) {
            is ClueWithDynamicFeedbackChecker -> clue.checker(reply)
            is ClueWithPredefinedFeedbackChecker -> CheckResult(clue.checker(reply), clue.predefinedFeedback)
        }
    } catch (e: AssertionError) {
        if (clue is ClueWithPredefinedFeedbackChecker) {
            fail(clue.predefinedFeedback)
        } else {
            CheckResult.FALSE
        }
    }
}
