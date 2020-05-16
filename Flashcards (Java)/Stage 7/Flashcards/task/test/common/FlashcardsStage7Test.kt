package common

import org.hyperskill.hstest.stage.StageTest
import org.hyperskill.hstest.testcase.CheckResult
import org.hyperskill.hstest.testcase.TestCase
import flashcards.Main
import java.io.File


abstract class FlashcardsStage7Test : StageTest<DialogClue>(Main::class.java) {

    override fun generate(): List<TestCase<DialogClue>> {
        File("capitals.txt").delete()
        File("capitalsNew.txt").delete()
        val tests = mutableListOf<TestCase<DialogClue>>()

        val capitalList = mutableListOf<Pair<String, String>>()
        fun capitals() = capitalList.toTypedArray()

        // wrappers for add/remove card, but saving cards to capitalList:
        fun addCapital(card: String, def: String) = addCard(card, def)
                .also { capitalList += card to def }

        fun removeCapital(card: String) = removeCard(card)
                .also { capitalList.removeIf { it.first == card } }

        // clear files
        tests += dialogTest(
                exportCards("capitals.txt", 0),
                exportCards("capitalsNew.txt", 0),
                exit()
        )
        tests += dialogTest(
                addCapital("France", "Paris"),
                addCapital("Russia", "Moscow"),
                askCards("France", "??", ""),
                exit(),
                exportArg(2),
                consoleArgs = arrayOf("-export", "capitals.txt")
        )
        tests += dialogTest(
                importArg(2, *capitals()),
                addCapital("Japan", "Tokyo"),
                askCards("Moscow", "Paris", "Tokyo"),
                exit(),
                exportArg(3),
                consoleArgs = arrayOf("-import", "capitals.txt", "-export", "capitalsNew.txt")
        )
        tests += dialogTest(
                importArg(3, *capitals()),
                askCards("Moscow", "Paris", "Tokyo"),
                removeCapital("Japan"),
                exit(),
                exportArg(2),
                consoleArgs = arrayOf("-export", "capitals.txt", "-import", "capitalsNew.txt")
        )
        tests += dialogTest(
                importArg(2, *capitals()),
                exit(),
                consoleArgs = arrayOf("-import", "capitals.txt")
        )

        return tests
    }

    override fun check(reply: String, clue: DialogClue): CheckResult {
        return clue.checkOutput(reply)
    }


    // ------ extensions for building a dialog: ------

    fun importArg(count: Int, vararg cards: Pair<String, String>) =
            containing("$count cards have been loaded",
                    updateContext = { ctx ->
                        cards.forEach { (card, def) ->
                            ctx.addCard(card, def)
                            ctx.wrongCards.removeAll(listOf(card))
                        }
                    })

    fun exportArg(count: Int) = containing("$count cards have been saved")


    fun inputAction(action: String) = compositePhrase {
        listOf(containing("action", hint = "This line should ask the action."), user(action))
    }

    inner class LogPhrase(val fileName: String) : Phrase {
        override fun toPhraseLines() = compositePhrase(
                inputAction("log"),
                anyLine(),
                user(fileName),
                OutputLine { text, ctx ->
                    val result = containing("saved", hint = "This line should indicate, that the log has been saved.").checker(text, ctx)
                    if (!result.isCorrect) {
                        return@OutputLine result
                    }
                    if (!File(fileName).exists()) {
                        return@OutputLine CheckResult.wrong("The log file $fileName does not exist.")
                    }
                    CheckResult.correct();
                }
        ).toPhraseLines()
    }

    private fun log(fileName: String) = LogPhrase(fileName)

    private fun resetStats() = compositePhrase(
            inputAction("reset stats"), containing("reset", hint = "This line should confirm card statistics reset.",
            updateContext = { ctx -> ctx.wrongCards.clear() })
    )

    /** Between tests we cache wrong answered capitals to check hardest cards, when we restore them from file. */
    private val wrongAnweredCapitals: MutableList<String> = mutableListOf()

    /** [customWrongCards] are used to load saved wrong cards from the previous test. */
    fun hardestCards(customWrongCards: List<String>? = null) = compositePhrase(
            inputAction("hardest card"),
            OutputLine { text, ctx ->
                if (customWrongCards != null) {
                    ctx.wrongCards.clear()
                    ctx.wrongCards.addAll(customWrongCards)
                }
                val groupedCards = ctx.wrongCards
                        .groupBy { it }.mapValues { (_, v) -> v.size }
                val maxMistakes = groupedCards.values.max() ?: 0
                val hardestCards = groupedCards.filterValues { it == maxMistakes }.keys.toList()

                when (hardestCards.size) {
                    0 -> return@OutputLine containing("There are no cards with errors").checker(text, ctx)
                    1 -> return@OutputLine containing("The hardest card is \"${hardestCards[0]}\"",
                            "$maxMistakes").checker(text, ctx)
                    else -> {
                        hardestCards.forEach { card ->
                            if (card !in text) {
                                return@OutputLine CheckResult.wrong("Your line `$text`\n" +
                                        "should contain the hardest cards " +
                                        "${hardestCards.joinToString("\", \"", "\"", "\"")} with $maxMistakes mistakes.")
                            }
                        }
                        val numberOfHardestCards = text.count { it == '"' }
                        if (numberOfHardestCards != hardestCards.size * 2) {
                            return@OutputLine CheckResult.wrong("Your line `$text`\n" +
                                    "contains more hardest cards, than expected. Expected: $hardestCards.")
                        }
                        if (maxMistakes.toString() !in text) {
                            if (numberOfHardestCards != hardestCards.size) {
                                return@OutputLine CheckResult.wrong("Your line `$text`\n" +
                                        "should contain $maxMistakes mistakes for your hardest cards.")
                            }
                        }
                    }
                }
                CheckResult.correct();
            }
    )


    // extend dialog context with our own data:

    @Suppress("UNCHECKED_CAST")
    private val Context.cardToDef
        get() = rawData.getOrPut("cardToDef") { mutableMapOf<String, String>() } as MutableMap<String, String>

    @Suppress("UNCHECKED_CAST")
    /** All cards, that were answered wrong. */
    private val Context.wrongCards
        get() = rawData.getOrPut("wrongCards") { mutableListOf<String>() } as MutableList<String>


    @Suppress("UNCHECKED_CAST")
    private val Context.defToCard
        get() = rawData.getOrPut("defToCard") { mutableMapOf<String, String>() } as MutableMap<String, String>

    private fun Context.addCard(card: String, definition: String) {
        cardToDef[card] = definition
        defToCard[definition] = card
    }

    private fun Context.removeCard(card: String) {
        val def = cardToDef.remove(card)
        if (def != null) {
            defToCard.remove(def)
        }
    }


    private fun addCard(card: String, def: String) = compositePhrase(
            inputAction("add"),
            anyLine(), user(card),
            anyLine(), user(def),
            containing("has been added",
                    hint = "This line should add the pair (\"$card\":\"$def\").",
                    updateContext = { ctx -> ctx.addCard(card, def) })
    )


    private fun existingDef(card: String, def: String) = compositePhrase(
            inputAction("add"),
            anyLine(), user(card),
            anyLine(), user(def),
            containing("definition", "exists", hint = "This line should reject the existing definition `$def`.")
    )


    private fun existingCard(card: String) = compositePhrase(
            inputAction("add"),
            anyLine(), user(card),
            containing("card", "exists", hint = "This line should reject the existing card `$card`.")
    )

    private fun removeCard(card: String) = compositePhrase(
            inputAction("remove"),
            anyLine(), user(card),
            containing("has been removed", hint = "This line should remove the card `$card`.",
                    updateContext = { ctx -> ctx.removeCard(card) })
    )

    private fun removeNonExisting(card: String) = compositePhrase(
            inputAction("remove"),
            anyLine(), user(card),
            containing("Can't remove \"$card\"", hint = "This line should reject removing non existing card `$card`.")
    )


    private fun importCards(fileName: String, vararg cards: Pair<String, String>) = compositePhrase(
            inputAction("import"),
            anyLine(), user(fileName),
            containing("${cards.size} cards have been loaded",
                    updateContext = { ctx ->
                        val cardToDef = ctx.cardToDef
                        val defToCard = ctx.defToCard
                        cards.forEach { (card, def) ->
                            if (card in cardToDef) {
                                defToCard.remove(cardToDef[card]) // erase wrong reverse link
                            }
                            ctx.addCard(card, def) // with update!!
                        }
                    })
    )

    private fun importNonExisting(fileName: String) = compositePhrase(
            inputAction("import"),
            anyLine(), user(fileName),
            containing("not found", hint = "This line should say, that the file $fileName does not exist.")
    )

    private fun exportCards(fileName: String, cardsSize: Int) = compositePhrase(
            inputAction("export"),
            anyLine(), user(fileName),
            containing("$cardsSize cards have been saved")
    )

    private fun exit() = compositePhrase(
            inputAction("exit"),
            anyLine()
    )

    /** Perform ask action. [ansDefs] are our test answers with definitions.
     * We don't know are they wrong or correct, because the test is random. */
    private fun askCards(vararg ansDefs: String, saveWrongAnsweredCapitals: Boolean = false) = compositePhrase {
        val startPhrases = listOf(
                inputAction("ask"),
                anyLine(), user(ansDefs.size.toString())
        )
        // here we add the dialog logic, by creating base OutputLine class with a custom checker
        // we use context to connect with neighbor checkers

        // iterate test answered definitions:
        val repeatingPhrases = ansDefs.map { ansDef ->
            compositePhrase(
                    OutputLine { text, ctx ->
                        val askedCard = text.dropWhile { it != '"' }.dropLastWhile { it != '"' }.trim('"')
                        if (askedCard.isEmpty()) {
                            return@OutputLine CheckResult.wrong("Not found card in quotes. " +
                                    "This line should ask the definition of a random card.")
                        }
                        if (askedCard !in ctx.cardToDef) {
                            return@OutputLine CheckResult.wrong("You asked the definition of the non existing card: `$askedCard`.")
                        }
                        ctx.rawData["lastAskedCard"] = askedCard
                        CheckResult.correct();
                    },
                    // unfortunately we can't access the Context in user action, see documentation of user()
                    user(ansDef),
                    // check the answer:
                    OutputLine { text, ctx ->
                        val askedCard = ctx.rawData["lastAskedCard"]?.toString()
                                ?: throw IllegalStateException("Not found lastAskedCard in the `ask` checker.")
                        val cardToDef = ctx.cardToDef
                        val defToCard = ctx.defToCard
                        val rightAns = cardToDef[askedCard]

                        val hint = "The asked card was `$askedCard`, the answer was `$ansDef`."
                        if (cardToDef[askedCard] == ansDef)
                            containing("Correct answer", hint = hint).checker(text, ctx)
                        else {
                            ctx.wrongCards += askedCard

                            val isDefFor = defToCard[ansDef]
                            if (isDefFor != null) {
                                containing("Wrong answer", "The correct one is \"$rightAns\"",
                                        "you've just written the definition of \"$isDefFor\"", hint = hint).checker(text, ctx)
                            } else {
                                // should not contain definition hint!!
                                if (text.contains("you've just written the definition of")) {
                                    CheckResult.wrong("Your line\n`$text`\nshould NOT contain " +
                                            "`you've just written the definition of`.\n$hint")
                                } else {
                                    containing("Wrong answer", "The correct one is \"$rightAns\"", hint = hint).checker(text, ctx)
                                }

                            }
                        }.also {
                            // only for these tests. To test restoring wrong capitals from file.
                            if (saveWrongAnsweredCapitals) {
                                wrongAnweredCapitals.clear()
                                wrongAnweredCapitals.addAll(ctx.wrongCards)
                            }
                        }
                    }
            )
        }
        (startPhrases + repeatingPhrases)
    }
}
