package com.kainalu.readerforreddit.tree

import com.kainalu.readerforreddit.network.models.More

class MoreNode(more: More) : AbstractSubmissionNode<More>(more) {

    override fun addChild(node: AbstractSubmissionNode<*>) {
        throw UnsupportedOperationException("MoreChildrenNodes cannot have children")
    }

    override fun addChild(index: Int, node: AbstractSubmissionNode<*>) {
        throw UnsupportedOperationException("MoreChildrenNodes cannot have children")
    }

    override fun removeChild(node: AbstractSubmissionNode<*>): AbstractSubmissionNode<*> {
        throw UnsupportedOperationException("MoreChildrenNodes do not have children")
    }

    override fun removeChildAt(index: Int): AbstractSubmissionNode<*> {
        throw UnsupportedOperationException("MoreChildrenNodes do not have children")
    }

    override fun replaceChildAt(index: Int, newNode: AbstractSubmissionNode<*>) {
        throw UnsupportedOperationException("MoreChildrenNodes do not have children")
    }
}