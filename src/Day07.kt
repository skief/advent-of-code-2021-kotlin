import kotlin.math.*

fun main() {
    fun part1(input: List<String>): Int {
        val crabs = input[0].split(",").map { it.toInt() }

        val median = crabs.sorted()[crabs.size / 2]

        return crabs.sumOf { abs(median - it) }
    }

    fun part2(input: List<String>): Int {
        val crabs = input[0].split(",").map { it.toInt() }

        val avg = crabs.average()
        val lAvg = floor(avg).toInt()
        val hAvg = ceil(avg).toInt()

        val mi = crabs.sumOf { (abs(lAvg - it) * (abs(lAvg - it) + 1)) / 2 }
        val ma = crabs.sumOf { (abs(hAvg - it) * (abs(hAvg - it) + 1)) / 2 }

        return min(mi, ma)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 37)
    check(part2(testInput) == 168)
    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}