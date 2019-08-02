package com.kainalu.readerforreddit.tree

import com.kainalu.readerforreddit.network.models.Comment

class CommentNode(comment: Comment) : AbstractSubmissionNode<Comment>(comment) {

    override fun addChild(node: AbstractSubmissionNode<*>) {
        checkNodeType(node)
        super.addChild(node)
    }

    override fun addChild(index: Int, node: AbstractSubmissionNode<*>) {
        checkNodeType(node)
        super.addChild(index, node)
    }

    private fun checkNodeType(node: AbstractSubmissionNode<*>) {
        if (node !is CommentNode && node !is MoreNode) {
            throw IllegalArgumentException("Only CommentNodes and MoreNodes can be children of a CommentNode.")
        }
    }
}
