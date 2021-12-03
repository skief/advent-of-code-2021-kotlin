fun main() {
    fun toInt(input: List<Int>): Int {
        return input.joinToString("").toInt(2)
    }

    fun part1(input: List<String>): Int {
        val inp = input.map { it.toCharArray() }
            .map { it.map { it.toString().toInt() } }

        val gamma = inp[0].indices.map { inp.sumOf { row -> row[it] } }
            .map { if (it > input.size - it) 1 else 0 }
        val eps = gamma.map { 1 - it }

        return toInt(gamma) * toInt(eps)
    }

    fun oxygenFilter(input: List<List<Int>>, pos: Int): List<List<Int>> {
        return input.filter {
            it[pos] ==
                    input.map { it[pos] }
                        .sum()
                        .run { if (this >= input.size - this) 1 else 0 }
        }
    }

    fun co2Filter(input: List<List<Int>>, pos: Int): List<List<Int>> {
        return input.filter {
            it[pos] ==
                    input.map { it[pos] }
                        .sum()
                        .run { if (this < input.size - this) 1 else 0 }
        }
    }

    fun part2(input: List<String>): Int {
        val inp = input.map { it.toCharArray() }
            .map { it.map { it.toString().toInt() } }

        var pos = 0
        val ox = generateSequence(inp) {
            oxygenFilter(it, pos++)
        }.take(inp[0].size + 1).last()[0]

        pos = 0
        val co2 = generateSequence(inp) {
            co2Filter(it, pos++)
        }.take(inp[0].size + 1).filter { it.size == 1 }.first()[0]

        return toInt(ox) * toInt(co2)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)
    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
