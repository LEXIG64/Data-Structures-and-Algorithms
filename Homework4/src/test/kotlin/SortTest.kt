import org.example.findmin
import org.example.heapSort
import org.example.insertionSort
import org.example.mergeSort
import org.example.selectionSort
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class SortTest {

    val testList1 = mutableListOf<Int>(9, 8, 7, 6, 5, 4, 3, 2, 1, 0)
    val testList2 = mutableListOf<Int>(8, 54, 88, 101, 636, 28, 25, 67, 1008, 50, 560)
    val testList3 = mutableListOf<Int>(1, 8, 106, 67, 24, 73, 91, 19, 20, 1, 13)
    val testList4 = mutableListOf<Int>(14, 88, 26, 34, 20, 63, 37, 77, 62, 94, 90)
    val testList5 = mutableListOf<Int>(9, 9, 9, 9, 9, 9, 9, 9, 9)

    @Test
    fun testFindmin() {
        assertEquals(0, findmin(testList1))
        assertEquals(8, findmin(testList2))
        assertEquals(1, findmin(testList3))
        assertEquals(14, findmin(testList4))
        assertEquals(9, findmin(testList5))
    }

    @Test
    fun testhHeapSort() {
        assertEquals(listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), heapSort(testList1))
        assertEquals(listOf(8, 25, 28, 50, 54, 67, 88, 101, 560, 636, 1008), heapSort(testList2))
        assertEquals(listOf(1, 1, 8, 13, 19, 20, 24, 67, 73, 91, 106), heapSort(testList3))
        assertEquals(listOf(14, 20, 26, 34, 37, 62, 63, 77, 88, 90, 94), heapSort(testList4))
        assertEquals(testList5, heapSort(testList5))
    }

    @Test
    fun testMergeSort() {
        assertEquals(listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), mergeSort(testList1))
        assertEquals(listOf(8, 25, 28, 50, 54, 67, 88, 101, 560, 636, 1008), mergeSort(testList2))
        assertEquals(listOf(1, 1, 8, 13, 19, 20, 24, 67, 73, 91, 106), mergeSort(testList3))
        assertEquals(listOf(14, 20, 26, 34, 37, 62, 63, 77, 88, 90, 94), mergeSort(testList4))
        assertEquals(testList5, mergeSort(testList5))
    }

    @Test
    fun testSelectionSort() {
        assertEquals(listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), selectionSort(testList1))
        assertEquals(listOf(8, 25, 28, 50, 54, 67, 88, 101, 560, 636, 1008), selectionSort(testList2))
        assertEquals(listOf(1, 1, 8, 13, 19, 20, 24, 67, 73, 91, 106), selectionSort(testList3))
        assertEquals(listOf(14, 20, 26, 34, 37, 62, 63, 77, 88, 90, 94), selectionSort(testList4))
        assertEquals(testList5, selectionSort(testList5))
    }

    @Test
    fun testInsertionSort() {
        assertEquals(listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), insertionSort(testList1))
        assertEquals(listOf(8, 25, 28, 50, 54, 67, 88, 101, 560, 636, 1008), insertionSort(testList2))
        assertEquals(listOf(1, 1, 8, 13, 19, 20, 24, 67, 73, 91, 106), insertionSort(testList3))
        assertEquals(listOf(14, 20, 26, 34, 37, 62, 63, 77, 88, 90, 94), insertionSort(testList4))
        assertEquals(testList5, insertionSort(testList5))
    }

}