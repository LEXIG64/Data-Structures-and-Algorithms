import org.example.MyQueue
import org.example.MyStack
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MyStackTest {
    @Test
    fun push() {
        val exampleIntStack = MyStack<Int>()
        exampleIntStack.push(1)
        exampleIntStack.push(2)
        exampleIntStack.push(3)
        assertEquals(3, exampleIntStack.peek())
    }

    @Test
    fun pop() {
        val exampleStack = MyStack<String>()
        exampleStack.push("a")
        exampleStack.push("b")
        exampleStack.pop()
        assertEquals("b", exampleStack.pop())
        assertEquals("a", exampleStack.peek())

    }

    @Test
    fun peek() {
        val exampleCharStack = MyStack<Char>()
        exampleCharStack.push(',')
        assertEquals(exampleCharStack.peek(), ',')
        exampleCharStack.pop()
        assertEquals(null, exampleCharStack.peek())
    }

    @Test
    fun isEmpty() {
        val emptyStack = MyStack<Char>()
        assertEquals(true, emptyStack.isEmpty())
        emptyStack.push('!')
        assertEquals(false, emptyStack.isEmpty())
    }

}