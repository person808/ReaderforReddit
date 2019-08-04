package com.kainalu.readerforreddit.tree

import com.kainalu.readerforreddit.models.LinkData
import com.kainalu.readerforreddit.network.models.Comment
import com.kainalu.readerforreddit.network.models.More

object NodeFactory {

    /**
     * Creates a new instance of an [AbstractNode] with no parent.
     *
     * @param data The data used to instantiate fields of the node.
     */
    fun <T> create(data: T): AbstractNode {
        return when (data) {
            is LinkData -> LinkNode(linkData = data)
            is Comment -> CommentNode(comment = data)
            is More -> MoreNode(more = data)
            else -> throw IllegalArgumentException("No node registered for this type.")
        }
    }
}