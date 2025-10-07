package org.example
import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.measureTime

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

fun findmin(unsortedList: MutableList<Int>): Int {
    var minimum: Int = unsortedList[0]
    for (i in  unsortedList) {
        if (i < minimum) {
            minimum = i
        }
    }
    return minimum
}

/**
 * Representation of a min heap
 * @param T the type of the heap elements
 * @property vertices these hold the tree structure using the scheme in
 *     https://en.wikipedia.org/wiki/Heap_(data_structure)
 * @property indexMap this is used for quick lookups of existing vertices
 */
class MinHeap<T>(nodes: MutableList<Pair<T, Int>>) {
    var vertices = nodes
    private var indexMap: MutableMap<T, Int> = mutableMapOf()



    /**
     * Gets the minimum value from the heap and removes it.
     * @return the minimum value in the heap (or null if heap is empty)
     */
    fun getMin(): T {
        when (vertices.size) {
            1 -> {
                val tmp = vertices[0].first
                vertices = mutableListOf()
                return tmp
            }
            else -> {
                val tmp = vertices[0].first
                swap(0, vertices.size - 1)
                vertices.removeLast()
                indexMap.remove(tmp)
                bubbleDown(0)
                return tmp
            }
        }
    }


    /**
     * @return true if the element is in the heap, false otherwise
     */
    fun contains(vertex: T): Boolean {
        return getIndex(of=vertex) != null
    }

    /**
     * @return the index in the list where the element is stored (or null if
     *     not there)
     */
    private fun getIndex(of: T): Int? {
        return indexMap[of]
    }

    /**
     * Bubble down from [startIndex] if needed
     * @param startIndex the index in the tree to start the bubbling
     */
    private fun bubbleDown(startIndex: Int) {
        val startNumber = vertices[startIndex].second
        val leftIndex = getLeftIndex(of=startIndex)
        val rightIndex = getRightIndex(of=startIndex)
        val leftNumber = if (leftIndex >= vertices.size) null else vertices[leftIndex].second
        val rightNumber = if (rightIndex >= vertices.size) null else vertices[rightIndex].second

        /*
         * We determine whether we need to continue with bubbling
         * Case 1: for each child, either the number is less or the child doesn't exist
         * Case 2: either the right child doesn't exist (meaning the left child must) or
         *    the right child exists, the left child exists, and left is smaller than right
         * Case 3: this will capture the case where we need to swap to the right
         */
        if ((leftNumber == null || startNumber < leftNumber) &&
            (rightNumber == null || startNumber < rightNumber)) {
            return
        } else if (rightNumber == null || (leftNumber != null && leftNumber < rightNumber)) {
            // swap with left since it is smallest
            swap(leftIndex, startIndex)
            bubbleDown(leftIndex)
            return
        } else {
            // swap with right since it is smallest
            swap(rightIndex, startIndex)
            bubbleDown(rightIndex)
            return
        }
    }

    /**
     * Swap [index1] and [index2] in the tree
     * @param index1 the first element to swap
     * @param index2 the second element to swap
     */
    private fun swap(index1: Int, index2: Int) {
        // update our index map so we still can find things
        indexMap[vertices[index1].first] = index2
        indexMap[vertices[index2].first] = index1
        val tmp = vertices[index1]
        vertices[index1] = vertices[index2]
        vertices[index2] = tmp
    }

    /**
     * Percolate up from [startIndex] if needed
     * @param startIndex the index in the tree to start the percolation
     */
    private fun percolateUp(startIndex: Int) {
        val parentIndex = getParentIndex(of = startIndex)
        if (parentIndex < 0) {
            // we must be at the root
            return
        } else if (vertices[startIndex].second < vertices[parentIndex].second) {
            swap(parentIndex, startIndex)
            percolateUp(parentIndex)
        }
    }

    /**
     * Get the parent index in the list
     * @param of the index to start from
     * @return the index where the parent is stored (if applicable)
     */
    private fun getParentIndex(of: Int):Int {
        return (of - 1) / 2
    }

    /**
     * Get the left index in the list
     * @param of the index to start from
     * @return the index where the left child is stored (if applicable)
     */
    private fun getLeftIndex(of: Int):Int {
        return of * 2 + 1
    }

    /**
     * Get the right index in the list
     * @param of the index to start from
     * @return the index where the right child is stored (if applicable)
     */
    private fun getRightIndex(of: Int):Int {
        return of * 2 + 2
    }
}

fun heapSort(unsortedList: MutableList<Int>): List<Int> {
    val heapList: MutableList<Pair<Int, Int>> = mutableListOf()
    val sortedList: MutableList<Int> = mutableListOf()
    for (i in unsortedList) {
        heapList.add(Pair(i, i))
    }
    val unsortedHeap = MinHeap(heapList)
    while (unsortedHeap.vertices.isNotEmpty()) {
        sortedList.add(unsortedHeap.getMin())
    }

    return sortedList
}

fun mergeSort(unsortedList: List<Int>): List<Int> {

    val size: Int = unsortedList.size
    val list1: MutableList<Int> = mutableListOf()
    val list2: MutableList<Int> = mutableListOf()
    val returnList: MutableList<Int> = mutableListOf()
    var k = 0
    var j = 0

    if (size == 1) {
        return unsortedList
    }
    for (i in 0..size/2) {
        list1.add(unsortedList[i])
    }

    val sortedList1 = mergeSort(list1)

    for (i in (size/2)+1 .. size) {
        list2.add(unsortedList[i])
    }

    val sortedList2 = mergeSort(list2)

    while (returnList.size != unsortedList.size) {
        if (sortedList1[k] <= sortedList2[j]) {
            returnList.add(sortedList1[k])
            k += 1
        }
        if (list2[j] < list1[k]) {
            returnList.add(sortedList2[j])
            j += 1
        }
    }

    return returnList
}



fun selectionSort(unsortedList: MutableList<Int>): List<Int> {
    val copyList = unsortedList
    var minInfo: Int
    val returnList: MutableList<Int> = mutableListOf()

    while (returnList.size != unsortedList.size) {
        minInfo = findmin(unsortedList)
        returnList.add(minInfo)
        copyList.remove(minInfo)
    }
    return returnList
}

fun insertionSort(unsortedList: MutableList<Int>): List<Int> {
    var index: Int


    for (i in 1 ..unsortedList.size) {
        index = i

        while (unsortedList[index] > unsortedList[index - 1]) {
            unsortedList.add((index - 1), i)
            index -= 1
            if (index <= 0) {
                index = 1
            }
        }
    }
    return unsortedList
}

val heapRuntimes = mutableMapOf<Int, Duration>()
val mergeRuntimes = mutableMapOf<Int, Duration>()
val selectionRuntimes = mutableMapOf<Int, Duration>()
val insertionRuntimes = mutableMapOf<Int, Duration>()

fun main() {

    val sizes = listOf(1000,
        10, 20, 100, 1000, 10000)
    val testList = mutableListOf<Int>()

    for (size in sizes) {
        for (n in 1 .. size) {
            testList.add(Random.nextInt(0, 100))
            n +1
        }

        val heapTime = measureTime { heapSort(testList) }
        heapRuntimes[size] = heapTime
        println("For a size $size list the heap sorting time is $heapTime")

        val selectionTime = measureTime { selectionSort(testList) }
        selectionRuntimes[size] = selectionTime
        println("For a size $size list the selection sorting time is $selectionTime")

        val insertionTime = measureTime { insertionSort(testList) }
        insertionRuntimes[size] = insertionTime
        println("For a size $size list the insertion sorting time is $insertionTime")

        val mergeTime = measureTime { mergeSort(testList) }
        mergeRuntimes[size] = mergeTime
        println("For a size $size list the merge sorting time is $mergeTime")
    }
}

