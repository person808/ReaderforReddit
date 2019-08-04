package com.kainalu.readerforreddit.tree

import com.kainalu.readerforreddit.network.models.More

class MoreNode(more: More, depth: Int) : AbstractNode(depth), HideableItem {

    override var visibility: VisibilityState = VisibilityState.VISIBLE

    val childIds = more.children
    val count = more.count
    val id = more.id
    val name = more.name
    val parentId = more.parentId

    override fun addChild(node: AbstractNode) {
        throw UnsupportedOperationException("MoreChildrenNodes cannot have children")
    }

    override fun addChild(index: Int, node: AbstractNode) {
        throw UnsupportedOperationException("MoreChildrenNodes cannot have children")
    }

    override fun removeChild(node: AbstractNode): AbstractNode {
        throw UnsupportedOperationException("MoreChildrenNodes do not have children")
    }

    override fun removeChildAt(index: Int): AbstractNode {
        throw UnsupportedOperationException("MoreChildrenNodes do not have children")
    }

    override fun replaceChildAt(index: Int, newNode: AbstractNode) {
        throw UnsupportedOperationException("MoreChildrenNodes do not have children")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MoreNode

        if (visibility != other.visibility) return false
        if (childIds != other.childIds) return false
        if (count != other.count) return false
        if (id != other.id) return false
        if (name != other.name) return false
        if (parentId != other.parentId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = visibility.hashCode()
        result = 31 * result + childIds.hashCode()
        result = 31 * result + count
        result = 31 * result + id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + parentId.hashCode()
        return result
    }

    override fun toString(): String {
        return "MoreNode(visibility=$visibility, childIds=$childIds, count=$count, id='$id', name='$name', parentId='$parentId')"
    }
}