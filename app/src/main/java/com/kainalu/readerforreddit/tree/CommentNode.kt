package com.kainalu.readerforreddit.tree

import com.kainalu.readerforreddit.network.models.Comment

class CommentNode(comment: Comment, depth: Int) : AbstractNode(depth), HideableItem {

    override var visibility: VisibilityState = if (comment.collapsed) {
        VisibilityState.COLLAPSED
    } else {
        VisibilityState.VISIBLE
    }

    val author = comment.author
    val body = comment.body
    val createdUtc = comment.createdUtc
    val id = comment.id
    val name = comment.name
    val score = comment.score
    val scoreHidden = comment.scoreHidden

    override fun addChild(node: AbstractNode) {
        checkNodeType(node)
        super.addChild(node)
    }

    override fun addChild(index: Int, node: AbstractNode) {
        checkNodeType(node)
        super.addChild(index, node)
    }

    private fun checkNodeType(node: AbstractNode) {
        if (node !is CommentNode && node !is MoreNode) {
            throw IllegalArgumentException("Only CommentNodes and MoreNodes can be children of a CommentNode.")
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CommentNode

        if (visibility != other.visibility) return false
        if (author != other.author) return false
        if (body != other.body) return false
        if (createdUtc != other.createdUtc) return false
        if (id != other.id) return false
        if (name != other.name) return false
        if (score != other.score) return false
        if (scoreHidden != other.scoreHidden) return false

        return true
    }

    override fun hashCode(): Int {
        var result = visibility.hashCode()
        result = 31 * result + author.hashCode()
        result = 31 * result + body.hashCode()
        result = 31 * result + createdUtc.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + score
        result = 31 * result + scoreHidden.hashCode()
        return result
    }

    override fun toString(): String {
        return "CommentNode(visibility=$visibility, author='$author', body='$body', createdUtc=$createdUtc, id='$id', name='$name', score=$score, scoreHidden=$scoreHidden)"
    }
}
