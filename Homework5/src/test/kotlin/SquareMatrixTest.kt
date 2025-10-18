import org.example.SquareMatrix
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

var testSM: SquareMatrix = SquareMatrix(4)

val exSM: SquareMatrix = SquareMatrix(2)

class SquareMatrixTest {

    @Test
    fun addValues() {
        testSM.addValues(0, 0,0)
        testSM.addValues(1, 1,0)
        testSM.addValues(2, 2,0)
        testSM.addValues(3, 3,0)
        testSM.addValues(0, 0,1)
        testSM.addValues(1, 1,1)
        testSM.addValues(2, 2,1)
        testSM.addValues(3, 3,1)
        testSM.addValues(0, 0,2)
        testSM.addValues(1, 1,2)
        testSM.addValues(2, 2,2)
        testSM.addValues(3, 3,2)
        testSM.addValues(0, 0,3)
        testSM.addValues(1, 1,3)
        testSM.addValues(2, 2,3)
        testSM.addValues(3, 3,3)

        assertEquals(1.1, testSM.getValues(1, 1))

        exSM.addValues(1, 0, 0)
        exSM.addValues(1, 1, 0)
        exSM.addValues(1, 0, 1)
        exSM.addValues(1, 1, 1)
    }

    @Test
    fun getValues() {
        assertEquals(0, testSM.getValues(0, 0))

        assertEquals(1, exSM.getValues(1,1))
    }

    @Test
    fun findSize() {
        assertEquals(4, testSM.findSize())
        assertEquals(2, exSM.findSize())
    }

    @Test
    fun copy() {
        val testOfCopy = testSM.copy()
        assertEquals(testOfCopy, testSM.data)
    }

    @Test
    fun deleteValues() {
        testSM.deleteValues(0,0)
        assertEquals(null, testSM.getValues(0,0))
    }

    @Test
    fun divide() {
        testSM.addValues(0, 0,0)
        val testDiv = testSM.divide()

        val div = SquareMatrix(2)
        div.addValues(0, 0,0)
        div.addValues(1, 1,0)
        div.addValues(0, 0,1)
        div.addValues(1, 1,1)

        assertEquals(testDiv[0], div)

        div.addValues(2, 0,0)
        div.addValues(3, 1,0)
        div.addValues(2, 0,1)
        div.addValues(3, 1,1)

        assertEquals(testDiv[2], div)
    }

    @Test
    fun addition() {
        val addTest = testSM.addition(exSM)
        assertEquals(null, addTest)

        val exAdd = SquareMatrix(2)
        exAdd.addValues(2, 0, 0)
        exAdd.addValues(2, 1, 0)
        exAdd.addValues(2, 0, 1)
        exAdd.addValues(2, 1, 1)

        assertEquals(exAdd, exSM.addition(exSM))
    }

    @Test
    fun combine() {
        val exDivide = exSM.divide()
        val exCombo = exSM.combine(exDivide[0], exDivide[1], exDivide[2], exDivide[3])

        assertEquals(exCombo, exSM)
    }

    @Test
    fun multiply() {
        val multTest = testSM.multiply(exSM)
        assertEquals(null, multTest)

        val exMult = SquareMatrix(2)
        exMult.addValues(2, 0, 0)
        exMult.addValues(2, 1, 0)
        exMult.addValues(2, 0, 1)
        exMult.addValues(2, 1, 1)

        assertEquals(exMult, exSM.multiply(exSM))
    }

    @Test
    fun strassenMultiply() {
        val strassTest = testSM.strassenMultiply(exSM)
        assertEquals(null, strassTest)

        val exStrass = SquareMatrix(2)
        exStrass.addValues(2, 0, 0)
        exStrass.addValues(2, 1, 0)
        exStrass.addValues(2, 0, 1)
        exStrass.addValues(2, 1, 1)

        assertEquals(exStrass, exSM.strassenMultiply(exSM))

    }

}