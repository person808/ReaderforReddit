package com.kainalu.readerforreddit.tree

import com.kainalu.readerforreddit.network.models.Comment

class CommentNode(comment: Comment) : AbstractNode<Comment>(comment), HideableItem {

    override var visibility: VisibilityState = if (comment.collapsed) {
        VisibilityState.COLLAPSED
    } else {
        VisibilityState.VISIBLE
    }

    override fun addChild(node: AbstractNode<*>) {
        checkNodeType(node)
        super.addChild(node)
    }

    override fun addChild(index: Int, node: AbstractNode<*>) {
        checkNodeType(node)
        super.addChild(index, node)
    }

    private fun checkNodeType(node: AbstractNode<*>) {
        if (node !is CommentNode && node !is MoreNode) {
            throw IllegalArgumentException("Only CommentNodes and MoreNodes can be children of a CommentNode.")
        }
    }
}
