package org.example
import kotlin.random.Random
import kotlin.time.measureTime

class SquareMatrix(n: Int) {
    // ... (other stuff omitted)
    // first array is rows, second array is columns
    var data: Array<IntArray> = Array(n) { IntArray(n) }
    val size: Int = n

    /**
     * Adds [newValue] to the matrix at a specific index of row [row] amd column [column]
     */
    fun addValues(newValue: Int, row: Int, column: Int) {
        data[row][column] = newValue
    }

    /**
     * @returns the size of the matrix
     */
    fun findSize(): Int {
        return size
    }


    /**
     * @returns the value at the positon of the [row]th row
     * and the [column]th column
     */
    fun getValues(row: Int, column: Int): Int {
        return data[row][column]
    }

    /**
     * Creates a copy of the matrix data array and its values
     */
    fun copy(): Array<IntArray> {
        val copyMatrix: Array<IntArray> = Array(size) { IntArray(size) }
        for (row in 0 .. size) {
            for (col in 0 .. size){
                copyMatrix[row][col] = getValues(row, col)
            }
        }
        return copyMatrix
    }

    /**
     * Deletes the value at the positon of the [row]th row
     * and the [column]th column
     */
    fun deleteValues(row: Int, column: Int) {
        val datacopy = copy()
        val arrayTolist = datacopy.toMutableList()
        arrayTolist.removeAt(row*column)
        data = arrayTolist.toTypedArray()
    }


    /**
     * dividing this matrix into four n/2 × n/2 matrices
     */
    fun divide():List<SquareMatrix> {
        val miniArray1 = SquareMatrix(findSize()/2)

        for (r in 1 .. (findSize()/2)) {
            for (col in 1 .. (findSize()/2)) {
                miniArray1.addValues(getValues(r-1, col-1), r-1, col-1)

            }
        }

        val miniArray2 = SquareMatrix(findSize()/2)

        for (r in 1 .. (findSize()/2)) {
            for (col in (findSize()/2) .. findSize()) {
                miniArray2.addValues(getValues(r-1, col-1), r-1, col-1)

            }
        }

        val miniArray3 = SquareMatrix(findSize()/2)

        for (r in (findSize()/2)  .. findSize()-1) {
            for (col in 0 .. (findSize()/2)-1){
                miniArray3.addValues(getValues(r,col), r, col)

            }
        }

        val miniArray4 = SquareMatrix(findSize()/2)

        for (r in (findSize()/2) .. findSize()-1) {
            for (col in (findSize()/2) .. findSize()-1){
                miniArray4.addValues(getValues(r, col), r, col)

            }
        }

        val blockArray: List<SquareMatrix> = listOf(miniArray1, miniArray2, miniArray3, miniArray4)

        return blockArray
    }

    /**
     * Add this matrix to [other] matrix.
     * @return this+[other] if the dimensions are compatible and null otherwise
     */
    fun addition(other: SquareMatrix?): SquareMatrix? {
        if (other?.findSize() != findSize()) {
            return null
        }

        val sumMatrix = SquareMatrix(findSize())
        var sumValue: Int

        for (c in 0 .. findSize()-1) {

            for (r in 0 .. findSize()-1) {

                sumValue = getValues(r, c) + other.getValues(r, c)
                sumMatrix.addValues(sumValue, r, c)

            }
        }

        return sumMatrix
    }

    /**
     * Combine 4 block matrices into one main matrices
     * @returns a square matrix is the dimensions are compatible and null otherwise
     */
    fun combine(topL: SquareMatrix, topR: SquareMatrix, lowL: SquareMatrix, lowR: SquareMatrix): SquareMatrix? {
        if (topL.findSize() + topR.findSize() != topL.findSize() + lowL.findSize()) {
            return null
        }
        val length: Int = topL.findSize() + topR.findSize()
        var value = 0

        val fullMatrix = SquareMatrix(length)

        for (r in 0 ..topL.findSize()-1) {

            for (index in 0..length) {
                if (index < topL.findSize()) {
                    value = topL.getValues(r, index)
                }

                if (index >= topL.findSize()) {
                    value = topR.getValues(r, index)
                }

                fullMatrix.addValues(value, r, index)
            }
        }

        for (r in topL.findSize() .. length-1) {

            for (index in 0..length-1) {
                if (index < lowL.findSize()) {
                    value = lowL.getValues(r, index)
                }

                if (index >= lowL.findSize()) {
                    value = lowR.getValues(r, index)
                }

                fullMatrix.addValues(value, r, index)
            }
        }

        return fullMatrix
    }

    /**
     * Multiply this matrix by [other].
     * You can implement this either using block-based matrix multiplication or
     * traditional matrix multiplication (the kind you learn about in math
     * classes!)
     * @return this*[other] if the dimensions are compatible and null otherwise
     */
    fun multiply(other: SquareMatrix): SquareMatrix? {
        if (other.findSize() != findSize()) {
            return null
        }

        val productMatrix = SquareMatrix(findSize())
        var productValue: Int


        for (colum in 0 .. findSize()-1) {
            productValue = 0

            for (row in 0 .. findSize()-1) {

                for (add in 0..findSize()-1) {
                    productValue += (data[row][add] * other.getValues(add, colum))

                }
                productMatrix.addValues(productValue, row, colum)
            }
        }

        return productMatrix
    }


    /**
     * Multiply this matrix by [other] using Strassen's algorithm
     * @return this*[other] if the dimensions are compatible and null otherwise
     */
    fun strassenMultiply(other: SquareMatrix):SquareMatrix? {
        if (other.findSize() != findSize()) {
            return null
        }

        val theseBlocks = divide()

        val otherBlocks = other.divide()

        var topLeft = theseBlocks[0].multiply(otherBlocks[0])
        topLeft = topLeft?.addition(theseBlocks[1].multiply(otherBlocks[2]))

        var topRight = theseBlocks[0].multiply(otherBlocks[1])
        topRight = topRight?.addition(theseBlocks[1].multiply(otherBlocks[3]))

        var lowerLeft = theseBlocks[2].multiply(otherBlocks[0])
        lowerLeft = lowerLeft?.addition(theseBlocks[3].multiply(otherBlocks[2]))

        var lowerRight = theseBlocks[2].multiply(otherBlocks[1])
        lowerRight = lowerRight?.addition(theseBlocks[3].multiply(otherBlocks[3]))

        val productMatrix: SquareMatrix? = combine(topLeft!!, topRight!!, lowerLeft!!, lowerRight!!)

        return productMatrix
    }
}

fun main() {
    val sizes = listOf(4,
        8, 20, 100, 1000)
    var testMatrix: SquareMatrix


    for (size in sizes) {
        testMatrix = SquareMatrix(size)

        for (r in 1..size) {

            for (c in 1 .. size) {
                testMatrix.addValues(Random.nextInt(0, 100), r-1, c-1)
            }
        }

        val multiplyTime = measureTime { testMatrix.multiply(testMatrix) }
        println("For a $size square matrix, the runtime to multiply it by itself with matrix multiplication is $multiplyTime")

        val strassenTime = measureTime { testMatrix.multiply(testMatrix) }
        println("For a $size square matrix, the runtime to multiply it by itself with Strassan's algorithm is $strassenTime")
    }
}

