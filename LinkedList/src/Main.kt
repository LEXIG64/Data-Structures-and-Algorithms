fun main() {
    println("Hi")
}

interface LinkedList<T> {
    /**
     * Adds the element [data] to the front of the linked list.
     */
    fun pushFront(data: T)

    /**
     * Adds the element [data] to the back of the linked list.
     */
    fun pushBack(data: T)

    /**
     * Removes an element from the front of the list. If the list is empty, it is unchanged.
     * @return the value at the front of the list or nil if none exists
     */
    fun popFront(): T?

    /**
     * Removes an element from the back of the list. If the list is empty, it is unchanged.
     * @return the value at the back of the list or nil if none exists
     */
    fun popBack(): T?

    /**
     * @return the value at the front of the list or nil if none exists
     */
    fun peekFront(): T?

    /**
     * @return the value at the back of the list or nil if none exists
     */
    fun peekBack(): T?

    /**
     * @return true if the list is empty and false otherwise
     */
    fun isEmpty(): Boolean
}

interface Stack<T> {

    /**
     * Add [data] to the top of the stack
     */
    fun push(data: T)


    /**
     * Remove the element at the top of the stack.  If the stack is empty, it remains unchanged.
     * @return the value at the top of the stack or nil if none exists
     */
    fun pop(): T?


    /**
     * @return the value on the top of the stack or nil if none exists
     */
    fun peek(): T?


    /**
     * @return true if the stack is empty and false otherwise
     */
    fun isEmpty(): Boolean

}

class MyStack<T> : Stack<T> {
    /**
     * A StackNode holds a piece of data and a reference to the next element of the stack
     */
    class StackNode<T>(val data: T,
                       var next: StackNode<T>?
    )

    var top: StackNode<T>? = null

    /**
     * Add [data] to the top of the stack
     */
    override fun push(data: T) {
        val newNode = StackNode<T>(data, top)
        top = newNode

    }

    /**
     * Remove the element at the top of the stack.  If the stack is empty, it remains unchanged.
     * @return the value at the top of the stack or nil if none exists
     */
    override fun pop(): T? {
        if (top?.data == null) {
            return null
        } else{
            top = top?.next
            return top?.data
        }

    }


    /**
     * @return the value on the top of the stack or nil if none exists
     */
    override fun peek(): T? {
        return top?.data
    }


    /**
     * @return true if the stack is empty and false otherwise
     */
    override fun isEmpty(): Boolean {
        if (top?.data == null) {
            return true
        }
        return false

    }

}

interface Queue<T> {
    /**
     * Add [data] to the end of the queue.
     */
    fun enqueue(data: T)

    /**
     * Remove the element at the front of the queue.  If the queue is empty, it remains unchanged.
     * @return the value at the front of the queue or nil if none exists
     */
    fun dequeue(): T?

    /**
     * @return the value at the front of the queue or nil if none exists
     */
    fun peek(): T?

    /**
     * @return true if the queue is empty and false otherwise
     */
    fun isEmpty(): Boolean

}