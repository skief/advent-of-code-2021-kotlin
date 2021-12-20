fun main() {
    fun parseRules(input: String): List<Boolean> = input.toCharArray().map { it == '#' }
    fun parse(input: List<String>) = input.drop(2).map { it.map { it == '#' } }

    fun step(state: List<List<Boolean>>, rules: List<Boolean>, edge: Boolean): Pair<List<List<Boolean>>, Boolean>{
        val paddedState = (-1..state.size).map { y -> (-1..state[0].size).map { x ->
            if (y in state.indices && x in state[0].indices) state[y][x] else edge } }

        val newState = paddedState.indices.map { y -> paddedState[0].indices.map { x ->
            var value = 0
            var bin = 512
            for (dy in -1..1){
                for (dx in -1..1){
                    bin /= 2
                    if ((y + dy) !in paddedState.indices || (x + dx) !in paddedState[0].indices){
                        value += if (edge) bin else 0
                        continue
                    }
                    value += if (paddedState[y + dy][x + dx]) bin else 0
                }
            }
            rules[value]
        } }

        return Pair(newState, if(rules[0]) !edge else false)
    }

    fun part1(input: List<String>): Int {
        val rules = parseRules(input[0])

        return (1..2).fold(Pair(parse(input), false)) { acc, _ -> step(acc.first, rules, acc.second)}
            .first.sumOf { it.count { it } }
    }

    fun part2(input: List<String>): Int {
        val rules = parseRules(input[0])

        return (1..50).fold(Pair(parse(input), false)) { acc, _ -> step(acc.first, rules, acc.second)}
            .first.sumOf { it.count { it } }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day20_test")
    check(part1(testInput) == 35)
    check(part2(testInput) == 3351)
    val input = readInput("Day20")
    println(part1(input))
    println(part2(input))
}