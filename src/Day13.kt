fun main() {
    fun fold(coords: Set<Pair<Int, Int>>, axis: String, pos: Int): Set<Pair<Int, Int>>{
        val (original, folded) = coords.partition { if (axis == "x") it.first <= pos else it.second <= pos }

        return original.union(folded
            .map { if(axis == "x") Pair(2 * pos - it.first, it.second) else Pair(it.first, 2 * pos - it.second) })
    }

    fun part1(input: List<String>): Int {
        val coords = input
            .filter { it.isNotEmpty() && !it.startsWith("fold") }
            .map { it.split(",").map { it.toInt() } }
            .map { Pair(it[0], it[1]) }
            .toSet()

        val folds = input
            .filter { it.startsWith("fold") }
            .map { it.split(" ")[2].split("=") }

        return fold(coords, folds[0][0], folds[0][1].toInt()).size
    }

    fun part2(input: List<String>): Int {
        val coords = input
            .filter { it.isNotEmpty() && !it.startsWith("fold") }
            .map { it.split(",").map { it.toInt() } }
            .map { Pair(it[0], it[1]) }
            .toSet()

        val folds = input
            .filter { it.startsWith("fold") }
            .map { it.split(" ")[2].split("=") }

        val res = folds.fold(coords) { coords, f -> fold(coords, f[0], f[1].toInt()) }

        println((0..res.maxOf { it.second })
            .map { y-> (0..res.maxOf { it.first }).map { x-> if(res.contains(Pair(x, y))) "#" else " " } }
            .map { it.joinToString("") }
            .joinToString("\n"))

        return res.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 17)
    check(part2(testInput) == 16)
    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}