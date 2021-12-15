import java.util.*
import kotlin.math.abs

fun main() {
    fun genNeighbors(pos: Pair<Int, Int>, w: Int, h: Int) =
        (-1..1).map { y -> (-1..1).map { x -> Pair(pos.first + y, pos.second + x) } }
            .flatten()
            .filter { abs(pos.first - it.first) + abs(pos.second - it.second) == 1 }
            .filter { (it.first in (0 until h)) && (it.second in (0 until w)) }

    fun dijkstra(map: List<List<Int>>): Int {
        val distances = MutableList(map.size) { MutableList(map[0].size) { Int.MAX_VALUE } }
        distances[0][0] = 0

        val queue = PriorityQueue<Pair<Int, Int>> { from, to -> distances[from.first][from.second].compareTo(distances[to.first][to.second]) }
        queue.add(Pair(0, 0))

        while (queue.isNotEmpty()){
            val bestV = queue.poll()
            for (n in genNeighbors(bestV, map[0].size, map.size)){
                val dist = distances[bestV.first][bestV.second] + map[n.first][n.second]
                if (dist < distances[n.first][n.second]){
                    distances[n.first][n.second] = dist

                    queue.add(n)
                }
            }
        }

        return distances.last().last()
    }

    fun part1(input: List<String>): Int {
        return dijkstra(input.map { it.toCharArray().map { it.toString().toInt() } })
    }

    fun part2(input: List<String>): Int {
        val originalMap = input.map { it.toCharArray().map { it.toString().toInt() } }

        val mapRow = originalMap.map { row -> (1..4).fold(row) { acc, value ->
            acc + row.map { it + value }.map { if (it <= 9) it else it - 9 }
        } }

        val map = (1..4).fold(mapRow) { acc, value ->
            acc + mapRow.map { it.map { it + value } }.map { it.map { if (it <= 9) it else it - 9 } }
        }

        return dijkstra(map)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput) == 40)
    check(part2(testInput) == 315)
    val input = readInput("Day15")
    println(part1(input))
    println(part2(input))
}