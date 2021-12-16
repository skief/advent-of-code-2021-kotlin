fun main() {
    data class Packet(val version: Int, val type: Int, val payload: Long, val subPackets: MutableList<Packet>)

    fun hex2bin(s: Char) = s.digitToInt(16).toString(2).padStart(4, '0')

    fun parseSubPacket(data: String): Pair<Packet, String>{
        var transmission = data

        val version = transmission.take(3).toInt(2)
        transmission = transmission.drop(3)

        val typeID = transmission.take(3).toInt(2)
        transmission = transmission.drop(3)

        if (typeID == 4){
            var value = ""

            var parseMore = true
            while (parseMore){
                val chunk = transmission.take(5)
                transmission = transmission.drop(5)

                value += chunk.takeLast(4)
                parseMore = chunk[0] == '1'
            }
            return Pair(Packet(version, typeID, value.toLong(2), mutableListOf()), transmission)

        } else {
            val lengthTypeID = transmission.take(1).toInt(2)
            transmission = transmission.drop(1)

            if (lengthTypeID == 0){
                val totalLength = transmission.take(15).toInt(2)
                transmission = transmission.drop(15)

                var subTransmission = transmission.take(totalLength)
                transmission = transmission.drop(totalLength)

                val subPackets = mutableListOf<Packet>()
                while (subTransmission.isNotEmpty()){
                    val temp = parseSubPacket(subTransmission)
                    subPackets.add(temp.first)
                    subTransmission = temp.second
                }
                return Pair(Packet(version, typeID, 0, subPackets), transmission)

            } else {
                val totalLength = transmission.take(11).toInt(2)
                transmission = transmission.drop(11)

                val subPackets = mutableListOf<Packet>()
                for (i in 1..totalLength){
                    val temp = parseSubPacket(transmission)
                    subPackets.add(temp.first)
                    transmission = temp.second
                }
                return Pair(Packet(version, typeID, 0, subPackets), transmission)
            }
        }
    }

    fun parse(data: String) = parseSubPacket(data).first

    fun eval(packet: Packet): Long{
        when (packet.type) {
            0 -> return packet.subPackets.sumOf { eval(it) }
            1 -> return packet.subPackets.fold(1L) { acc, value -> acc * eval(value) }
            2 -> return packet.subPackets.minOf { eval(it) }
            3 -> return packet.subPackets.maxOf { eval(it) }
            4 -> return packet.payload
            5 -> return if (eval(packet.subPackets[0]) > eval(packet.subPackets[1])) 1 else 0
            6 -> return if (eval(packet.subPackets[0]) < eval(packet.subPackets[1])) 1 else 0
            7 -> return if (eval(packet.subPackets[0]) == eval(packet.subPackets[1])) 1 else 0
        }
        return 0
    }

    fun countVersions(data: Packet): Int =
        data.version + data.subPackets.sumOf { countVersions(it) }

    fun part1(input: List<String>): Int {
        val transmission = input[0].map { hex2bin(it) }.joinToString("")
        return countVersions(parse(transmission))
    }

    fun part2(input: List<String>): Long {
        val transmission = input[0].map { hex2bin(it) }.joinToString("")
        return eval(parse(transmission))
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test")
    // check(part1(testInput) == 31)
    check(part2(testInput) == 1L)
    val input = readInput("Day16")
    println(part1(input))
    println(part2(input))
}