fun main() {
    fun countAscending(input: List<String>, windowSize: Int): Int{
        return input
            .map(String::toInt)
            .windowed(size = windowSize, step = 1)
            .map { w->w.sum() }
            .zipWithNext()
            .filter { (i, j) -> i < j }
            .size
    }

    fun part1(input: List<String>): Int {
        return countAscending(input, 1)
    }

    fun part2(input: List<String>): Int {
        return countAscending(input, 3)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 7)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
