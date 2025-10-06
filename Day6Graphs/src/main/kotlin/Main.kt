package org.example


class Graph<VertexType> {
    private var vertices: MutableSet<VertexType> = mutableSetOf()
    private var edges: MutableMap<VertexType, MutableSet<VertexType>> = mutableMapOf()

    /**
     * Add the vertex [v] to the graph
     * @param v the vertex to add
     * @return true if the vertex is successfully added, false if the vertex
     *   was already in the graph
     */
    fun addVertex(v: VertexType): Boolean {
        if (vertices.contains(v)) {
            return false
        }
        vertices.add(v)
        return true
    }

    /**
     * Add an edge between vertex [from] connecting to vertex [to]
     * @param from the vertex for the edge to originate from
     * @param to the vertex to connect the edge to
     * @return true if the edge is successfully added and false if the edge
     *     can't be added or already exists
     */
    fun addEdge(from: VertexType, to: VertexType): Boolean {
        if (!vertices.contains(from) || !vertices.contains(to)) {
            return false
        }
        edges[from]?.also { currentAdjacent ->
            if (currentAdjacent.contains(to)) {
                return false
            }
            currentAdjacent.add(to)
        } ?: run {
            edges[from] = mutableSetOf(to)
        }
        return true
    }

    /**
     * Clear all vertices and edges
     */
    fun clear() {
        vertices = mutableSetOf()
        edges = mutableMapOf()
    }



    /**
    * Search through a graph using a breadth-first search
    * @param start the node to start the search
    * @param target the node to search for
    * @return true if and only if path exists between [start] and [target]
    > */
    fun bfs(start: VertexType, target: VertexType): Boolean {
        val toVisit: MutableList<VertexType> = mutableListOf()
        val priorityList: MutableList<VertexType> = mutableListOf()
        var m: MutableSet<VertexType>?

        priorityList.add(start)
        toVisit.add(start)

        var currentPos: VertexType = start

        while (priorityList.count() != 0) {
            currentPos = priorityList.first()
            priorityList.remove(currentPos)
            if (currentPos == target) {
                return true
            }
            m = edges[currentPos]

            if (m != null) {
                for (vertex in m) {
                    if (vertex !in toVisit) {
                        priorityList.add(1, vertex)
                        toVisit.add(1, vertex)
                    }
                }
            }
        }

        return false
    }

    /**
     * Search through a graph using a breadth-first search
     * @param start the node to start the search
     * @param target the node to search for
     * @return the path from start to target (if one exists) and null otherwise
     */
    fun bfsPath(start: VertexType, target: VertexType): List<VertexType>? {
        val toVisit: MutableList<VertexType> = mutableListOf()
        val priorityList: MutableList<VertexType> = mutableListOf()
        var m: MutableSet<VertexType>?
        val path: MutableList<VertexType> = mutableListOf()

        priorityList.add(start)
        toVisit.add(start)

        var currentPos: VertexType = start

        while (priorityList.count() != 0) {
            currentPos = priorityList.first()
            path.add(currentPos)
            priorityList.remove(currentPos)
            if (currentPos == target) {
                return path
            }
            m = edges[currentPos]

            if (m != null) {
                for (vertex in m) {
                    if (vertex !in toVisit) {
                        priorityList.add(1, vertex)
                        toVisit.add(1, vertex)
                    }
                }
            }
        }

        return null
    }



    /**
     * Search through a graph using a depth-first search
     * @param start the node to start the search
     * @param target the node to search for
     * @return true if and only if path exists between [start] and [target]
     */
    fun dfs(start: VertexType, target: VertexType): Boolean {
        val toVisit: MutableList<VertexType> = mutableListOf()
        val priorityList: MutableList<VertexType> = mutableListOf()
        var m: MutableSet<VertexType>?

        priorityList.add(start)
        toVisit.add(start)

        var currentPos: VertexType = start

        while (priorityList.count() != 0) {
            currentPos = priorityList.last()
            priorityList.remove(currentPos)
            if (currentPos == target) {
                return true
            }
            m = edges[currentPos]

            if (m != null) {
                for (vertex in m) {
                    if (vertex !in toVisit) {
                        priorityList.add(vertex)
                        toVisit.add(vertex)
                    }
                }
            }
        }

        return false
    }

    /**
     * Search through a graph using a depth-first search
     * @param start the node to start the search
     * @param target the node to search for
     * @return the path from start to target (if one exists) and null otherwise
     */
    fun dfsPath(start: VertexType, target: VertexType): List<VertexType>? {
        val toVisit: MutableList<VertexType> = mutableListOf()
        val priorityList: MutableList<VertexType> = mutableListOf()
        var m: MutableSet<VertexType>?
        val path: MutableList<VertexType> = mutableListOf()

        priorityList.add(start)
        toVisit.add(start)

        var currentPos: VertexType = start

        while (priorityList.count() != 0) {
            currentPos = priorityList.last()
            path.add(currentPos)
            priorityList.remove(currentPos)
            if (currentPos == target) {
                return path
            }
            m = edges[currentPos]

            if (m != null) {
                for (vertex in m) {
                    if (vertex !in toVisit) {
                        priorityList.add(vertex)
                        toVisit.add(vertex)
                    }
                }
            }
        }

        return null
    }

}