import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class DWGraphTest {

    @Test
    fun getVertices() {
        private val testgraph = DWGraph<Int>()
        assertEquals(mutableSetOf<Int>(), testgraph.getVertices())

    }

    @Test
    fun addEdge() {
        private val testgraph = DWGraph<String>()
        testgraph.addEdge("A", "B", 2)
        assertEquals(mutableMapOf("B" to 2), testgraph.getEdges())

    }

    @Test
    fun getEdges() {
        private val testgraph = DWGraph<Int>()
        testgraph.addEdge(10, 12, 2)
        assertEquals(mutableMapOf(12 to 2), testgraph.getEdges())

    }

    @Test
    fun clear() {
        private val testgraph = DWGraph<Int>()
        testgraph.addEdges(10, 1000, 3)
        testgraph.clear
        assertEquals(mutableMapOf<Int>(), testgraph.getEdges())
        assertEquals(mutableSetOf<Int>(), testgraph.getVertices())
    }

}