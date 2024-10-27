package me.lished.utils.datastructure

class DLLNode<K, V>(val key: K?, var value: V?) {
    var prev: DLLNode<K, V>? = null
    var next: DLLNode<K, V>? = null

    fun remove(): DLLNode<K, V> {
        prev?.next = next
        next?.prev = prev

        return this
    }
}