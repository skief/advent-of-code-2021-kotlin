fun main() {
    fun formulaIt(input: Int, i: Int, zInit: List<Int>): List<Int>{
        var x = 0
        var y = 0
        val z = zInit.toMutableList()

        x = if (z.size > 0) z.last() else 0

        when (i){
            0  -> x += 12
            1  -> x += 13
            2  -> x += 13
            3  -> {
                z.removeLast()
                x += -2
            }
            4  -> {
                z.removeLast()
                x += -10
            }
            5  -> x += 13
            6  -> {
                z.removeLast()
                x += -14
            }
            7  -> {
                z.removeLast()
                x += -5
            }
            8  -> x += 15
            9  -> x += 15
            10 -> {
                z.removeLast()
                x += -14
            }
            11 -> x += 10
            12 -> {
                z.removeLast()
                x += -14
            }
            13 -> {
                z.removeLast()
                x += -5
            }
        }

        if (x != input){
            y = input

            when (i){
                0  -> y += 7
                1  -> y += 8
                2  -> y += 10
                3  -> y += 4
                4  -> y += 4
                5  -> y += 6
                6  -> y += 11
                7  -> y += 13
                8  -> y += 1
                9  -> y += 8
                10 -> y += 4
                11 -> y += 13
                12 -> y += 4
                13 -> y += 14
            }

            z.add(y)
        }

        return z
    }

    fun getSolution(max: Boolean = true, depth: Int = 0, input: List<Int> = listOf(), z: List<Int> = listOf()): List<Int>{
        val pops = listOf(3, 4, 6, 7, 10, 12, 13)

        if (depth == 14){
            if (z.isEmpty()){
                return input
            } else {
                return listOf()
            }

        } else if (z.size >= pops.filter { it >= depth }.size + 1) {
            return listOf()
        }

        for (i in if (max) 9 downTo 1 else 1..9){
            val res = getSolution(max, depth + 1, input + listOf(i), formulaIt(i, depth, z))
            if (res.isNotEmpty()){
                return res
            }
        }
        return listOf()
    }

    fun part1(): Long {
        return getSolution(true).joinToString("").toLong()
    }

    fun part2(): Long {
        return getSolution(false).joinToString("").toLong()
    }

    println(part1())
    println(part2())
}