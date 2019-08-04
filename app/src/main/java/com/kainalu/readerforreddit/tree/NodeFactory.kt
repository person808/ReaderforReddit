package com.kainalu.readerforreddit.tree

import com.kainalu.readerforreddit.models.LinkData
import com.kainalu.readerforreddit.network.models.Comment
import com.kainalu.readerforreddit.network.models.More

object NodeFactory {

    /**
     * Creates a new instance of an [AbstractNode] based on the data we want the node to hold.
     *
     * @param data The data used to instantiate fields of the node.
     * @param depth The node's depth in the tree.
     */
    fun <T> create(data: T, depth: Int): AbstractNode {
        return when (data) {
            is LinkData -> LinkNode(linkData = data, depth = depth)
            is Comment -> CommentNode(comment = data, depth = depth)
            is More -> MoreNode(more = data, depth = depth)
            else -> throw IllegalArgumentException("No node registered for this type.")
        }
    }
}