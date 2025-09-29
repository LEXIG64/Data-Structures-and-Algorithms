package org.example

/**
 * ``Graph`` represents a directed graph
 * @param VertexType the type that represents a vertex in the graph
 */
interface Graph<VertexType> {


    /**
     * @return the vertices in the graph
     */
    fun getVertices(): Set<VertexType>

    /**
     * Add an edge between [from] and [to] with edge weight [cost]
     */
    fun addEdge(from: VertexType, to: VertexType, cost: Double)

    /**
     * Get all the edges that begin at [from]
     * @return a map where each key represents a vertex connected to [from] and the value represents the edge weight.
     */
    fun getEdges(from: VertexType): Map<VertexType, Double>

    /**
     * Remove all edges and vertices from the graph
     */
    fun clear()
}


/**
 * ``Graph`` represents a directed graph
 * @param VertexType the type that represents a vertex in the graph
 */
class DWGraph<VertexType>(): Graph<VertexType> {

    private var vertices: MutableSet<VertexType> = mutableSetOf()
    private var edges: MutableMap<VertexType, MutableSet<Pair<VertexType, Double>>> = mutableMapOf()

    /**
     * @return the vertices in the graph
     */
    override fun getVertices(): Set<VertexType> {
        return vertices
    }

    /**
     * Add a vertices [newVertex] to the graph
     */
    fun addVertices(newVertex: VertexType): {
        vertices.add(newVertex)
    }


    /**
     * Add an edge between [from] and [to] with edge weight [cost]
     */
    override fun addEdge(from: VertexType, to: VertexType, cost: Double) {
        edges[from] = mutableSetOf(Pair(to, cost))
    }

    /**
     * Get all the edges that begin at [from]
     * @return a map where each key represents a vertex connected to [from] and the value represents the edge weight.
     */
    override fun getEdges(from: VertexType): Map<VertexType, Double> {
        val edgeMap = mutableMapOf<VertexType, Double>()
        val fromEdges = edges[from]
        if (fromEdges != null) {
            for (pair in fromEdges) {
                edgeMap[pair.first] = pair.second

            }
        }
        return edgeMap
    }

    /**
     * Remove all edges and vertices from the graph
     */
    override fun clear() {
        vertices = mutableSetOf<VertexType>()
        edges = mutableMapOf<VertexType>()
    }
}


/**
 * Representation of a min heap
 * @param T the type of the heap elements
 * @property vertices these hold the tree structure using the scheme in
 *     https://en.wikipedia.org/wiki/Heap_(data_structure)
 * @property indexMap this is used for quick lookups of existing vertices
 */
class MinHeap<T> {
    private var vertices: MutableList<Pair<T, Double>> = mutableListOf()
    private var indexMap: MutableMap<T, Int> = mutableMapOf()

    fun isEmpty(): Boolean {
        return vertices.isEmpty()
    }


    /**
     * Insert [data] into the heap with value [heapNumber]
     * @return true if [data] is added and false if [data] was already there
     */
    fun insert(data: T, heapNumber: Double):Boolean {
        if (contains(data)) {
            return false
        }
        vertices.add(Pair<T, Double>(data, heapNumber))
        indexMap[data] = vertices.size - 1
        percolateUp(vertices.size - 1)
        return true
    }

    /**
     * Gets the minimum value from the heap and removes it.
     * @return the minimum value in the heap (or null if heap is empty)
     */
    fun getMin(): T? {
        when (vertices.size) {
            0 -> {
                return null
            }
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

    fun returnPriorityNum(index: Int) : Double {
        return vertices[index].second
    }

    /**
     * Change the number of an element
     * @param vertex the element to change
     * @param newNumber the new number for the element
     */
    fun adjustHeapNumber(vertex: T, newNumber: Double) {
        getIndex(of=vertex)?.also{ index ->
            vertices[index] = Pair(vertices[index].first, newNumber)
            // do both operations to avoid explicitly testing which way to go
            percolateUp(startIndex=index)
            bubbleDown(startIndex=index)
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
        // update our index map so we still can find thigns
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




/**
 * ``MinPriorityQueue`` maintains a priority queue where the lower
 *  the priority value, the sooner the element will be removed from
 *  the queue.
 *  @param T the representation of the items in the queue
 */
interface MinPriorityQueue<T> {
    /**
     * @return true if the queue is empty, false otherwise
     */
    fun isEmpty(): Boolean

    /**
     * Add [elem] with at level [priority]
     */
    fun addWithPriority(elem: T, priority: Double)

    /**
     * Get the next (lowest priority) element.
     * @return the next element in terms of priority.  If empty, return null.
     */
    fun next(): T?

    /**
     * Adjust the priority of the given element
     * @param elem whose priority should change
     * @param newPriority the priority to use for the element
     *   the lower the priority the earlier the element int
     *   the order.
     */
    fun adjustPriority(elem: T, newPriority: Double)
}

/**
 * ``MinPriorityQueue`` maintains a priority queue where the lower
 *  the priority value, the sooner the element will be removed from
 *  the queue.
 *  @param T the representation of the items in the queue
 */
class PriorityQueue<T>(): MinPriorityQueue<T> {

    val queue = MinHeap<T>()

    /**
     * @return true if the queue is empty, false otherwise
     */
    override fun isEmpty(): Boolean {
        return queue.isEmpty()
    }

    /**
     * Add [elem] with at level [priority]
     */
    override fun addWithPriority(elem: T, priority: Double) {
        queue.insert(elem, priority)
    }

    /**
     * Get the next (lowest priority) element.
     * @return the next element in terms of priority.  If empty, return null.
     */
    override fun next(): T? {
        return queue.getMin()
    }

    /**
     * Adjust the priority of the given element
     * @param elem whose priority should change
     * @param newPriority the priority to use for the element
     *   the lower the priority the earlier the element int
     *   the order.
     */
    override fun adjustPriority(elem: T, newPriority: Double) {
        queue.adjustHeapNumber(elem, newPriority)
    }
}

/**
 * Implementation of a Dijkstr Search, where we cycle through all edges in a graph to
 * find the fastest path from a starting vertex to a goal vertex
 * @parem graph is the graph being searched
 * @parem start is the vertex we are starting the search from
 * @parem goal is the vertex we are trying to find the shortest path to
 * @return an integer of the shortest distance path in edge weight units
 */
fun DijkstraSearch(graph: Graph<VertexType>, start: VertexType, goal: VertexType): Int {


    val vertices = graph.getVertices()
    var priorityQueue = PriorityQueue<T>()
    var u: T
    var distance: Int = 0

    for (v in vertices) {
        if (v != start) {
            priorityQueue.addWithPriority(v, null)
        }
    }

    while (!priorityQueue.isEmpty()) {
        u = priorityQueue.next()

        for (n in graph.getEdges(u)) {


            if (priorityQueue.queue.contains(n.key)) {

                val oldval = priorityQueue.queue.returnPriorityNum(n.key)
                priorityQueue.adjustPriority(n.key, n.value + oldval)

            } else {

                priorityQueue.addWithPriority(n.key, n.value)

            }

            if (n.key == goal) {
                distance = priorityQueue.queue.returnPriorityNum(n.key)
            }

        }

    }
    when (distance == 0) {
        return null
    } else {
        return distance
    }

}

/**
 * Using the Dijkstra Search algorithm to search for through a custome graph
 * of mine for the shortest path
 */
fun main() {
    val nyctravel = DWGraph<String>()

    nyctravel.addVertices("Brooklyn")
    nyctravel.addVertices("Bronx")
    nyctravel.addVertices("Queens")
    nyctravel.addVertices("Manhattan")
    nyctravel.addVertices("Staten Island")
    nyctravel.addEdges("Brooklyn", "Manhattan", 20)
    nyctravel.addEdges("Brooklyn", "Queens", 40)
    nyctravel.addEdges("Queens", "Manhattan", 30)
    nyctravel.addEdges("Bronx", "Manhattan", 35)
    nyctravel.addEdges("Queens", "Bronx", 30)
    nyctravel.addEdges("Manhattan", "Staten Island", 45)
    nyctravel.addEdges("Brooklyn", "Staten Island", 20)
    println(DijkstraSearch(nyctravel, "Bronx", "Staten Island"))
    println(DijkstraSearch(nyctravel, "Brooklyn", "Bronx"))
}
