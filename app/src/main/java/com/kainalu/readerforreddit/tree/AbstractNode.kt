package com.kainalu.readerforreddit.tree

/**
 * A node that can contain an arbitrary number of ordered children.
 */
abstract class AbstractNode {

    private val _children: MutableList<AbstractNode> = mutableListOf()

    /** The children of this node */
    val children: List<AbstractNode>
        get() = _children

    /** The depth of a node in a tree. -1 if the node is not attached to a tree */
    var depth = -1
        set(value) {
            field = value
            for (_child in _children) {
                _child.depth = depth + 1
            }
        }

    /**
     * The number of nodes in the tree starting at this node.
     */
    val size: Int
        get() {
            var total = 1  // Always count this node
            for (child in _children) {
                total += child.size
            }
            return total
        }

    /** The immediate parent of this node */
    var parent: AbstractNode? = null

    /**
     * Adds a child node to this node.
     *
     * @param node The node to add
     */
    open fun addChild(node: AbstractNode) {
        _children.add(node)
        node.parent = this
        node.depth = depth + 1
    }

    /**
     * Adds a child node to this node at a given index.
     *
     * @param index The index of [children] where the new node is added
     * @param node The node to add
     */
    open fun addChild(index: Int, node: AbstractNode) {
        _children.add(index, node)
        node.parent = this
        node.depth = depth + 1
    }

    /**
     * Removes a child of this node.
     *
     * @param node The node to remove
     */
    open fun removeChild(node: AbstractNode): AbstractNode {
        _children.remove(node)
        return node
    }

    /**
     * Removes a child of this node at a given index of this node's children.
     *
     * @param index The index of the node to remove in [children]
     */
    open fun removeChildAt(index: Int): AbstractNode {
        return _children.removeAt(index)
    }

    /**
     * Removes a child node at a given index and adds a new child node at the same index.
     *
     * @param index The index of the node to replace in [children]
     * @param newNode The new child node to add at [index]
     */
    open fun replaceChildAt(index: Int, newNode: AbstractNode) {
        removeChildAt(index)
        _children.add(index, newNode)
        newNode.depth = depth + 1
    }

    /**
     * Removes a child node from this node's children and adds a new child node at the same
     * index of the removed child node
     *
     * @param oldChild The child node to remove
     * @param newChild The child node to add
     */
    open fun replaceChild(oldChild: AbstractNode, newChild: AbstractNode) {
        val index = _children.indexOf(oldChild)
        replaceChildAt(index, newChild)
    }

    /**
     * Removes a child node from this node's children and adds new children nodes starting
     * at the same index of the removed child node
     *
     * @param oldChild The child node to remove
     * @param newChildren A list of new children nodes to add
     */
    fun replaceChild(
        oldChild: AbstractNode,
        newChildren: List<AbstractNode>
    ) {
        var index = _children.indexOf(oldChild)
        removeChildAt(index)
        for (child in newChildren) {
            addChild(index++, child)
        }
    }

    /**
     * Flattens the tree into a list using pre-order traversal
     *
     * @return A list of nodes
     */
    fun flatten(): List<AbstractNode> {
        val output = ArrayList<AbstractNode>(size)
        flattenNode(this, output)
        return output
    }

    private fun flattenNode(node: AbstractNode, output: MutableList<AbstractNode>) {
        output.add(node)
        for (childNode in node._children) {
            flattenNode(childNode, output)
        }
    }

    private fun setNodeDepth(node: AbstractNode, depth: Int) {
        node.depth = depth
        for (child in node.children) {
            setNodeDepth(child, depth + 1)
        }
    }

    /**
     * A recursive count of the number of children attached to this node
     */
    fun countChildren(): Int = size - 1
}