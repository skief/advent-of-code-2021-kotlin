import kotlin.math.abs

fun main() {
    data class Coord(val x: Int, val y: Int, val z: Int)
    data class CoordF(val x: Float, val y: Float, val z: Float){
        override fun equals(other: Any?): Boolean {
            return (other is CoordF) && (x.toInt() == other.x.toInt() && y.toInt() == other.y.toInt() && z.toInt() == other.z.toInt())
        }
    }

    operator fun Coord.plus(rhs: Coord) = Coord(x + rhs.x, y + rhs.y, z + rhs.z)
    operator fun Coord.minus(rhs: Coord) = Coord(x - rhs.x, y - rhs.y, z - rhs.z)
    operator fun Coord.div(rhs: Int) = Coord(x / rhs, y / rhs, z / rhs)

    operator fun CoordF.plus(rhs: CoordF) = CoordF(x + rhs.x, y + rhs.y, z + rhs.z)
    operator fun CoordF.minus(rhs: CoordF) = CoordF(x - rhs.x, y - rhs.y, z - rhs.z)
    operator fun CoordF.div(rhs: Float) = CoordF(x / rhs, y / rhs, z / rhs)

    data class Rotation(val x: String, val y: String, val z: String)

    val allRotations = arrayOf(
        Rotation("-z", "+y", "+x"),
        Rotation("-z", "-x", "+y"),
        Rotation("+x", "-z", "+y"),
        Rotation("+x", "+y", "+z"),
        Rotation("+z", "-x", "-y"),
        Rotation("+y", "+x", "-z"),
        Rotation("-y", "+x", "+z"),
        Rotation("-z", "+x", "-y"),
        Rotation("+x", "-y", "-z"),
        Rotation("+y", "+z", "+x"),
        Rotation("+y", "-x", "+z"),
        Rotation("+z", "+y", "-x"),
        Rotation("-y", "-z", "+x"),
        Rotation("+z", "+x", "+y"),
        Rotation("-x", "-y", "+z"),
        Rotation("+z", "-y", "+x"),
        Rotation("-y", "+z", "-x"),
        Rotation("-x", "+z", "+y"),
        Rotation("+x", "+z", "-y"),
        Rotation("-z", "-y", "-x"),
        Rotation("-y", "-x", "-z"),
        Rotation("+y", "-z", "-x"),
        Rotation("-x", "+y", "-z"),
        Rotation("-x", "-z", "-y")
    )

    fun CoordF.apply(rotation: Rotation): CoordF{
        fun get(axis: Char): Float = when (axis){
            'x' -> x
            'y' -> y
            'z' -> z
            else -> 0f
        }

        val newX = (if(rotation.x[0] == '+') 1f else -1f) * get(rotation.x[1])
        val newY = (if(rotation.y[0] == '+') 1f else -1f) * get(rotation.y[1])
        val newZ = (if(rotation.z[0] == '+') 1f else -1f) * get(rotation.z[1])

        return CoordF(newX, newY, newZ)
    }

    fun Coord.apply(rotation: Rotation): Coord{
        fun get(axis: Char): Int = when (axis){
            'x' -> x
            'y' -> y
            'z' -> z
            else -> 0
        }

        val newX = (if(rotation.x[0] == '+') 1 else -1) * get(rotation.x[1])
        val newY = (if(rotation.y[0] == '+') 1 else -1) * get(rotation.y[1])
        val newZ = (if(rotation.z[0] == '+') 1 else -1) * get(rotation.z[1])

        return Coord(newX, newY, newZ)
    }

    fun List<CoordF>.apply(rotation: Rotation) = this.map { it.apply(rotation) }
    fun List<Coord>.apply(rotation: Rotation) = this.map { it.apply(rotation) }

    data class Scanner(val beacons: MutableList<Coord>){
        var pos: Coord? = null
        var rot: Rotation? = null

        var ids: Map<Int, List<Int>>

        init {
            ids = identifiers()
        }

        fun identifiers(): Map<Int, List<Int>>{
            val result = mutableMapOf<Int, List<Int>>()

            for (i in beacons.indices){
                val ids = mutableListOf<Int>()
                for (j in beacons.indices){
                    val dx = abs(beacons[i].x - beacons[j].x)
                    val dy = abs(beacons[i].y - beacons[j].y)
                    val dz = abs(beacons[i].z - beacons[j].z)

                    ids.add(dx * dx + dy * dy + dz * dz)
                }
                result[i] = ids
            }

            return result
        }
    }

    fun parseInput(input: List<String>): List<Scanner>{
        val scanners = mutableListOf<Scanner>()
        var line = 1
        var beacons = mutableListOf<Coord>()

        while (line < input.size){
            if (input[line] != ""){
                val coord = input[line].split(",").map { it.toInt() }
                beacons.add(Coord(coord[0], coord[1], coord[2]))
                line++
            } else {
                scanners.add(Scanner(beacons))
                beacons = mutableListOf()
                line += 2
            }
        }
        scanners.add(Scanner(beacons))
        return scanners
    }

    fun findMatching(b0: List<Int>, b1: List<Int>): Int{
        var matches = 0

        for (id0 in b0){
            for (id1 in b1){
                if (id0 == id1){
                    matches++
                }
            }
        }

        return matches
    }

    fun findMatching(s0: Scanner, s1: Scanner): List<Pair<Int, Int>>{
        val result = mutableListOf<Pair<Int, Int>>()

        for (i in s0.beacons.indices){
            for (j in s1.beacons.indices){
                if (findMatching(s0.ids[i]!!, s1.ids[j]!!) >= 12){
                    result.add(Pair(i, j))
                }
            }
        }

        return result
    }

    fun centroid(points: List<Coord>): CoordF{
        var x = 0f
        var y = 0f
        var z = 0f

        for (point in points){
            x += point.x.toFloat()
            y += point.y.toFloat()
            z += point.z.toFloat()
        }
        x /= points.size.toFloat()
        y /= points.size.toFloat()
        z /= points.size.toFloat()
        return CoordF(x, y, z)
    }

    fun center(points: List<Coord>): List<CoordF>{
        val c = centroid(points)

        return points.map { CoordF(it.x.toFloat(), it.y.toFloat(), it.z.toFloat()) - c }
    }

    fun findRotation(a: List<CoordF>, b: List<CoordF>): Rotation{
        for(rotation in allRotations){
            val newB = b.apply(rotation)
            var correct = true
            for (i in a.indices){
                if (a[i] != newB[i]){
                    correct = false
                    break
                }
            }
            if (correct){
                return rotation
            }
        }
        println("Could not find rotation")
        return Rotation("", "", "")
    }

    fun findPose(s0: Scanner, s1: Scanner){
        val matches = findMatching(s0, s1)
        val a = mutableListOf<Coord>()
        val b = mutableListOf<Coord>()
        for(match in matches){
            a.add(s0.beacons[match.first])
            b.add(s1.beacons[match.second])
        }

        val aCentered = center(a)
        val bCentered = center(b)
        val rot = findRotation(aCentered, bCentered)

        val pos = s0.pos!! + (a[0] - b[0].apply(rot)).apply(s0.rot!!)

        for (beacon in s1.beacons){
            val newPos = (beacon.apply(rot) + pos)
            if (!s0.beacons.contains(newPos)){
                s0.beacons.add(newPos)
            }
        }
        s0.ids = s0.identifiers()

        s1.pos = pos
        s1.rot = rot
    }

    fun findMatch(scanners: List<Scanner>): Pair<Int, Int>?{
        for (unknown in scanners.indices){
            if (scanners[unknown].pos != null){
                continue
            }
            if (findMatching(scanners[0], scanners[unknown]).size >= 12){
                return Pair(0, unknown)
            }
        }

        return null
    }

    fun part1(input: List<String>): Int {
        val scanners = parseInput(input)
        scanners[0].pos = Coord(0, 0, 0)
        scanners[0].rot = Rotation("+x", "+y", "+z")

        var match = findMatch(scanners)
        while(match != null){
            findPose(scanners[match!!.first], scanners[match!!.second])

            match = findMatch(scanners)
        }

        return scanners[0].beacons.size
    }

    fun part2(input: List<String>): Int {
        val scanners = parseInput(input)
        scanners[0].pos = Coord(0, 0, 0)
        scanners[0].rot = Rotation("+x", "+y", "+z")

        var match = findMatch(scanners)
        while(match != null){
            findPose(scanners[match!!.first], scanners[match!!.second])

            match = findMatch(scanners)
        }

        var maxDist = 0
        for (i in scanners.indices){
            for (j in scanners.indices){
                if (i == j){
                    continue
                }

                val dist = abs(scanners[i].pos!!.x - scanners[j].pos!!.x) +
                        abs(scanners[i].pos!!.y - scanners[j].pos!!.y) +
                        abs(scanners[i].pos!!.z - scanners[j].pos!!.z)
                if (dist > maxDist){
                    maxDist = dist
                }
            }
        }

        return maxDist
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day19_test")
    check(part1(testInput) == 79)
    check(part2(testInput) == 3621)
    val input = readInput("Day19")
    println(part1(input))
    println(part2(input))
}