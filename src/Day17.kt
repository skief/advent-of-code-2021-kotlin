fun main() {
    fun part1(input: List<String>): Int {
        val ymin = input[0].split("y=")[1].split("..")[0].toInt()

        return ((-ymin) * (-ymin - 1)) / 2
    }

    fun endsInside(vx: Int, vy: Int, xmin: Int, xmax: Int, ymin: Int, ymax: Int): Boolean{
        var x = 0
        var y = 0

        var velX = vx
        var velY = vy

        while (true){
            x += velX
            y += velY
            velY--
            if (velX > 0){
                velX--
            } else if (velX < 0){
                velX++
            }

            if (y in ymin..ymax && x in xmin..xmax){
                return true
            }
            if (y < ymin || x > xmax){
                return false;
            }
        }
    }

    fun part2(input: List<String>): Int {
        val (ymin, ymax) = input[0].split("y=")[1].split("..").map { it.toInt() }
        val (xmin, xmax) = input[0].split("x=")[1].split(",")[0].split("..").map { it.toInt() }

        return (1..xmax).map { x -> (ymin..-ymin).map { y -> endsInside(x, y, xmin, xmax, ymin, ymax) } }
            .flatten()
            .count { it }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day17_test")
    check(part1(testInput) == 45)
    check(part2(testInput) == 112)
    val input = readInput("Day17")
    println(part1(input))
    println(part2(input))
}