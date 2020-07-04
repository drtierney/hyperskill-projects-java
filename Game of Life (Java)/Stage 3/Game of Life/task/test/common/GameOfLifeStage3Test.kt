package common


import org.hyperskill.hstest.v6.stage.BaseStageTest
import org.hyperskill.hstest.v6.testcase.CheckResult
import org.hyperskill.hstest.v6.testcase.TestCase
import life.Main

abstract class GameOfLifeStage3Test : BaseStageTest<ClueWithChecker>(Main::class.java) {

    override fun generate(): List<TestCase<ClueWithChecker>> {
        // two times:
        return (5..MAX_SIZE).flatMap(::stage3Tests) + (5..MAX_SIZE).flatMap(::stage3Tests)
    }

    override fun check(reply: String, clue: ClueWithChecker): CheckResult {
        return checkClueWithCheckerTest(reply = reply, clue = clue)
    }

    companion object {
        const val MAX_SIZE = 10
    }
}

fun stage3Tests(universeSize: Int): List<TestCase<ClueWithChecker>> {
    return listOf(
            createDynamicFeedbackTest("$universeSize\n") { reply ->
                @Suppress("FoldInitializerAndIfToElvis")
                val generations = reply
                        .toLowerCase()
                        .lines()
                        .filter { it.isNotEmpty() }
                        .joinToString("\n")
                        .split("generation".toRegex())
                        .drop(1)
                        .map {
                            val lines = it.lines()
                            val (idString, aliveString) = lines
                            val map = lines.drop(2)

                            if (!map.canBeMap(universeSize)) {
                                return@createDynamicFeedbackTest fail("Can't read map in:\n$it\n")
                            }

                            val id = intRegex.find(idString)?.groupValues?.firstOrNull()?.toInt()
                            if (id == null) {
                                return@createDynamicFeedbackTest fail("Can't find generation ID:\n$it\n")
                            }

                            val alive = intRegex.find(aliveString)?.groupValues?.firstOrNull()?.toInt()
                            if (alive == null) {
                                return@createDynamicFeedbackTest fail("Can't find alive count:\n$it\n")
                            }

                            val generation = Generation(id, alive, map)
                            val aliveOnMap = generation.aliveOnMap(universeSize)

                            if (aliveOnMap != alive) {
                                return@createDynamicFeedbackTest fail(
                                        "Alive count is wrong ($alive expected, $aliveOnMap got):\n$it\n"
                                )
                            }

                            return@map generation
                        }

                if (generations.isEmpty()) {
                    return@createDynamicFeedbackTest fail("Your program doesn't output generations for size $universeSize")
                }

                if (generations.first().id != 1) {
                    return@createDynamicFeedbackTest fail("First generation has number ${generations.first().id}")
                }

                val diffs = generations.windowed(2)

                diffs.forEach { (prev, next) ->
                    if (prev.id + 1 != next.id) {
                        return@createDynamicFeedbackTest fail("ID of the generation #${prev.id + 1} is wrong (${next.id})")
                    }

                    if (!prev.evolvesTo(next, universeSize)) {
                        return@createDynamicFeedbackTest fail("Wrong evolution from #${prev.id} to #${prev.id + 1}")
                    }
                }

                return@createDynamicFeedbackTest CheckResult.TRUE
            }
    )
}

val intRegex = """(\d)+""".toRegex()

fun List<String>.canBeMap(universeSize: Int): Boolean {
    if (this.size < universeSize) {
        return false
    }

    return this.take(universeSize).all { it.length >= universeSize }
}

data class Generation(val id: Int, val alive: Int, val map: List<String>) {
    fun aliveOnMap(universeSize: Int): Int {
        return (0 until universeSize)
                .flatMap { y -> (0 until universeSize).map { x -> x to y } }
                .map { (x, y) -> map[x][y] }
                .count { it.isAlive() }
    }

    fun evolvesTo(other: Generation, universeSize: Int): Boolean {
        return (0 until universeSize)
                .flatMap { y -> (0 until universeSize).map { x -> x to y } }
                .map { (x, y) ->
                    if (map[x][y].isAlive()) {
                        if (aliveNeighbours(x, y, universeSize) in setOf(2, 3)) {
                            other.map[x][y].isAlive()
                        } else {
                            !other.map[x][y].isAlive()
                        }
                    } else {
                        if (aliveNeighbours(x, y, universeSize) == 3) {
                            other.map[x][y].isAlive()
                        } else {
                            !other.map[x][y].isAlive()
                        }
                    }
                }
                .all { it }
    }

    fun aliveNeighbours(x: Int, y: Int, universeSize: Int): Int {
        var north = x - 1
        if (north < 0) north = universeSize - 1
        var south = x + 1
        if (south > universeSize - 1) south = 0
        var west = y - 1
        if (west < 0) west = universeSize - 1
        var east = y + 1
        if (east > universeSize - 1) east = 0

        var aliveNeighbours = 0
        if (map[north][west].isAlive()) ++aliveNeighbours
        if (map[north][y].isAlive()) ++aliveNeighbours
        if (map[north][east].isAlive()) ++aliveNeighbours
        if (map[x][west].isAlive()) ++aliveNeighbours
        if (map[x][east].isAlive()) ++aliveNeighbours
        if (map[south][west].isAlive()) ++aliveNeighbours
        if (map[south][y].isAlive()) ++aliveNeighbours
        if (map[south][east].isAlive()) ++aliveNeighbours

        return aliveNeighbours
    }

    fun Char.isAlive(): Boolean = this != ' '
}
