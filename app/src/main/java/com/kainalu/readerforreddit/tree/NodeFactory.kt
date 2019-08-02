package com.kainalu.readerforreddit.tree

import com.kainalu.readerforreddit.network.models.Comment
import com.kainalu.readerforreddit.network.models.Link
import com.kainalu.readerforreddit.network.models.More

object NodeFactory {

    fun <T> create(data: T): AbstractNode<*> {
        return when (data) {
            is Link -> LinkNode(link = data)
            is Comment -> CommentNode(comment = data)
            is More -> MoreNode(more = data)
            else -> throw IllegalArgumentException("No node registered for this type.")
        }
    }
}