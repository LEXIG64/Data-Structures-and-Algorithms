package org.example

interface MutableIntList {

    /**
     * Add [element] to the end of the list
     */
    fun add(element: Int)

    /**
     * Remove all elements from the list
     */
    fun clear()

    /**
     * @return the size of the list
     */
    fun size(): Int

    /**
     * @param index the index to return
     * @return the element at [index]
     */
    operator fun get(index: Int): Int

    /**
     * Store [value] at position [index]
     * @param index the index to set
     * @param value to store at [index]
     */
    operator fun set(index: Int, value: int)
}

class MyMutableIntList(): MutableIntList {
    var list = Array<Int>(10) {0}
    var numofelements = 0

    /**
     * Add [element] to the end of the list
     */
    override fun add(element: Int) {
        while(numofelements < list.size-1) {
            list[numofelements] = element
            numofelements += 1
        }
        do {
            var newList = Array<Int>(list.size*2) {0}
            for (n in 0..list.size-1) {
                newList[n] = list[n]
            }

            list = newList

        } while(numofelements >= list.size-1)

    }

    /**
     * Remove all elements from the list
     */
    override fun clear(){
        list = emptyArray()

    }

    /**
     * @return the size of the list
     */
    override fun size(): Int {
        return list.size

    }

    /**
     * @param index the index to return
     * @return the element at [index]
     */
    override operator fun get(index: Int): Int {
        return list[index]

    }

    /**
     * Store [value] at position [index]
     * @param index the index to set
     * @param value to store at [index]
     */
    override operator fun set(index: Int, value: int){
        list[index] = value

    }
}

fun main() {
    val arraySizes = listOf(100, 1000, 10000, 100000, 1000000, 10000000, 100000000)
    println("numberOfElements totalTime timePerElement")
    for (arraySize in arraySizes) {
        val myList = MyMutableIntList()
        val timeTaken = measureTime {
            for (i in 0..<arraySize) {
                myList.add(i)
            }
        }
        println("$arraySize $timeTaken ${timeTaken/arraySize}")
    }
}