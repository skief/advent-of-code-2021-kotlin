fun main() {
    fun part1(input: List<String>): Long {
        val playerPos = input.map { it.split(": ")[1].toInt() }.toMutableList()
        val playerPoints = mutableListOf(0L, 0L)

        var turn = 0
        var rolled = 0

        while (playerPoints.none { it >= 1000 }){
            var roll = 0
            for (i in 1..3){
                roll += rolled % 100 + 1
                rolled++
            }
            playerPos[turn] = (playerPos[turn] + roll - 1) % 10 + 1
            playerPoints[turn] += (playerPos[turn]).toLong()

            turn = (turn + 1) % 2
        }

        return rolled * playerPoints.minOf { it }
    }

    data class GameState(var p1Pos: Int, var p1Points: Int, var p2Pos: Int, var p2Points: Int)

    fun part2(input: List<String>): ULong {
        val playerPos = input.map { it.split(": ")[1].toInt() }.toMutableList()

        val possibleRolls = mutableMapOf(
            3 to 1,
            4 to 3,
            5 to 6,
            6 to 7,
            7 to 6,
            8 to 3,
            9 to 1)

        var universes = mutableMapOf(GameState(playerPos[0], 0, playerPos[1], 0) to 1UL)
        var p1Win = 0UL
        var p2Win = 0UL

        while (universes.isNotEmpty()){
            val newUniverses = mutableMapOf<GameState, ULong>()

            for ((state, count) in universes){
                for ((p1Roll, p1RollCount) in possibleRolls){
                    val p1Pos = (state.p1Pos + p1Roll - 1) % 10 + 1
                    val p1Points = state.p1Points + p1Pos

                    if (p1Points >= 21){
                        p1Win += count * p1RollCount.toULong()
                        continue
                    }

                    for ((p2Roll, p2RollCount) in possibleRolls){
                        val p2Pos = (state.p2Pos + p2Roll - 1) % 10 + 1
                        val p2Points = state.p2Points + p2Pos

                        if (p2Points >= 21){
                            p2Win += count * p1RollCount.toULong() * p2RollCount.toULong()
                            continue
                        }

                        val newState = GameState(p1Pos, p1Points, p2Pos, p2Points)
                        newUniverses[newState] = newUniverses.getOrDefault(newState, 0UL) +
                                count * p1RollCount.toULong() * p2RollCount.toULong()
                    }
                }
            }
            universes = newUniverses
        }

        return maxOf(p1Win, p2Win)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day21_test")
    check(part1(testInput) == 739785L)
    check(part2(testInput) == 444356092776315UL)

    val input = readInput("Day21")
    println(part1(input))
    println(part2(input))
}