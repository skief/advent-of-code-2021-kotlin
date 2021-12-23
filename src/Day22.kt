import kotlin.math.abs
import kotlin.math.min
import kotlin.math.max

fun main() {
    data class Vec3(val x: Int, val y: Int, val z: Int){
        operator fun plus(rhs: Int) = Vec3(x + rhs, y + rhs, z + rhs)
    }

    data class Cuboid(val start: Vec3, val stop: Vec3){
        fun volume() = abs((stop.x - start.x).toLong() * (stop.y - start.y).toLong() * (stop.z - start.z).toLong())

        fun intersection(other: Cuboid): Cuboid?{
            val start = Vec3(
                max(start.x, other.start.x),
                max(start.y, other.start.y),
                max(start.z, other.start.z)
            )
            val stop = Vec3(
                min(stop.x, other.stop.x),
                min(stop.y, other.stop.y),
                min(stop.z, other.stop.z)
            )

            if (start.x >= stop.x || start.y >= stop.y || start.z >= stop.z){
                return null
            }

            return Cuboid(start, stop)
        }

        fun cut(other: Cuboid): List<Cuboid>{
            val res = mutableListOf<Cuboid>()

            res.add(Cuboid(start, Vec3(stop.x, other.start.y, stop.z)))
            res.add(Cuboid(Vec3(start.x, other.stop.y, start.z), stop))

            res.add(Cuboid(Vec3(start.x, other.start.y, other.start.z), Vec3(other.start.x, other.stop.y, other.stop.z)))
            res.add(Cuboid(Vec3(other.stop.x, other.start.y, other.start.z), Vec3(stop.x, other.stop.y, other.stop.z)))

            res.add(Cuboid(Vec3(start.x, other.start.y, start.z), Vec3(stop.x, other.stop.y, other.start.z)))
            res.add(Cuboid(Vec3(start.x, other.start.y, other.stop.z), Vec3(stop.x, other.stop.y, stop.z)))

            return res.filter { it.volume() > 0 }
        }
    }

    fun solve(input: List<String>, restriction: Cuboid? = null): Long{
        val regex = Regex("([-+]?\\d+)..([-+]?\\d+)")

        var cubes = mutableListOf<Cuboid>()

        for (line in input){
            val coords = regex.findAll(line)
                .map { Pair(it.groups[1]!!.value.toInt(), it.groups[2]!!.value.toInt()) }
                .toList()

            val newCube = if (restriction != null){
                Cuboid(Vec3(coords[0].first, coords[1].first, coords[2].first),
                    Vec3(coords[0].second, coords[1].second, coords[2].second) + 1).intersection(restriction) ?: continue
            } else {
                Cuboid(Vec3(coords[0].first, coords[1].first, coords[2].first),
                    Vec3(coords[0].second, coords[1].second, coords[2].second) + 1)
            }

            val newCubes = mutableListOf<Cuboid>()
            for (cube in cubes){
                val intersect = newCube.intersection(cube)
                if (intersect != null){
                    newCubes.addAll(cube.cut(intersect))
                } else {
                    newCubes.add(cube)
                }
            }
            cubes = newCubes

            if (line.startsWith("on")){
                cubes.add(newCube)
            }
        }

        return cubes.sumOf { it.volume() }
    }

    fun part1(input: List<String>): Long {
        return solve(input, Cuboid(Vec3(-50, -50, -50), Vec3(51, 51, 51)))
    }

    fun part2(input: List<String>): Long {
        return solve(input)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day22_test")
    check(part1(testInput) == 39L)
    //check(part2(testInput) == 12)
    val input = readInput("Day22")
    println(part1(input))
    println(part2(input))
}