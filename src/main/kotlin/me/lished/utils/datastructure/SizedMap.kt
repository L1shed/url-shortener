package me.lished.utils.datastructure

typealias Node<K, V> = DLLNode<K, V>

// Works like LRU Cache, using Doubly Linked List
class SizedMap<K, V>(private val maxSize: Int) {

    private val map = HashMap<K, Node<K, V>>(maxSize)

    private val dummyHead = Node<K, V>(null, null)
    private val dummyTail = Node<K, V>(null, null)
    private var size: Int = 0

    init {
        dummyHead.next = dummyTail
        dummyTail.prev = dummyHead
    }

    fun get(key: K): V? {
        map[key]?.let {
            moveToHead(it)
            return it.value
        }

        return null
    }

    fun put(key: K, value: V) {
        if (map[key] == null) {
            val newNode = Node<K, V>(key, value)
            map[key] = newNode
            addFirst(newNode)
        } else {
            val node = map[key]!!
            node.value = value
            moveToHead(node)
        }
    }

    private fun addFirst(node: Node<K, V>) {
        val next = dummyHead.next
        node.prev = dummyHead
        dummyHead.next = node

        node.next = next
        next?.prev = node

        if (++size > maxSize) removeLast()
    }

    private fun moveToHead(node: Node<K, V>) {
        removeNode(node)
        addFirst(node)
    }

    private fun removeLast(): Node<K, V>? {
        dummyTail.prev?.let {
            if (it == dummyHead) return null
            val key = it.key
            map.remove(key)
            return removeNode(it)
        }

        return null
    }

    private fun removeNode(node: Node<K, V>): Node<K, V> {
        val next = node.next
        val prev = node.prev

        prev?.next = next
        next?.prev = prev

        size--

        return node
    }

}