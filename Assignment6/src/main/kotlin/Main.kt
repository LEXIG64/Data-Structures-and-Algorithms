package org.example
import kotlin.math.max
import kotlin.random.Random
import kotlin.time.measureTime

/**
 *
 */
class Matrix(r: Int, c: Int) {
    // ... (other stuff omitted)
    // first array is rows, second array is columns
    var data: Array<IntArray> = Array(r) { IntArray(c) }

    /**
     * Adds [newValue] to the matrix at a specific index of row [row] amd column [column]
     */
    fun addValues(newValue: Int, row: Int, column: Int) {
        data[row][column] = newValue
    }

    /**
     * @returns the value at the positon of the [row]th row
     * and the [column]th column
     */
    fun getValues(row: Int, column: Int): Int {
        return data[row][column]
    }
}


/**
 * Runs an algorithm to solve the classic Knapsack problem, where are given a list of [items],
 * each with a weight and a value, and we need to determine the number of each item to include
 * in a collection so that the total weight is less than or equal to [capacity] and the total
 * value is as large as possible using dynamic programming
 * @returns the highest possible value the Knapsack can have
 */
fun dynamicKnapsack(items: MutableList<Pair<Int, Int>>, capacity: Int): Int {

    val values = mutableListOf<Int>()
    val weights = mutableListOf<Int>()

    var max = 0

    for (i in items) {
        values.add(i.first)
        weights.add(i.second)
    }

    val dynamicMatrix = Matrix((items.size + 1), (capacity + 1))
    for (j in 0 .. capacity) {
        dynamicMatrix.addValues(0, 0, j)
    }
    for (i in 0 .. items.size) {
        dynamicMatrix.addValues(0, i, 0)
    }

    for (i in 1 .. items.size) {
        for (j in 1 .. capacity) {

            if (weights[i-1] >= j) {
                dynamicMatrix.addValues(dynamicMatrix.getValues(i - 1, j), i, j)
            } else {
                dynamicMatrix.addValues(
                    max(dynamicMatrix.getValues(i-1, j),
                        dynamicMatrix.getValues(i-1, j-weights[i-1]) + values[i-1]),
                    i, j)
            }
            if (dynamicMatrix.getValues(i, j) > max)
                max = dynamicMatrix.getValues(i, j)
        }
    }

    return max
}

/**
 *
 */
fun findMaxIndex(list: MutableList<Int>): Int? {
    if (list.isEmpty()) {
        return null
    }

    var maximum: Int = list[0]
    var maxIndex = 0

    for (i in 1 .. list.size) {
        if (list[i-1] > maximum) {
            maximum = list[i-1]
            maxIndex = i-1
        }
    }
    return maxIndex
}

/**
 * Runs an algorithm to solve the classic Knapsack problem, where are given a list of [items],
 * each with a weight and a value, and we need to determine the number of each item to include
 * in a collection so that the total weight is less than or equal to [capacity] and the total
 * value is as large as possible using the greedy algorithm
 * @returns the highest possible value the Knapsack can have
 */
fun greedyKnapsack(items: MutableList<Pair<Int, Int>>, capacity: Int): Int {

    val values = mutableListOf<Int>()
    val weights = mutableListOf<Int>()
    val valuesUsed = mutableListOf<Int>()
    val weightsUsed = mutableListOf<Int>()
    var sackValue = 0
    var sackWeight = 0
    var index : Int?
    val vwRatio = mutableListOf<Int>()

    for (i in items) {
        vwRatio.add(i.first/i.second)
        values.add(i.first)
        weights.add(i.second)
    }

    while (sackWeight < capacity) {
        index = findMaxIndex(vwRatio)
        if (index == null) {
            return sackValue
        }
        sackWeight += weights[index]
        weightsUsed.add(weights[index])

        sackValue += values[index]
        valuesUsed.add(values[index])

        if (sackWeight > capacity) {
            sackWeight -= weightsUsed.min()
            sackValue -= valuesUsed.min()

        }

        vwRatio.removeAt(index)
        weights.removeAt(index)
        values.removeAt(index)
    }

    if (sackWeight > capacity) {
        sackValue -= valuesUsed.min()

    }

    return sackValue
}

/**
 * Runs an algorithm to solve the classic Knapsack problem, where are given a list of [items],
 * each with a weight and a value, and we need to determine the number of each item to include
 * in a collection so that the total weight is less than or equal to [capacity] and the total
 * value is as large as possible using recursion
 * @returns the highest possible value the Knapsack can have
 */
fun recursionKnapsack(items: MutableList<Pair<Int, Int>>, capacity: Int, itemsNum: Int): Int {

    val values = mutableListOf<Int>()
    val weights = mutableListOf<Int>()

    var selections = 0

    if (itemsNum == 0 || capacity == 0) {
        return 0
    }

    for (i in 1 .. itemsNum) {
        values.add(items[i-1].first)
        weights.add(items[i-1].second)
    }

    if (weights[itemsNum-1] < capacity) {
        selections = values[itemsNum-1] + recursionKnapsack(items, (capacity-weights[itemsNum-1]), itemsNum-1) }

    val skips: Int = recursionKnapsack(items, capacity, itemsNum-1)

    return max(selections, skips)
}

fun diffSizeRuntimes() {
    val numOfItems = listOf(5, 10, 15, 20, 25)
    val w = Random.nextInt(0, 50)
    var testItems = mutableListOf<Pair<Int, Int>>()

    for (num in numOfItems) {

        for (i in 1 .. num) {
            testItems.add(i-1, Pair(Random.nextInt(1, 25), Random.nextInt(1, 25)))
        }

        val dynamicTime = measureTime {dynamicKnapsack(testItems, w)}
        println("It takes $dynamicTime to solve the Knapsack problem of weight $w for $num items using dynamic programming")

        val greedyTime = measureTime {greedyKnapsack(testItems, w)}
        println("It takes $greedyTime to solve the Knapsack problem of weight $w for $num items using the greedy algorithm")

        val recursionTime = measureTime {recursionKnapsack(testItems, w, num)}
        println("It takes $recursionTime to solve the Knapsack problem of weight $w for $num items using a recursion method")

        testItems = mutableListOf()
    }
}

fun diffWeightRuntimes() {

    val testItems: MutableList<Pair<Int, Int>> = mutableListOf(
        Pair(Random.nextInt(1, 25), Random.nextInt(1, 10)),
        Pair(Random.nextInt(1, 45), Random.nextInt(1, 10)),
        Pair(Random.nextInt(1, 45), Random.nextInt(1, 10)),
        Pair(Random.nextInt(1, 45), Random.nextInt(1, 10)),
        Pair(Random.nextInt(1, 45), Random.nextInt(1, 10)),
        Pair(Random.nextInt(1, 45), Random.nextInt(1, 10)),
        Pair(Random.nextInt(1, 45), Random.nextInt(1, 10)),
        Pair(Random.nextInt(1, 45), Random.nextInt(1, 10)),
        Pair(Random.nextInt(1, 45), Random.nextInt(1, 10)),
        Pair(Random.nextInt(1, 45), Random.nextInt(1, 10)),
    )

    val weights = listOf(5, 10, 15, 20, 30, 55)

    for (w in weights) {
        val dynamicTime = measureTime {dynamicKnapsack(testItems, w)}
        println("It takes $dynamicTime to solve the Knapsack problem of $w weight using dynamic programming")

        val greedyTime = measureTime {greedyKnapsack(testItems, w)}
        println("It takes $greedyTime to solve the Knapsack problem of $w weight using the greedy algorithm")

        val recursionTime = measureTime {recursionKnapsack(testItems, w, 10)}
        println("It takes $recursionTime to solve the Knapsack problem of $w weight using a recursion method" +
                "")
    }
}

val ex = mutableListOf(
    Pair(20, 1), Pair(5, 2), Pair(10, 3),
    Pair(40,8), Pair(15,7), Pair(25, 4)
)

fun main() {

    println(dynamicKnapsack(ex, 10))
    println(greedyKnapsack(ex, 10))
    println(recursionKnapsack(ex, 10, 6))

    diffSizeRuntimes()
    diffWeightRuntimes()
}