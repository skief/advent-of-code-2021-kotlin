fun main() {
    fun part1(input: List<String>): Int {
        val res = input.map { x -> x.split(" ") }
            .fold(arrayOf(0, 0)) { acc, (action, value) ->
                when (action) {
                    "forward" -> arrayOf(acc[0] + value.toInt(), acc[1])
                    "down" -> arrayOf(acc[0], acc[1] + value.toInt())
                    "up" -> arrayOf(acc[0], acc[1] - value.toInt())
                    else -> acc
                }}

        return res[0] * res[1]
    }

    fun part2(input: List<String>): Int {
        val res = input.map { x -> x.split(" ") }
            .fold(arrayOf(0, 0, 0)) { acc, (action, value) ->
                when (action) {
                    "forward" -> arrayOf(acc[0] + value.toInt(), acc[1] + acc[2] * value.toInt(), acc[2])
                    "down" -> arrayOf(acc[0], acc[1], acc[2] + value.toInt())
                    "up" -> arrayOf(acc[0], acc[1], acc[2] - value.toInt())
                    else -> acc
                }}

        return res[0] * res[1]
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 150)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
