fun main() {
    fun part1(input: List<String>): Int {
        val out = input.map { it.split(" | ") }.map { it[1].split(" ") }

        return out.sumOf { it.count { it.length == 2 || it.length == 3 || it.length == 4 || it.length == 7 } }
    }

    fun decode(row: List<Set<Char>>, out: List<Set<Char>> ): Int{
        val mapping = mutableMapOf<Int, Set<Char>>()

        mapping[1] = row.filter { it.size == 2 }.first()
        mapping[4] = row.filter { it.size == 4 }.first()
        mapping[7] = row.filter { it.size == 3 }.first()
        mapping[8] = row.filter { it.size == 7 }.first()

        mapping[6] = row.filter { it.size == 6 && it.intersect(mapping[1]!!.toSet()).size == 1 }.first()

        val lr = mapping[6]!!.intersect(mapping[1]!!.toSet())
        val ur = mapping[1]!!.minus(lr)

        mapping[3] = row.filter { it.size == 5 && it.containsAll(lr) && it.containsAll(ur) }.first()

        mapping[2] = row.filter { it.size == 5 && it.containsAll(ur) && !it.containsAll(lr) }.first()
        mapping[5] = row.filter { it.size == 5 && !it.containsAll(ur) && it.containsAll(lr) }.first()

        mapping[9] = row.filter { it.size == 6 && it.minus(mapping[3]!!.toSet()).size == 1 && it != mapping[6] }.first()
        mapping[0] = row.filter { it.size == 6 && it.minus(mapping[3]!!.toSet()).size == 2 && it != mapping[6] }.first()

        return out.map { mapping.entries.associateBy( { it.value }) { it.key }[it].toString() }.joinToString("").toInt()
    }

    fun part2(input: List<String>): Int {
        val split = input.map { it.split(" | ") }
        val out = split.map { it[1].split(" ") }
        val inp = split.map { it[0].split(" ") }

        val all =  inp.zip(out).map { (it.first + it.second).map { it.toCharArray().toSet() } }

        return all.mapIndexed { index, sets -> decode(sets, out[index].map { it.toCharArray().toSet() }) }
            .sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 26)
    check(part2(testInput) == 61229)
    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}