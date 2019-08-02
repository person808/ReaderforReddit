package com.kainalu.readerforreddit.tree

import com.kainalu.readerforreddit.network.models.Comment
import com.kainalu.readerforreddit.network.models.Link
import com.kainalu.readerforreddit.network.models.More
import com.kainalu.readerforreddit.network.models.SubmissionItem

class SubmissionTree(link: Link, comments: List<SubmissionItem>) : AbstractSubmissionTree() {

    override var root: AbstractSubmissionNode<*>? = LinkNode(link)
    val link: LinkNode
        get() = root as LinkNode

    override var size: Int = 1 // Start at 1 because we always have a root

    init {
        comments.forEach { item ->
            root?.let { root ->
                attachComments(root, item)
            }
        }
    }

    override fun addChild(parent: AbstractSubmissionNode<*>, child: AbstractSubmissionNode<*>) {
        if (child is LinkNode) {
            throw IllegalArgumentException("LinkNodes are only allowed as the root of this tree")
        }
        super.addChild(parent, child)
    }

    override fun addChild(parent: AbstractSubmissionNode<*>, index: Int, child: AbstractSubmissionNode<*>) {
        if (child is LinkNode) {
            throw IllegalArgumentException("LinkNodes are only allowed as the root of this tree")
        }
        super.addChild(parent, index, child)
    }

    private fun attachComments(parent: AbstractSubmissionNode<*>, item: SubmissionItem) {
        val node = when (item) {
            is Comment -> CommentNode(item)
            is More -> MoreNode(item)
            else -> throw IllegalArgumentException("Item must be an instance of `Link` or `More`")
        }

        addChild(parent, node)

        if (item is Comment) {
            item.replies.children.forEach {
                attachComments(node, it)
            }
        }
    }
}