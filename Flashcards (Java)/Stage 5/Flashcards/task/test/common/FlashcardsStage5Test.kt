package common

import org.hyperskill.hstest.stage.StageTest
import org.hyperskill.hstest.testcase.CheckResult
import org.hyperskill.hstest.testcase.TestCase
import flashcards.Main

abstract class FlashcardsStage5Test : StageTest<DialogClue>(Main::class.java) {

    // how to remove files? Now at least rewrite in the first test:
    override fun generate(): List<TestCase<DialogClue>> {
        val tests = mutableListOf<TestCase<DialogClue>>()
        // old tests:
        tests += dialogTest(
                addCard("black", "white"),
                existingCard("black"),
                existingDef("red", "white"),
                askCards("white", "green"), // the ask order is random
                exit()
        )
        tests += dialogTest(
                addCard("a brother of one's parent", "uncle"),
                addCard("a part of the body where the foot and the leg meet", "ankle"),
                askCards("ankle", "??", "uncle", "ankle", "??", "uncle"), // the ask order is random
                exit()
        )
        // stage 5 tests:

        val capitalList = mutableListOf<Pair<String, String>>()
        fun capitals() = capitalList.toTypedArray()

        // wrappers for add/remove card, but saving cards to capitalList:
        fun addCapital(card: String, def: String) = addCard(card, def)
                .also { capitalList += card to def }

        fun removeCapitalCard(card: String) = removeCard(card)
                .also { capitalList.removeIf { it.first == card } }

        // testing files
        // clear files
        tests += dialogTest(
                exportCards("capitals.txt", 0),
                exportCards("capitalsNew.txt", 0),
                exit()
        )
        tests += dialogTest(
                importNonExisting("ghost_file.txt"),
                exit()
        )
        tests += dialogTest(
                addCapital("Great Britain", "London"),
                removeNonExisting("Wakanda"),
                exportCards("capitals.txt", 1),
                importCards("capitals.txt", *capitals()),
                askCards("London"),
                exportCards("capitalsNew.txt", 1),
                exit()
        )
        tests += dialogTest(
                importCards("capitalsNew.txt", *capitals()), // import checks only the amount of cards
                exit()
        )
        tests += dialogTest(
                importCards("capitalsNew.txt", *capitals()),
                addCapital("France", "Paris"),
                addCapital("Russia", "Moscow"),
                removeCapitalCard("Great Britain"),
                exportCards("capitalsNew.txt", 2),
                importCards("capitalsNew.txt", *capitals()),
                askCards("London", "Paris", "Moscow", "Paris"),
                exit()
        )
        // check merge with file import
        tests += dialogTest(
                addCard("Japan", "Tokyo"), // should be merged
                addCard("France", "UpdateMeFromImport"), // should be updated from import file
                addCard("Russia", "UpdateMeFromImport2"), // should be updated from import file
                importCards("capitalsNew.txt", *capitals()),
                askCards("Tokyo", "Paris", "Moscow"),
                removeCard("Japan"),
                removeCapitalCard("Russia"),
                exportCards("capitalsNew.txt", 1), // only France left
                exit()
        )

        // check reverse map while merge
        tests += dialogTest(
                addCard("France", "UpdateMeFromImport"), // should be updated from import file
                importCards("capitalsNew.txt", *capitals()),
                askCards("UpdateMeFromImport"), // check that we removed from reverse map
                exit()
        )
        
        // check remove, add and ask:
        tests += dialogTest(
                addCard("a", "1"),
                addCard("b", "2"),
                addCard("c", "3"),
                existingCard("b"),
                existingCard("c"),
                addCard("d", "4"),
                removeCard("c"),
                removeNonExisting("xxxx"),
                addCard("c", "5"),
                existingDef("new card", "4"),
                existingDef("f", "5"),
                removeCard("c"),
                removeCard("d"), // left only a and b
                askCards("1", "2", "3", "4", "3", "2", "1"), // try to fit random
                askCards("2"),
                exit()
        )
        return tests
    }

    override fun check(reply: String, clue: DialogClue): CheckResult {
        return clue.checkOutput(reply)
    }


    // ------ extensions for building a dialog: ------

    fun inputAction(action: String) = compositePhrase {
        listOf(containing("action", hint = "This line should ask the action."), user(action))
    }

    // extend dialog context with our own data:

    @Suppress("UNCHECKED_CAST")
    private val Context.cardToDef
        get() = rawData.getOrPut("cardToDef") { mutableMapOf<String, String>() } as MutableMap<String, String>

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
    private fun askCards(vararg ansDefs: String) = compositePhrase {
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
                        val askedCard = ctx.rawData["lastAskedCard"]
                                ?: throw IllegalStateException("Not found lastAskedCard in the `ask` checker.")
                        val cardToDef = ctx.cardToDef
                        val defToCard = ctx.defToCard
                        val rightAns = cardToDef[askedCard]

                        val hint = "The asked card was `$askedCard`, the answer was `$ansDef`."
                        if (cardToDef[askedCard] == ansDef)
                            containing("Correct answer", hint = hint).checker(text, ctx)
                        else {
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
                        }
                    }
            )
        }
        (startPhrases + repeatingPhrases)
    }

}

