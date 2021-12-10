fun main() {
    fun part1(input: List<String>): Int {
        val inp = input.map { it.toCharArray() }
        val points = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)
        val brackets = mapOf('(' to ')', '[' to ']', '{' to '}', '<' to '>')

        var res = 0

        for (input in inp) {
            val stack = mutableListOf<Char>()
            for (char in input) {
                if (char in brackets.keys){
                    stack.add(char)
                } else {
                    if (char == brackets[stack.last()]){
                        stack.removeLast()
                    } else {
                        res = res + points[char]!!
                        break
                    }
                }
            }
        }

        return res
    }

    fun part2(input: List<String>): ULong {
        val inp = input.map { it.toCharArray() }
        val points = mapOf(')' to 1, ']' to 2, '}' to 3, '>' to 4)
        val brackets = mapOf('(' to ')', '[' to ']', '{' to '}', '<' to '>')

        val res = mutableListOf<ULong>()

        for (input in inp) {
            val stack = mutableListOf<Char>()
            var correct = true
            for (char in input) {
                if (char in brackets.keys){
                    stack.add(char)
                } else {
                    if (char == brackets[stack.last()]){
                        stack.removeLast()
                    } else {
                        correct = false
                        break
                    }
                }
            }
            if (correct && stack.size > 0){
                var score = 0UL
                while (stack.size > 0){
                    score = score * 5UL + points[brackets[stack.last()]!!]!!.toULong()
                    stack.removeLast()
                }
                res.add(score)
            }
        }

        return res.sorted()[res.size / 2]
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 26397)
    check(part2(testInput) == 288957UL)
    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}
