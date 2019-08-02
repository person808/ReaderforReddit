package com.kainalu.readerforreddit.tree

/**
 * A node that can contain an arbitrary number of children Do not manipulate nodes
 * directly. Instead use the methods in [AbstractSubmissionTree] to manipulate tree data.
 *
 * @see AbstractSubmissionTree
 */
abstract class AbstractSubmissionNode<T>(val data: T) {

    private val _children: MutableList<AbstractSubmissionNode<*>> = mutableListOf()

    /** The children of this node */
    val children: List<AbstractSubmissionNode<*>>
        get() = _children

    /**
     * Adds a child node to this node.
     *
     * @param node The node to add
     * @see SubmissionTree.addChild
     */
    open fun addChild(node: AbstractSubmissionNode<*>) {
        _children.add(node)
    }

    /**
     * Adds a child node to this node at a given index.
     *
     * @param index The index of [children] where the new node is added
     * @param node The node to add
     * @see SubmissionTree.addChild
     */
    open fun addChild(index: Int, node: AbstractSubmissionNode<*>) {
        _children.add(index, node)
    }

    /**
     * Removes a child of this node.
     *
     * @param node The node to remove
     * @see SubmissionTree.removeChild
     */
    open fun removeChild(node: AbstractSubmissionNode<*>): AbstractSubmissionNode<*> {
        _children.remove(node)
        return node
    }

    /**
     * Removes a child of this node at a given index of this node's children.
     *
     * @param index The index of the node to remove in [children]
     * @see SubmissionTree.removeChildAt
     */
    open fun removeChildAt(index: Int): AbstractSubmissionNode<*> {
        return _children.removeAt(index)
    }

    /**
     * Removes a child node at a given index and adds a new child node at the same index.
     *
     * @param index The index of the node to replace in [children]
     * @param newNode The new child node to add at [index]
     * @see AbstractSubmissionTree.replaceChild
     * @see AbstractSubmissionTree.removeChildAt
     */
    open fun replaceChildAt(index: Int, newNode: AbstractSubmissionNode<*>) {
        removeChildAt(index)
        _children.add(index, newNode)
    }

    /**
     * The number of nodes in the tree starting at this node.
     */
    fun size(): Int {
        var total = 1  // Always count this node
        children.forEach {
            total += it.size()
        }
        return total
    }

    /**
     * A recursive count of the number of children attached to this node
     */
    fun countChildren(): Int = size() - 1
}