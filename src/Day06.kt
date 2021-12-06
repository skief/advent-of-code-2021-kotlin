fun main() {
    fun fishSimulator(input: List<String>, days: Int): ULong {
        val inp = input[0].split(",").map { it.toInt() }
        val fishes = (0..8).map { num -> inp.count { it == num }.toULong() }

        return generateSequence(fishes) {
            (it.slice(1..8) + it.slice(0..0))
                .mapIndexed { index, value -> if (index != 6) value else value + it[0] }
        }.take(days + 1).last().sum()
    }

    fun part1(input: List<String>): ULong {
        return fishSimulator(input, 80)
    }

    fun part2(input: List<String>): ULong {
        return fishSimulator(input, 256)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 5934UL)
    check(part2(testInput) == 26984457539UL)
    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}