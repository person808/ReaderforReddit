package com.kainalu.readerforreddit.tree

import com.kainalu.readerforreddit.models.LinkData
import com.kainalu.readerforreddit.network.models.Comment
import com.kainalu.readerforreddit.network.models.SubmissionItem

class LinkNode(linkData: LinkData) : AbstractNode(), LinkData by linkData {

    init {
        depth = 0
    }

    constructor(linkData: LinkData, comments: Collection<SubmissionItem>): this(linkData) {
        for (comment in comments) {
            attachComments(this, comment)
        }
    }

    override fun addChild(node: AbstractNode) {
        assertNotLink(node)
        super.addChild(node)
    }

    override fun addChild(index: Int, node: AbstractNode) {
        assertNotLink(node)
        super.addChild(index, node)
    }

    override fun replaceChildAt(index: Int, newNode: AbstractNode) {
        assertNotLink(newNode)
        super.replaceChildAt(index, newNode)
    }

    override fun replaceChild(oldChild: AbstractNode, newChild: AbstractNode) {
        assertNotLink(newChild)
        super.replaceChild(oldChild, newChild)
    }

    /**
     * Returns the flattened list of comments for this link
     */
    fun getFlattenedComments(): List<AbstractNode> {
        return flatten().drop(1)
    }


    companion object {

        private fun assertNotLink(obj: AbstractNode) {
            require(obj !is LinkData && obj !is LinkNode) { "Links are not allowed as a parameter here" }
        }

        private fun attachComments(root: AbstractNode, item: SubmissionItem) {
            val node = NodeFactory.create(item)
            root.addChild(node)
            if (item is Comment) {
                for (comment in item.replies.children) {
                    attachComments(node, comment)
                }
            }
        }
    }
}
