package com.kainalu.readerforreddit.tree

abstract class AbstractSubmissionTree(var root: AbstractSubmissionNode<*>) {

    /** The number of nodes in the tree */
    abstract var size: Int

    /**
     * Add child to parent's children.
     *
     * @param parent The parent node
     * @param child The child node to add
     */
    open fun addChild(parent: AbstractSubmissionNode<*>, child: AbstractSubmissionNode<*>) {
        size += child.size()
        parent.addChild(child)
    }

    /**
     * Creates a node for the given data and adds it to a node's children.
     *
     * @param parent The parent node
     * @param data The data to be held in the new node.
     */
    open fun <T> addChild(parent: AbstractSubmissionNode<*>, data: T) {
        val child = NodeFactory.create(data)
        size += child.size()
        parent.addChild(child)
    }

    /**
     * Add child to parent's children at a given index.
     *
     * @param parent The parent node
     * @param index The index of the parents children to insert the child at
     * @param child The child node to add
     */
    open fun addChild(parent: AbstractSubmissionNode<*>, index: Int, child: AbstractSubmissionNode<*>) {
        size += child.size()
        parent.addChild(index, child)
    }

    /**
     * Creates a node for the given data and adds it to a node's children at a given index.
     *
     * @param parent The parent node
     * @param index The index of the parents children to insert the child at
     * @param data The data to be held in the new node.
     */
    open fun <T> addChild(parent: AbstractSubmissionNode<*>, index: Int, data: T) {
        val child = NodeFactory.create(data)
        size += child.size()
        parent.addChild(index, child)
    }

    /**
     * Removes a child node from a node's children.
     *
     * @param parent The parent node
     * @param child The child node to remove
     * @return The removed node
     */
    fun removeChild(parent: AbstractSubmissionNode<*>, child: AbstractSubmissionNode<*>): AbstractSubmissionNode<*> {
        size -= child.size()
        return parent.removeChild(child)
    }

    /**
     * Removes a child node at a given index from a node's children.
     *
     * @param parent The parent node
     * @param index The index of the child to remove
     * @return The removed node
     */
    fun removeChildAt(parent: AbstractSubmissionNode<*>, index: Int): AbstractSubmissionNode<*> {
        val child = parent.removeChildAt(index)
        size -= child.size()
        return child
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
        parent: AbstractSubmissionNode<*>,
        oldChild: AbstractSubmissionNode<*>,
        newChild: AbstractSubmissionNode<*>
    ) {
        val index = parent.children.indexOf(oldChild)
        replaceChildAt(parent, index, newChild)
    }

    /**
     * Removes a child node at a given index from a node's children and adds a new child node
     * at that index
     *
     * @param parent The parent node
     * @param index The index of the child node to remove
     * @param newChild The child node to add
     */
    fun replaceChildAt(parent: AbstractSubmissionNode<*>, index: Int, newChild: AbstractSubmissionNode<*>) {
        val oldChild = parent.removeChildAt(index)
        size = size - oldChild.size() + newChild.size()
        parent.addChild(index, newChild)
    }

    /**
     * Flattens the tree into a list using pre-order traversal
     *
     * @return A list of nodes
     */
    fun flatten(): List<AbstractSubmissionNode<*>> {
        val output = ArrayList<AbstractSubmissionNode<*>>(size)
        flattenNode(root, output)
        return output
    }

    private fun flattenNode(node: AbstractSubmissionNode<*>, output: MutableList<AbstractSubmissionNode<*>>) {
        output.add(node)
        node.children.forEach {
            flattenNode(it, output)
        }
    }

    interface Builder<T : AbstractSubmissionTree> {
        fun build(): T
    }
}