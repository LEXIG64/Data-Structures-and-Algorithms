import org.example.MyQueue
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MyQueueTest {
    @Test
    fun enqueue() {
        val exampleQueue = MyQueue<Int>()
        exampleQueue.enqueue(8)
        assertEquals(8, exampleQueue.peek())
        exampleQueue.enqueue(9)
        assertEquals(8, exampleQueue.peek())
    }

    @Test
    fun dequeue() {
        val testQueue = MyQueue<String>()
        testQueue.enqueue("z")
        testQueue.enqueue("y")
        testQueue.enqueue("x")
        testQueue.dequeue()
        assertEquals("y", testQueue.peek())
    }

    @Test
    fun peek() {
        val practiceQueue = MyQueue<Int>()
        practiceQueue.enqueue(5)
        assertEquals(5, practiceQueue.peek())
        practiceQueue.dequeue()
        assertEquals(null, practiceQueue.peek())
    }

    @Test
    fun isEmpty() {
        val emptyQueue = MyQueue<Char>()
        assertEquals(true, emptyQueue.isEmpty())
        emptyQueue.enqueue('.')
        assertEquals(false, emptyQueue.isEmpty())
    }

}