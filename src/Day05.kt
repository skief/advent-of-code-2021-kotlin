import kotlin.math.*

fun main() {
    fun crossLines(lines: List<List<List<Int>>>): Int {
        val diag = lines.filter { it[0][0] != it[1][0] && it[0][1] != it[1][1] }

        val horCoords = lines
            .filter { it[0][0] != it[1][0] && it[0][1] == it[1][1] }
            .map { (min(it[0][0], it[1][0])..max(it[0][0], it[1][0])).map { x -> Pair(x, it[0][1]) } }
            .flatten()

        val vertCoords = lines
            .filter { it[0][0] == it[1][0] && it[0][1] != it[1][1] }
            .map { (min(it[0][1], it[1][1])..max(it[0][1], it[1][1])).map { y -> Pair(it[0][0], y) } }
            .flatten()

        val diags = diag.map { if (it[1][0] < it[0][0]) listOf(it[1], it[0]) else it }
            .partition { it[0][1] > it[1][1] }

        val upDiag = diags.first
            .map { (0..(it[1][0] - it[0][0])).map { i -> Pair(it[0][0] + i, it[0][1] - i) } }
            .flatten()

        val downDiag = diags.second
            .map { (0..(it[1][0] - it[0][0])).map { i -> Pair(it[0][0] + i, it[0][1] + i) } }
            .flatten()

        val coords = horCoords + vertCoords + upDiag + downDiag

        return coords.groupingBy { it }.eachCount().count { it.value > 1 }
    }

    fun part1(input: List<String>): Int {
        val inp = input.map { it.split(" -> ").map { it.split(",").map { it.toInt() } } }
        val valid = inp.filter { (it[0][0] != it[1][0]) xor (it[0][1] != it[1][1]) }

        return crossLines(valid)
    }

    fun part2(input: List<String>): Int {
        val inp = input.map { it.split(" -> ").map { it.split(",").map { it.toInt() } } }

        return crossLines(inp)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 12)
    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}