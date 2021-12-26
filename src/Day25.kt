fun main() {
    fun part1(input: List<String>): Int {
        val maxY = input.size
        val maxX = input[0].length

        val east = input.indices.map { y -> input[y].indices.map { x -> if(input[y][x] == '>') Pair(y, x) else Pair(-1, -1) } }
            .flatten()
            .filter { it != Pair(-1, -1) }
            .toSet()

        val south = input.indices.map { y -> input[y].indices.map { x -> if(input[y][x] == 'v') Pair(y, x) else Pair(-1, -1) } }
            .flatten()
            .filter { it != Pair(-1, -1) }
            .toSet()

        val iterations = generateSequence(Pair(east.toSet(), south.toSet())){ (east, south) ->
            val newE = east.map { (y, x) -> if(east.contains(Pair(y, (x + 1) % maxX)) ||
                south.contains(Pair(y, (x + 1) % maxX))) Pair(y, x) else Pair(y, (x + 1) % maxX)}.toSet()
            val newS = south.map { (y, x) -> if(newE.contains(Pair((y + 1) % maxY, x)) ||
                south.contains(Pair((y + 1) % maxY, x))) Pair(y, x) else Pair((y + 1) % maxY, x)}.toSet()
            Pair(newE, newS)
        }.zipWithNext().indexOfFirst { (prev, curr) -> prev.first == curr.first && prev.second == curr.second } + 1

        return iterations
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day25_test")
    check(part1(testInput) == 58)
    val input = readInput("Day25")
    println(part1(input))
}