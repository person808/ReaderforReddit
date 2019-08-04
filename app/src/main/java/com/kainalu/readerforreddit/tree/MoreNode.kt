package com.kainalu.readerforreddit.tree

import com.kainalu.readerforreddit.network.models.More

class MoreNode(more: More) : AbstractNode<More>(more), HideableItem {

    override var visibility: VisibilityState = VisibilityState.VISIBLE

    override fun addChild(node: AbstractNode<*>) {
        throw UnsupportedOperationException("MoreChildrenNodes cannot have children")
    }

    override fun addChild(index: Int, node: AbstractNode<*>) {
        throw UnsupportedOperationException("MoreChildrenNodes cannot have children")
    }

    override fun removeChild(node: AbstractNode<*>): AbstractNode<*> {
        throw UnsupportedOperationException("MoreChildrenNodes do not have children")
    }

    override fun removeChildAt(index: Int): AbstractNode<*> {
        throw UnsupportedOperationException("MoreChildrenNodes do not have children")
    }

    override fun replaceChildAt(index: Int, newNode: AbstractNode<*>) {
        throw UnsupportedOperationException("MoreChildrenNodes do not have children")
    }
}