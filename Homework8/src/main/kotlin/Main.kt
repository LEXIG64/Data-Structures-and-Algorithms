package org.example

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.time.Duration
import kotlin.random.Random
import kotlin.time.measureTime

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val exTree = KDTree(2, arrayOf(doubleArrayOf(1.0,1.1),
        doubleArrayOf(2.0,1.2), doubleArrayOf(3.0, 1.3), doubleArrayOf(4.0, 1.4)))

    exTree.nearestNeighborSearch(doubleArrayOf(5.0, 1.5))
    val exArray = arrayOf(doubleArrayOf(1.0,1.1), doubleArrayOf(2.0,1.2), doubleArrayOf(3.0, 1.3), doubleArrayOf(4.0, 1.4))

    bruteForceSearch(exArray, doubleArrayOf(5.0, 1.5)).printer()


    val kValues = mutableListOf(2, 3, 4, 5, 8, 10, 15)
    val diffNumPoints = mutableListOf(10, 100, 1000, 5000, 10000)

    val fill: Any = 0

    val buildDataTable = Array(8) { Array(6) {fill} }
    val kdSearchDataTable = Array(8) { Array(6) {fill} }
    val bruteSearchDataTable = Array(8) { Array(6) {fill} }

//    for my own personal reference of row column designation
    for (row in 1 .. kValues.size) {
        buildDataTable[row][0] = kValues[row-1]
        kdSearchDataTable[row][0] = kValues[row-1]
        bruteSearchDataTable[row][0] = kValues[row-1]
    }

    for (col in 1 .. diffNumPoints.size) {
        buildDataTable[0][col] = diffNumPoints[col-1]
        kdSearchDataTable[0][col] = diffNumPoints[col-1]
        bruteSearchDataTable[0][col] = diffNumPoints[col-1]
    }

    for (k in 1 .. kValues.size) {
        for (num in 1 .. diffNumPoints.size) {
            val times = runExperiment(kValues[k-1], diffNumPoints[num-1])
            buildDataTable[k][num] = times.first
            kdSearchDataTable[k][num] = times.second
            bruteSearchDataTable[k][num] = times.third
        }
    }

    println("The different runtimes to construct the KD Trees")
    tableDisplay(buildDataTable)

    println("The different runtimes to perform nearest neighbor search using KD Trees")
    tableDisplay(kdSearchDataTable)

    println("The different runtimes to perform nearest neighbor search using Brute Force")
    tableDisplay(bruteSearchDataTable)

}

fun tableDisplay (dataTable: Array<Array<Any>>) {
    val x = "Dimension"
    val y = "# of Points"
    val z = "Time"
    var table = "%15s | %15s | %15s\n".format(x, y, z)
    table += "-".repeat(16) + "|" + "-".repeat(17) + "|" + "-".repeat(16) + "\n"
    for (row in 1 .. 7) { // row is the dimensionality
        for (col in 1 .. 5) { // column is the number of points in the tree
            table += "%15s | %15s | %15s\n".format(
                dataTable[row][0].toString(),
                dataTable[0][col].toString(),
                dataTable[row][col].toString())
            table += "-".repeat(16) + "|" + "-".repeat(17) + "|" + "-".repeat(16) + "\n"
        }
    }

    print(table)
}

/**
 * Creates a class type for KD tree nodes that holds the most vital information:
 * the current dimension being checked [splitDim], the value being checked [splitValue],
 * the children that come off the node [leftChild] and [rightChild], and the points that are
 * below this node [pointList]
 */
class KDNode(val splitDim: Int?,
             val splitValue: Double?,
             var leftChild: KDNode?,
             var rightChild: KDNode?,
             var pointList: Array<DoubleArray>) {
    override fun toString(): String {
        return "$splitDim $splitValue [$leftChild] [$rightChild]"
    }

}

/**
 *
 */
class KDTree(val dimensionK: Int, points: Array<DoubleArray>) {

    val startRoot = buildTree(points, 0)

    /**
     *
     */
    fun buildTree(coordinates: Array<DoubleArray>, splitDim: Int): KDNode {
        if (coordinates.count() <= 1) {
            return KDNode(
                splitDim = null,
                splitValue = null,
                leftChild = null,
                rightChild = null,
                pointList = coordinates
            )
        }


        val median = coordinates.median(splitDim)

        val root = KDNode(splitDim, median, null, null, coordinates)

        val leftSide = mutableListOf<DoubleArray>()

        val rightSide = mutableListOf<DoubleArray>()

        for (coord in coordinates) {
            if (coord[splitDim] < median) {
                leftSide.add(coord)
            } else {
                rightSide.add(coord)
            }
        }
        require(!leftSide.isEmpty()) { "left side should have at least 1 value" }
        require(!rightSide.isEmpty()) { "right side should have at least 1 value" }

        root.leftChild = buildTree(leftSide.toTypedArray(), (splitDim+1)%dimensionK)
        root.rightChild = buildTree(rightSide.toTypedArray(), (splitDim+1)%dimensionK)

        return root

    }

    /**
     *
     */
    private fun nearestNeighborKDSearchHelper(point: DoubleArray, expression: KDNode): DoubleArray? {

        val leftChild = expression.leftChild
        val rightChild = expression.rightChild

        if (leftChild == null || rightChild == null) {
            return expression.pointList.last()
        }

        val splitDim = expression.splitDim?: return null
        val splitValue = expression.splitValue?: return null

        val candidate: DoubleArray
        val sibling: KDNode

        if (point[splitDim] < splitValue) {
            candidate = nearestNeighborKDSearchHelper(point, leftChild)?: return null
            sibling = rightChild
        } else {
            candidate = nearestNeighborKDSearchHelper(point, rightChild)?: return null
            sibling = leftChild
        }

        val dist1 = point.distance(candidate)

        if (dist1 > abs(point[splitDim] - splitValue)) {
            val sibCandidate = nearestNeighborKDSearchHelper(point, sibling)?: return candidate
            val dist2 = point.distance(sibCandidate)

            return if (dist1 < dist2) {
                candidate
            } else {
                sibCandidate
            }
        } else {
            return candidate
        }

    }

    /**
     * Callable function to run a nearest neighbor search for [point] using at KD tree
     * from outside the class
     */
    fun nearestNeighborSearch(point: DoubleArray): DoubleArray? {
        return nearestNeighborKDSearchHelper(point, startRoot)
    }
}

/**
 * Internal function for Double Arrays that calculate the distance from the values in an array to
 * another Double Array, treating each spot in the array as a value in a coordinate point
 * @return the distance between the two arrays
 */
fun DoubleArray.distance(comparison: DoubleArray): Double {
    val difference = DoubleArray(this.count()) { this[it] - comparison[it] }
    val squaredDiff = difference.map { it * it }
    val dist = sqrt(squaredDiff.sum())
    return dist
}

/**
 * Internal function for Double Arrays that print out each item in the array in their own line
 */
fun DoubleArray.printer() {
    for (v in this) {
        println(v)
    }
}

/**
 * Internal Function for Arrays containing double arrays
 */
fun Array<DoubleArray>.median(splitDim: Int): Double {
    val splitsCoord = mutableListOf<Double>()

    for (coord in this) {
        splitsCoord.add(coord[splitDim])
    }
    splitsCoord.sort()

    return splitsCoord[splitsCoord.count()/2]

}


/**
 * Searches through every point in [grid], calculating its distance to [query].
 * @return the closest point to the [query] from [grid]
 */
fun bruteForceSearch(grid: Array<DoubleArray>, query: DoubleArray): DoubleArray {
    var closestPoint = grid[0]
    var minDist = grid[0].distance(grid.last())

    for (point in grid) {
        val tempDist = query.distance(point)

        if (tempDist < minDist) {
            closestPoint = point
            minDist = tempDist
        }
    }

    return closestPoint
}



/**
 * Benchmark [KDTree] against brute force nearest neighbor.
 * 1000 test points will be generated to test against the training
 * points.
 *
 * @param k: the dimensionality of the dataset to create
 * @param numPoints: the number of points to use to match against
 *   (1000 test points will be used)
 * @return the triple of [Duration] objects where the first specifies
 * the time to build the tree, the second specifies the time it takes
 * to query the tree with 1,000 points, and the third is the time it
 * takes to find the nearest neighbor to these points using the brute
 * force approach.
 */
fun runExperiment(k: Int, numPoints: Int): Triple<Duration, Duration, Duration> {
    val trainingPoints = Array(numPoints)
    { DoubleArray(k) { Random.nextDouble(0.0, 20.0) } }

    val testQueries = Array(1000){ DoubleArray(k) { Random.nextDouble(0.0, 20.0) }  }

    val buildTime: Duration = measureTime { KDTree(k, trainingPoints) }

    val testTree = KDTree(k, trainingPoints)

    val kdSearchTime: Duration = measureTime {
        for (q in testQueries) {
            testTree.nearestNeighborSearch(q) } }


    val bruteSearchTime: Duration = measureTime {
        for (q in testQueries) {
            bruteForceSearch(trainingPoints, q)} }

    return Triple(buildTime, kdSearchTime, bruteSearchTime)
}