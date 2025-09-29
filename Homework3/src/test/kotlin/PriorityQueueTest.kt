import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PriorityQueueTest {

    @Test
    fun isEmpty() {
        val testqueue = PriorityQueue<Int>()
        assertEquals(true, testqueue.isEmpty())
        testqueue.addWithPriority(9, 9)
        assertEquals(false, testqueue.isEmpty())
    }

    @Test
    fun addWithPriority() {
        val exqueue = PriorityQueue<String>()
        exqueue.addWithPriority("A", 1)
        assertEquals(true, exqueue.queue.contains("A"))
    }

    @Test
    fun next() {
        val pracqueue = PriorityQueue<String>()
        pracqueue.addWithPriority("z", 26)
        pracqueue.addWithPriority("y", 25)
        assertEquals(<"y", 25>, pracqueue.next())
    }

    @Test
    fun adjustPriority() {
        val testqueue = PriorityQueue<Int>()
        testqueue.addWithPriority(10, 1)
        testqueue.adjustPriority(10, 2)
        assertEquals(2, testqueue.queue.returnPriorityNum())
    }

}