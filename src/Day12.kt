import kotlin.system.measureNanoTime

fun main() {
    fun isBig(node: String): Boolean{
        return node.count { it.isUpperCase() } == node.length
    }

    fun dfs(connections: Map<String, Set<String>>, visited: Set<String>, currentRoute: List<String>): Set<List<String>>{
        if (currentRoute.lastOrNull() == "end"){
            return setOf(currentRoute)
        }
        val allRoutes = mutableSetOf<List<String>>()

        for (neighbor in connections[currentRoute.last()]!!){
            if (isBig(neighbor) || !visited.contains(neighbor)){
                allRoutes.addAll(dfs(connections, visited + setOf(neighbor), currentRoute + listOf(neighbor)))
            }
        }

        return allRoutes
    }

    fun part1(input: List<String>): Int {
        val connections = input.map { it.split("-") }

        val connectionMap = mutableMapOf<String, MutableSet<String>>()
        val nodes = mutableSetOf<String>()

        for (c in connections){
            if (connectionMap.containsKey(c[0])){
                connectionMap[c[0]]!!.add(c[1])
            } else {
                connectionMap[c[0]] = mutableSetOf(c[1])
            }

            if (connectionMap.containsKey(c[1])){
                connectionMap[c[1]]!!.add(c[0])
            } else {
                connectionMap[c[1]] = mutableSetOf(c[0])
            }

            nodes.add(c[0])
            nodes.add(c[1])
        }

        val routes = dfs(connectionMap, setOf("start"), listOf("start"))

        return routes.size
    }

    fun dfs2(connections: Map<String, Set<String>>, visited: Set<String>, visitedTwice: Boolean, currentRoute: List<String>): Set<List<String>>{
        if (currentRoute.lastOrNull() == "end"){
            return setOf(currentRoute)
        }
        val allRoutes = mutableSetOf<List<String>>()

        for (neighbor in connections[currentRoute.last()]!!){
            if (isBig(neighbor) || !visited.contains(neighbor) || (!visitedTwice && neighbor != "start")){
                var newVisited = visited
                var newVisitedTwice = visitedTwice

                if (visited.contains(neighbor) && !isBig(neighbor)){
                    newVisitedTwice = true
                } else {
                    newVisited = newVisited + setOf(neighbor)
                }

                allRoutes.addAll(dfs2(connections, newVisited, newVisitedTwice, currentRoute + listOf(neighbor)))
            }
        }

        return allRoutes
    }

    fun part2(input: List<String>): Int {
        val connections = input.map { it.split("-") }

        val connectionMap = mutableMapOf<String, MutableSet<String>>()
        val nodes = mutableSetOf<String>()

        for (c in connections){
            if (connectionMap.containsKey(c[0])){
                connectionMap[c[0]]!!.add(c[1])
            } else {
                connectionMap[c[0]] = mutableSetOf(c[1])
            }

            if (connectionMap.containsKey(c[1])){
                connectionMap[c[1]]!!.add(c[0])
            } else {
                connectionMap[c[1]] = mutableSetOf(c[0])
            }

            nodes.add(c[0])
            nodes.add(c[1])
        }

        val routes = dfs2(connectionMap, setOf("start"), false, listOf("start"))

        return routes.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 10)
    check(part1(readInput("Day12_test2")) == 19)
    check(part1(readInput("Day12_test3")) == 226)
    check(part2(testInput) == 36)
    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}