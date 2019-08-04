package com.kainalu.readerforreddit

import com.kainalu.readerforreddit.tree.AbstractNode
import com.kainalu.readerforreddit.tree.AbstractTree
import org.junit.Assert.assertEquals
import org.junit.Test

class SubmissionTreeTest {

    class AbstractNodeImpl(depth: Int) : AbstractNode(depth)

    class AbstractTreeImpl : AbstractTree(AbstractNodeImpl(0)) {
        override var size: Int = 1
    }

    // We aren't testing depth so just use 0
    private fun newNode() = AbstractNodeImpl(0)

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
        val tree = AbstractTreeImpl()
        val root = tree.root

        fun newNodeOfSize7(): AbstractNodeImpl = newNode().apply {
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
        val tree = AbstractTreeImpl()
        val z = tree.root
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