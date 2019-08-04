package com.kainalu.readerforreddit.tree

/**
 * A node that can contain an arbitrary number of ordered children.
 * It is preferred to use the methods in [AbstractTree] to manipulate tree data
 * instead of directly manipulating the nodes using the methods in this class.
 *
 * @see AbstractTree
 */
abstract class AbstractNode {

    private val _children: MutableList<AbstractNode> = mutableListOf()

    /** The children of this node */
    val children: List<AbstractNode>
        get() = _children

    /** The depth of a node in a tree. -1 if the node is not attached to a tree */
    var depth = -1

    /** The immediate parent of this node */
    var parent: AbstractNode? = null

    /**
     * Adds a child node to this node.
     *
     * @param node The node to add
     * @see SubmissionTree.addChild
     */
    open fun addChild(node: AbstractNode) {
        _children.add(node)
        node.parent = this
    }

    /**
     * Adds a child node to this node at a given index.
     *
     * @param index The index of [children] where the new node is added
     * @param node The node to add
     * @see SubmissionTree.addChild
     */
    open fun addChild(index: Int, node: AbstractNode) {
        _children.add(index, node)
        node.parent = this
    }

    /**
     * Removes a child of this node.
     *
     * @param node The node to remove
     * @see SubmissionTree.removeChild
     */
    open fun removeChild(node: AbstractNode): AbstractNode {
        _children.remove(node)
        return node
    }

    /**
     * Removes a child of this node at a given index of this node's children.
     *
     * @param index The index of the node to remove in [children]
     * @see SubmissionTree.removeChildAt
     */
    open fun removeChildAt(index: Int): AbstractNode {
        return _children.removeAt(index)
    }

    /**
     * Removes a child node at a given index and adds a new child node at the same index.
     *
     * @param index The index of the node to replace in [children]
     * @param newNode The new child node to add at [index]
     * @see AbstractTree.replaceChild
     * @see AbstractTree.removeChildAt
     */
    open fun replaceChildAt(index: Int, newNode: AbstractNode) {
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