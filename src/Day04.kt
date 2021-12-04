fun main() {
    fun part1(input: List<String>): Int {
        val draws: List<Int> = input[0].split(',').map{ it.toInt() }

        val inp = input.drop(1).filter { it.isNotEmpty() }.map { it.trim().split("\\s+".toRegex()).map { it.toInt() }}

        val boards = inp.chunked(5)
            .map { it.flatten()}

        var count = 0
        val boardSequence = generateSequence(boards) {
            count++
            it.map { board -> board.map { value -> if (value == draws[count-1]) -1 else value } }
        }.take(draws.size).toList()

        val rowSums = boardSequence.map { boards ->
            boards.map { board -> (0..4).map {
                    row -> board.filterIndexed { index, i -> (index / 5) == row }.sum() }
            }
        }

        val colSums = boardSequence.map { boards ->
            boards.map { board -> (0 .. 4).map {
                    col -> board.filterIndexed { index, i -> (index % 5) == col }.sum() }
            }
        }

        val rowWins = rowSums.map { boards -> boards.map { board -> board.filter { sum -> sum == -5 }.isNotEmpty() } }
        val colWins = colSums.map { boards -> boards.map { board -> board.filter { sum -> sum == -5 }.isNotEmpty() } }

        val wins = rowWins.mapIndexed { rowIndex, boards -> boards.mapIndexed { boardIndex, b -> b || colWins[rowIndex][boardIndex] } }

        val winId = boardSequence.indices.filter { index -> wins[index].fold(false) { acc, v -> acc || v } }.first()
        val win = boardSequence[winId].filterIndexed { index, value -> wins[winId][index] }.first()
        return win.filter { it != -1 }.sum() * draws[winId-1]
    }

    fun part2(input: List<String>): Int {
        val draws: List<Int> = input[0].split(',').map{ it.toInt() }

        val inp = input.drop(1).filter { it.isNotEmpty() }.map { it.trim().split("\\s+".toRegex()).map { it.toInt() }}

        val boards = inp.chunked(5)
            .map { it.flatten()}

        var count = 0
        val boardSequence = generateSequence(boards) {
            count++
            it.map { board -> board.map { value -> if (value == draws[count-1]) -1 else value } }
        }.take(draws.size).toList()

        val rowSums = boardSequence.map { boards ->
            boards.map { board -> (0..4).map {
                    row -> board.filterIndexed { index, i -> (index / 5) == row }.sum() }
            }
        }

        val colSums = boardSequence.map { boards ->
            boards.map { board -> (0 .. 4).map {
                    col -> board.filterIndexed { index, i -> (index % 5) == col }.sum() }
            }
        }

        val rowWins = rowSums.map { boards -> boards.map { board -> board.filter { sum -> sum == -5 }.isNotEmpty() } }
        val colWins = colSums.map { boards -> boards.map { board -> board.filter { sum -> sum == -5 }.isNotEmpty() } }

        val wins = rowWins.mapIndexed { rowIndex, boards -> boards.mapIndexed { boardIndex, b -> b || colWins[rowIndex][boardIndex] } }

        val winId = boardSequence.indices.filter { index -> wins[index].fold(true) { acc, v -> acc && v } }.first()
        val win = boardSequence[winId].filterIndexed { index, value -> wins[winId][index] && !wins[winId-1][index] }.first()
        return win.filter { it != -1 }.sum() * draws[winId-1]
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)
    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
