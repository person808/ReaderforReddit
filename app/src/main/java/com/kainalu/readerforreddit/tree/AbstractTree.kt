package com.kainalu.readerforreddit.tree

/**
 * A tree whose nodes contain an arbitrary number of ordered children.
 */
abstract class AbstractTree(var root: AbstractNode) {

    /** The number of nodes in the tree */
    val size: Int
        get() = root.size()

    init {
        root.depth = 0
    }

    /**
     * Add child to parent's children.
     *
     * @param parent The parent node
     * @param child The child node to add
     */
    open fun addChild(parent: AbstractNode, child: AbstractNode) {
        parent.addChild(child)
        setNodeDepth(child, parent.depth + 1)
    }

    /**
     * Creates a node for the given data and adds it to a node's children.
     *
     * @param parent The parent node
     * @param data The data to be held in the new node.
     */
    open fun <T> addChild(parent: AbstractNode, data: T) {
        val child = NodeFactory.create(data)
        parent.addChild(child)
        setNodeDepth(child, parent.depth + 1)
    }

    /**
     * Add child to parent's children at a given index.
     *
     * @param parent The parent node
     * @param index The index of the parents children to insert the child at
     * @param child The child node to add
     */
    open fun addChild(parent: AbstractNode, index: Int, child: AbstractNode) {
        parent.addChild(index, child)
        setNodeDepth(child, parent.depth + 1)
    }

    /**
     * Creates a node for the given data and adds it to a node's children at a given index.
     *
     * @param parent The parent node
     * @param index The index of the parents children to insert the child at
     * @param data The data to be held in the new node.
     */
    open fun <T> addChild(parent: AbstractNode, index: Int, data: T) {
        val child = NodeFactory.create(data)
        parent.addChild(index, child)
        setNodeDepth(child, parent.depth + 1)
    }

    /**
     * Removes a child node from a node's children.
     *
     * @param parent The parent node
     * @param child The child node to remove
     * @return The removed node
     */
    fun removeChild(parent: AbstractNode, child: AbstractNode): AbstractNode {
        return parent.removeChild(child)
    }

    /**
     * Removes a child node at a given index from a node's children.
     *
     * @param parent The parent node
     * @param index The index of the child to remove
     * @return The removed node
     */
    fun removeChildAt(parent: AbstractNode, index: Int): AbstractNode {
        return parent.removeChildAt(index)
    }

    /**
     * Removes a child node from a node's children and adds a new child node at the same
     * index of the removed child node
     *
     * @param parent The parent node
     * @param oldChild The child node to remove
     * @param newChild The child node to add
     */
    fun replaceChild(
        parent: AbstractNode,
        oldChild: AbstractNode,
        newChild: AbstractNode
    ) {
        val index = parent.children.indexOf(oldChild)
        replaceChildAt(parent, index, newChild)
    }

    /**
     * Removes a child node from a node's children and adds new children nodes starting
     * at the same index of the removed child node
     *
     * @param parent The parent node
     * @param oldChild The child node to remove
     * @param newChildren A list of new children nodes to add
     */
    fun replaceChild(
        parent: AbstractNode,
        oldChild: AbstractNode,
        newChildren: List<AbstractNode>
    ) {
        var index = parent.children.indexOf(oldChild)
        removeChild(parent, oldChild)
        newChildren.forEach {
            addChild(parent, index++, it)
        }
    }

    /**
     * Removes a child node at a given index from a node's children and adds a new child node
     * at that index
     *
     * @param parent The parent node
     * @param index The index of the child node to remove
     * @param newChild The child node to add
     */
    fun replaceChildAt(parent: AbstractNode, index: Int, newChild: AbstractNode) {
        parent.removeChildAt(index)
        parent.addChild(index, newChild)
        setNodeDepth(newChild, parent.depth + 1)
    }

    private fun setNodeDepth(node: AbstractNode, depth: Int) {
        node.depth = depth
        node.children.forEach {
            setNodeDepth(it, depth + 1)
        }
    }

    /**
     * Flattens the tree into a list using pre-order traversal
     *
     * @return A list of nodes
     */
    fun flatten(): List<AbstractNode> {
        val output = ArrayList<AbstractNode>(size)
        flattenNode(root, output)
        return output
    }

    private fun flattenNode(node: AbstractNode, output: MutableList<AbstractNode>) {
        output.add(node)
        node.children.forEach {
            flattenNode(it, output)
        }
    }

    interface Builder<T : AbstractTree> {
        fun build(): T
    }
}