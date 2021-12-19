fun main() {
    data class Node(var left: Node? = null, var right: Node? = null, var value: Int? = null){
        var parent: Node? = null
    }

    fun parseNode(input: String): Pair<Node, String>{
        var s = input.drop(1)
        val result = Node()

        if (s[0] == '['){
            val temp = parseNode(s)
            result.left = temp.first
            s = temp.second
        } else {
            val end = s.indexOf(',')
            result.left = Node(null, null, s.substring(0, end).toInt())
            s = s.drop(end)
        }
        result.left!!.parent = result

        s = s.drop(1)

        if (s[0] == '['){
            val temp = parseNode(s)
            result.right = temp.first
            s = temp.second
        } else {
            val end = s.indexOf(']')
            result.right = Node(null, null, s.substring(0, end).toInt())
            s = s.drop(end)
        }
        result.right!!.parent = result

        s = s.drop(1)

        return Pair(result, s)
    }

    fun getLeftNeighbor(a: Node): Node?{
        var temp = a.parent!!
        var last = a
        while (last === temp.left){
            last = temp
            if (temp.parent == null){
                return null
            }
            temp = temp.parent!!
        }

        if (temp.left == null){
            return null;
        }

        temp = temp.left!!

        while(temp.value == null){
            temp = temp.right!!;
        }
        return temp
    }

    fun getRightNeighbor(a: Node): Node?{
        var temp = a.parent!!
        var last = a
        while (last === temp.right){
            last = temp
            if (temp.parent == null){
                return null
            }
            temp = temp.parent!!
        }

        if (temp.right == null){
            return null;
        }

        temp = temp.right!!

        while(temp.value == null){
            temp = temp.left!!;
        }
        return temp
    }

    fun explode(a: Node){
        val left = a.left!!.value!!
        val right = a.right!!.value!!

        val newNode = Node(null, null, 0)
        newNode.parent = a.parent

        var neighbor = getLeftNeighbor(a)
        neighbor?.value = neighbor?.value!! + left

        neighbor = getRightNeighbor(a)
        neighbor?.value = neighbor?.value!! + right

        if (a.parent?.left == a){
            a.parent?.left = newNode
        } else if (a.parent?.right == a){
            a.parent?.right = newNode
        }
    }

    fun split(a: Node){
        val number = a.value!!
        val newNode = Node(Node(null, null, number / 2), Node(null, null, (number + 1) / 2))
        newNode.left!!.parent = newNode
        newNode.right!!.parent = newNode
        newNode.parent = a.parent

        if (a.parent!!.left == a){
            a.parent!!.left = newNode
        } else {
            a.parent!!.right = newNode
        }
    }

    fun findExploding(a: Node, depth: Int): Node? {
        if (depth > 5 && a.value != null){
            return a
        }

        if (a.left != null){
            val leftExp = findExploding(a.left!!, depth + 1)
            if (leftExp != null){
                return leftExp
            }
        }

        if (a.right != null){
            val rightExp = findExploding(a.right!!, depth + 1)
            if (rightExp != null){
                return rightExp
            }
        }

        return null
    }

    fun findSplit(a: Node): Node?{
        if (a.value != null && a.value!! > 9){
            return a
        }

        if (a.left != null){
            val leftSplit = findSplit(a.left!!)
            if (leftSplit != null){
                return leftSplit
            }
        }

        if (a.right != null){
            val rightSplit = findSplit(a.right!!)
            if (rightSplit != null){
                return rightSplit
            }
        }

        return null
    }

    fun reduce(a: Node){
        while (true){
            val explosion = findExploding(a, 1)
            if (explosion != null){
                explode(explosion.parent!!)

                continue
            }

            val s = findSplit(a)
            if (s != null){
                split(s)

                continue
            }

            break
        }
    }

    fun addNodes(a: Node, b: Node): Node{
        val res = Node(a, b, null)
        a.parent = res
        b.parent = res
        reduce(res)
        return res
    }

    fun magnitude(a: Node): Int{
        if (a.value != null){
            return a.value!!
        }

        return 3 * magnitude(a.left!!) + 2 * magnitude(a.right!!)
    }

    fun parseLine(input: String): Node = parseNode(input).first

    fun part1(input: List<String>): Int {
        var a = parseLine(input[0])
        for (i in 1 until input.size){
            val b = parseLine(input[i])
            a = addNodes(a, b)
        }
        return magnitude(a)
    }

    fun part2(input: List<String>): Int {
        var largest = 0

        for (number1 in 0 until input.size){
            for (number2 in 0 until input.size){
                if (number1 == number2){
                    continue
                }

                val mag = magnitude(addNodes(parseLine(input[number1]), parseLine(input[number2])))
                if (mag > largest){
                    largest = mag
                }
            }
        }

        return largest
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18_test")
    check(part1(testInput) == 4140)
    check(part2(testInput) == 3993)
    val input = readInput("Day18")
    println(part1(input))
    println(part2(input))
}