package org.example

import kotlin.math.ceil
import kotlin.math.log


/**
 * Represents a mapping of keys to values.
 * @param K the type of the keys
 * @param V the type of the values
 */
interface AssociativeArray<K, V> {
    /**
     * Insert the mapping from the key, [k], to the value, [v].
     * If the key already maps to a value, replace the mapping.
     */
    operator fun set(k: K, v: V)

    /**
     * @return true if [k] is a key in the associative array
     */
    operator fun contains(k: K): Boolean

    /**
     * @return the value associated with the key [k] or null if it doesn't exist
     */
    operator fun get(k: K): V?

    /**
     * Remove the key, [k], from the associative array
     * @param k the key to remove
     * @return true if the item was successfully removed and false if the element was not found
     */
    fun remove(k: K): Boolean

    /**
     * @return the number of elements stored in the hash table
     */
    fun size(): Int

    /**
     * @return the full list of key value pairs for the associative array
     */
    fun keyValuePairs(): List<Pair<K, V>>
}

/**
 * Creates an associative area that performs the same functions as a map collection, with internal functions that allow
 * for setting new key value pairs, remove pairs, and get values by calling on their keys.
 */
class MyAssociativeArray<K, V>: AssociativeArray<K, V> {

    val primeNum = 97
    val bins: Array<MutableList<Pair<K, V>>> = Array(primeNum) {mutableListOf()} // arrayOf(mutableListOf(Pair(null, null)))
    var binIndex: Int = 0
    var listIndex: Int = 0
    val threshold = 5

    /**
     * Insert the mapping from the key, k, to the value, v.
     * If the key already maps to a value, replace the mapping.
     */
    override fun set(k: K, v: V) {
        binIndex = k.hashCode() % primeNum
        if (bins[binIndex].isEmpty()) {
            bins[binIndex].add(Pair(k, v))

        } else {
            listIndex = k.hashCode() % threshold // in cases of multiple entries in one bin this helps organize within the bin to make calling easier
            bins[binIndex].add(listIndex, Pair(k, v))
        }
    }

    /**
     * Returns: true if k is a key in the associative array
     */
    override fun contains(k: K): Boolean {
        binIndex = k.hashCode() % primeNum
        listIndex = k.hashCode() % threshold
        val currList = bins[binIndex]

        if (currList.isEmpty()) {
            return false
        }

        return if (currList.size == 1) {
            k == currList[0].first
        } else {
            k == currList[listIndex].first
        }
    }

    /**
     * Returns: the value associated with the key k or null if it doesn't exist
     */
    override fun get(k: K): V? {
        if (contains(k)) {

            binIndex = k.hashCode() % primeNum
            listIndex = k.hashCode() % threshold
            val currList = bins[binIndex]


            return if (currList.size == 1) {
                currList[0].second
            } else {
                currList[listIndex].second
            }
        }

        return null
    }

    /**
     * Remove the key, k, from the associative array
     * Params: k - the key to remove
    Returns: true if the item was successfully removed and false if the element was not found
    **/
    override fun remove(k: K): Boolean {
        if (contains(k)) {
            binIndex = k.hashCode() % primeNum
            listIndex = k.hashCode() % threshold
            val currList = bins[binIndex]
            currList.removeAt(listIndex)
            return true
        }

        return false
    }

    /**
     * Returns: the number of elements stored in the hash table
     */
    override fun size(): Int {
        var s = 0
        for (p in 1..primeNum) {
            s += bins[p-1].size
        }
        return s
    }

    /**
     * Returns: the full list of key value pairs for the associative array
     */
    override fun keyValuePairs(): MutableList<Pair<K, V>> {
        val keyValList: MutableList<Pair<K, V>> = mutableListOf()
        for (b in 1 .. primeNum) {
            val currBin = bins[b-1]
            if (currBin.isEmpty()) {
                continue
            }

            if (currBin.size == 1) {
                keyValList.add(currBin[0])
            }

            if (currBin.size > 1) {
                for (i in 1 .. currBin.size) {
                    keyValList.add(currBin[i-1])
                }
            }
        }

        return keyValList
    }

}

/**
 * Implements the Lempel-Ziv algorithm for lossless data compression.
 * @return an array representing the encoded table
 */
fun lempelZivEncoding(stream: String): Array<Any> {

    val subsequence = mutableListOf<String>()
    subsequence.add(0, "")
    subsequence.add(1, stream[0].toString())

    val referenceWithChar = mutableListOf<Pair<Int?, Char?>>()
    referenceWithChar.add(0, Pair(null, null))
    referenceWithChar.add(1, Pair(0, stream[0]))

    val x = BinaryWriter(true)
    x.write(0, 1)
    x.write(stream[0].code, 8)

    val miniStringArray = MyAssociativeArray<Int, String>()
    miniStringArray.set(0, "")
    miniStringArray.set(1, stream[0].toString())

    var charUsed = 1
    var stringIndex = 1

    var referenceNum: Int

    while (charUsed != stream.length) {

        var tempString: String = stream[stringIndex].toString()

        charUsed +=1
        stringIndex += 1
        referenceNum = 0

        for (i in 1..subsequence.count()-1) {

            val item = subsequence[i]

            if (charUsed == stream.length) {
                break
            }

            if (item == tempString) {
                tempString += stream[stringIndex]
                stringIndex += 1
                charUsed += 1
                referenceNum = i
            }

        }

        subsequence.add(tempString)
        referenceWithChar.add(Pair(referenceNum, tempString.last()))
        miniStringArray.set(subsequence.count()-1, tempString)


        val rar = when (val r = log(subsequence.count().toDouble()-2, 2.0)) {
            0.0 -> 1
            else -> ceil(r).toInt()
        }

        x.write(referenceNum, rar)
        x.write(tempString.last().code, 8)
    }

    println(miniStringArray.keyValuePairs())

    return arrayOf(miniStringArray, referenceWithChar, x)
}


fun lempelZivDecoding(miniStrings: MyAssociativeArray<Int, String>, refTable: MutableList<Pair<Int?, Char?>>, binary: BinaryWriter): String {
    var decodedString = ""
    val reader = BinaryReader(binary.toByteArray())

    //subsequences.set(0, "")

    for (j in 1 .. refTable.count()-1) {

        val rar = when (val r = log(j.toDouble(), 2.0)) {
            0.0 -> 1
            else -> ceil(r).toInt()
        }

        val refNum = reader.readInt(rar)
        val newChar = reader.readChar(8)


        val newString = miniStrings.get(refNum)!! + newChar.toString()

        decodedString += newString
        //subsequences.set(j, newString)

        println(decodedString)

    }

//    correct reading layout
//    reader.readInt(1)
//    reader.readChar(8)
//
//    reader.readInt(1)
//    reader.readChar(8)
//
//    reader.readInt(2)
//    reader.readChar(8)
//
//    reader.readInt(2)
//    reader.readChar(8)
//
//    reader.readInt(3)
//    reader.readChar(8)
//
//    reader.readInt(3)
//    reader.readChar(8)
//
//    reader.readInt(3)
//    reader.readChar(8)
//
//    reader.readInt(3)
//    reader.readChar(8)
//
//    reader.readInt(3)
//    reader.readChar(8)
//
//    reader.readInt(4)
//    reader.readChar(8)
//
//    reader.readInt(4)
//    reader.readChar(8)

    return decodedString
}

fun main() {
    println("SAMIAMIAMSAMSAMAMI")
    val data = lempelZivEncoding("SAMIAMIAMSAMSAMAMI")
    println(data[0])
    println(data[1])
    println(data[2])
    println(lempelZivDecoding(
        data[0] as MyAssociativeArray<Int, String>,
        data[1] as MutableList<Pair<Int?, Char?>>,
        data[2] as BinaryWriter))
}

