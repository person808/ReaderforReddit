package com.kainalu.readerforreddit

import com.kainalu.readerforreddit.tree.AbstractSubmissionNode
import com.kainalu.readerforreddit.tree.AbstractSubmissionTree
import org.junit.Assert.assertEquals
import org.junit.Test

class SubmissionTreeTest {

    class AbstractSubmissionNodeImpl : AbstractSubmissionNode<Any?>(null)

    class AbstractSubmissionTreeImpl : AbstractSubmissionTree() {
        override var root: AbstractSubmissionNode<*>? = AbstractSubmissionNodeImpl()
        override var size: Int = 1
    }

    private fun newNode() = AbstractSubmissionNodeImpl()

    @Test
    fun testNodeSize() {
        val node = newNode()
        assertEquals(1, node.size())

        repeat(2) {
            node.addChild(newNode())
        }
        assertEquals(3, node.size())

        repeat(2) {
            node.children[0].addChild(newNode())
            node.children[1].addChild(newNode())
        }
        assertEquals(7, node.size())
    }

    @Test
    fun testTreeSize_forCorrectness() {
        val tree = AbstractSubmissionTreeImpl()
        val root = tree.root!!

        fun newNodeOfSize7(): AbstractSubmissionNodeImpl = newNode().apply {
            repeat(2) {
                addChild(newNode())
            }
            repeat(2) {
                children[0].addChild(newNode())
                children[1].addChild(newNode())
            }
        }

        val newNode = newNodeOfSize7()
        tree.addChild(root, newNode)
        assertEquals(8, tree.size)

        tree.addChild(newNode, newNodeOfSize7())
        assertEquals(15, tree.size)

        tree.replaceChild(root, newNode, newNode())
        assertEquals(2, tree.size)
    }

    @Test
    fun testTreeFlatten() {
        // Make a tree with this structure:
        //        z
        //       / \
        //      a   b
        //     /|\
        //    c d e
        // output of flattening should be zacdeb
        val tree = AbstractSubmissionTreeImpl()
        val z = tree.root!!
        val a = newNode()
        val b = newNode()
        val c = newNode()
        val d = newNode()
        val e = newNode()

        tree.addChild(z, a)
        tree.addChild(z, b)
        tree.addChild(a, c)
        tree.addChild(a, d)
        tree.addChild(a, e)

        val flattenedTree = tree.flatten()
        val expected = listOf(z, a, c, d, e, b)
        assertEquals(expected, flattenedTree)
    }
}