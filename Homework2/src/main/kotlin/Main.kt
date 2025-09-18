package org.example

/**
 * Sets up a node, which will act as the elements
 * of linked lists and the items in a stack and queue
 */
class Node<T>(var value: T,
              var prev: Node<T>?,
              var next: Node<T>?
)

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

class MyLinkedList<T>(): LinkedList<T> {

    /**
     * Sets up the head and tail of a linked list as references
     */
    var head: Node<T>? = null // Node(null, null, null)
    var tail: Node<T>? = null // Node(null, head, null)


    /**
     * Adds the element [data] to the front of the linked list.
     */
    override fun pushFront(data: T) {
        val newHead = Node(data, null, head)
        if (head == null) {
            head = newHead
            tail = newHead
        } else {
            head?.prev = newHead
            head = newHead
        }
    }

    /**
     * Adds the element [data] to the back of the linked list.
     */
    override fun pushBack(data: T) {
        val newTail = Node(data, tail, null)
        if (tail == null) {
            head = newTail
            tail = newTail
        } else {
            tail?.next = newTail
            tail = newTail
        }
    }

    /**
     * Removes an element from the front of the list. If the list is empty, it is unchanged.
     * @return the value at the front of the list or nil if none exists
     */
    override fun popFront(): T? {
        if (head == null && tail == null) {
            return null
        } else {
            val newHead = head?.next
            val returnable = head?.value
            head = newHead
            head?.prev = null
            return returnable
        }
    }

    /**
     * Removes an element from the back of the list. If the list is empty, it is unchanged.
     * @return the value at the back of the list or nil if none exists
     */
    override fun popBack(): T? {
        if (tail == null && head == null) {
            return null
        } else {
            val oldTail = tail
            val newTail = oldTail?.prev
            val returnable = tail?.value
            tail = newTail
            tail?.next = null
            return returnable
        }
    }

    /**
     * @return the value at the front of the list or nil if none exists
     */
    override fun peekFront(): T? {
        return head?.value

    }

    /**
     * @return the value at the back of the list or nil if none exists
     */
    override fun peekBack(): T? {
        return tail?.value
    }

    /**
     * @return true if the list is empty and false otherwise
     */
    override fun isEmpty(): Boolean {
        if (tail == null && head == null) {
            return true
        }
        return false
    }

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

    val stackList = MyLinkedList<T>()

    /**
     * * Add [data] to the top of the stack
     * */
    override fun push(data: T) {
        stackList.pushFront(data)
    }


    /**
     * Remove the element at the top of the stack.  If the stack is empty, it remains unchanged.
     * @return the value at the top of the stack or nil if none exists
     */
    override fun pop(): T? {
        return stackList.popFront()
    }

    /**
     * @return the value on the top of the stack or nil if none exists
     */
    override fun peek(): T? {
        return stackList.peekFront()
    }

    /**
     * @return true if the stack is empty and false otherwise
     */
    override fun isEmpty(): Boolean {
        return stackList.isEmpty()
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

class MyQueue<T> : Queue<T> {

    val queueList = MyLinkedList<T>()

    /**
     * Add [data] to the end of the queue.
     */
    override fun enqueue(data: T) {
        queueList.pushBack(data)
    }

    /**
     * Remove the element at the front of the queue.  If the queue is empty, it remains unchanged.
     * @return the value at the front of the queue or nil if none exists
     */
    override fun dequeue(): T? {
        return queueList.popFront()
    }

    /**
     * @return the value at the front of the queue or nil if none exists
     */
    override fun peek(): T? {
        return queueList.peekFront()
    }

    /**
     * @return true if the queue is empty and false otherwise
     */
    override fun isEmpty(): Boolean {
        return queueList.isEmpty()
    }
}
/**
Reverses the elements in a stack by removing the top element and
adding it to a new stack until the original stack is empty
 */
fun <T> reverseStack(oldStack: MyStack<T>) : MyStack<T> {
    val newStack = MyStack<T>()
    while (oldStack.peek() != null) {
        oldStack.peek()?.also { node ->
            newStack.push(node)
        }
        oldStack.pop()
    }
    return newStack
}

/**
 * Demonstrates how the function reverseStack reverses the
 * elements in a stack in this example
 */
fun main(): MyStack<Int> {
    val originalStack = MyStack<Int>()
    originalStack.push(3)
    originalStack.push(30)
    originalStack.push(300)
    originalStack.push(3000)

    return reverseStack(originalStack)
}