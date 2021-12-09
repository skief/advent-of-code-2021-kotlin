import kotlin.math.abs
import kotlin.math.exp

fun main() {
    fun part1(input: List<String>): Int {
        val width = input[0].length
        val height = input.size

        val inp = input.map { listOf(9) + it.toCharArray().map { it.toString().toInt() } + listOf(9) }
        val paddedInp = listOf(List(width + 2) { 9 }) + inp + listOf(List(width + 2) {9})

        val positions = (1..width).map { w -> (1..height).map { h -> listOf(
            paddedInp[h][w],
            paddedInp[h - 1][w],
            paddedInp[h + 1][w],
            paddedInp[h][w - 1],
            paddedInp[h][w + 1]
        ) } }.flatten()

        return positions.filter { it.subList(1, it.size).minOrNull() !!> it[0] }.sumOf { 1 + it[0] }
    }

    fun expand(paddedInput: List<List<Int>>, points: MutableSet<Pair<Int, Int>>): MutableSet<Pair<Int, Int>> {
        val neighbors = points.map { p -> (
            listOf( Pair(p.first - 1, p.second),
                Pair(p.first + 1, p.second),
                Pair(p.first, p.second - 1),
                Pair(p.first, p.second + 1))
                .filter {  0 < it.first && it.first < paddedInput[0].size - 1 && 0 < it.second && it.second < paddedInput.size - 1 }
                .filter { paddedInput[it.second][it.first] > paddedInput[p.second][p.first] && paddedInput[it.second][it.first] != 9 }
                .toMutableSet()
        ) }.flatten()

        val result = mutableSetOf<Pair<Int, Int>>()
        result.addAll(points)
        result.addAll(neighbors)

        return result
    }

    fun part2(input: List<String>): Int {
        val width = input[0].length
        val height = input.size

        val inp = input.map { listOf(9) + it.toCharArray().map { it.toString().toInt() } + listOf(9) }
        val paddedInp = listOf(List(width + 2) { 9 }) + inp + listOf(List(width + 2) {9})

        val positions = (1..width).map { w -> (1..height).map { h -> Pair(Pair(w, h), listOf(
            paddedInp[h][w],
            paddedInp[h - 1][w],
            paddedInp[h + 1][w],
            paddedInp[h][w - 1],
            paddedInp[h][w + 1]
        )) } }.flatten()

        val lowPoints = positions.filter { it.second.subList(1, it.second.size).minOrNull() !!> it.second[0] }
            .map { mutableSetOf(it.first) }

        val basins = generateSequence(Pair(listOf<MutableSet<Pair<Int, Int>>>(), lowPoints)) {
            points -> Pair(points.second, points.second.map { expand(paddedInp, it) } )
        }.takeWhile { it.first != it.second }
            .last()
            .second
            .map { it.size }
            .sortedDescending()

        return basins[0] * basins[1] * basins[2]
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 1134)
    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}
