fun main() {
    fun polymerInsertion(input: List<String>, iterations: Int) : ULong{
        val insertionRules = input.drop(2).fold(mutableMapOf<String, List<String>>()) { acc, value ->
            val (from, to) = value.split(" -> ")
            acc[from] = listOf(from[0] + to, to + from[1])
            acc
        }

        val initPolymers = input[0].zipWithNext().fold(mutableMapOf<String, ULong>()) { acc, value ->
            acc[value.first.toString() + value.second] = acc.getOrDefault(value.first.toString() + value.second, 0UL) + 1UL
            acc
        }

        val polymer = generateSequence(initPolymers) {
            it.keys.fold(mutableMapOf()) { acc, value ->
                val new = insertionRules[value]!!
                acc[new[0]] = acc.getOrDefault(new[0], 0UL) + it[value]!!
                acc[new[1]] = acc.getOrDefault(new[1], 0UL) + it[value]!!
                acc
            }
        }.take(iterations + 1).last()

        val occurrences = polymer.keys.fold(mutableMapOf(input[0].first() to 1UL, input[0].last() to 1UL))
        { acc, value ->
            acc[value[0]] = acc.getOrDefault(value[0], 0UL) + polymer[value]!!
            acc[value[1]] = acc.getOrDefault(value[1], 0UL) + polymer[value]!!
            acc
        }.values.map { it / 2UL }.sorted()
        return occurrences.last() - occurrences.first()
    }

    fun part1(input: List<String>): ULong {
        return polymerInsertion(input, 10)
    }

    fun part2(input: List<String>): ULong {
        return polymerInsertion(input, 40)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 1588UL)
    check(part2(testInput) == 2188189693529UL)
    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}