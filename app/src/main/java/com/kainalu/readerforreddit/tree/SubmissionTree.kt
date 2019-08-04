package com.kainalu.readerforreddit.tree

import com.kainalu.readerforreddit.models.LinkData
import com.kainalu.readerforreddit.network.models.Comment
import com.kainalu.readerforreddit.network.models.More
import com.kainalu.readerforreddit.network.models.SubmissionItem

class SubmissionTree private constructor(root: AbstractNode, comments: List<SubmissionItem>) :
    AbstractTree(root) {

    init {
        comments.forEach { item ->
            attachComments(root, item)
        }
    }

    override fun addChild(parent: AbstractNode, child: AbstractNode) {
        assertNotLink(child)
        super.addChild(parent, child)
    }

    override fun <T> addChild(parent: AbstractNode, data: T) {
        assertNotLink(data)
        super.addChild(parent, data)
    }

    override fun addChild(parent: AbstractNode, index: Int, child: AbstractNode) {
        assertNotLink(child)
        super.addChild(parent, index, child)
    }

    override fun <T> addChild(parent: AbstractNode, index: Int, data: T) {
        assertNotLink(data)
        super.addChild(parent, index, data)
    }

    private fun attachComments(parent: AbstractNode, item: SubmissionItem) {
        if (item !is Comment && item !is More) {
            throw IllegalArgumentException("Item must be an instance of `Link` or `More`")
        }

        val node = NodeFactory.create(item)
        addChild(parent, node)

        if (item is Comment) {
            item.replies.children.forEach {
                attachComments(node, it)
            }
        }
    }

    private fun <T> assertNotLink(obj: T) {
        if (obj is LinkData || obj is LinkNode) {
            throw IllegalArgumentException("Links are only allowed as the root of this tree")
        }
    }

    class Builder : AbstractTree.Builder<SubmissionTree> {

        private var root: AbstractNode? = null
        private var comments: List<SubmissionItem> = emptyList()

        fun setComments(comments: List<SubmissionItem>): Builder {
            this.comments = comments
            return this
        }

        fun setRoot(link: LinkData): Builder {
            root = LinkNode(link)
            return this
        }

        override fun build(): SubmissionTree {
            root?.let {
                return SubmissionTree(it, comments)
            } ?: throw IllegalStateException("setRoot() must be called before build()")
        }
    }
}