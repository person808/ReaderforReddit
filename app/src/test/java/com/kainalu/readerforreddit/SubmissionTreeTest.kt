package com.kainalu.readerforreddit

import com.kainalu.readerforreddit.tree.AbstractNode
import org.junit.Assert.assertEquals
import org.junit.Test

class SubmissionTreeTest {

    class AbstractNodeImpl : AbstractNode()

    private fun newNode() = AbstractNodeImpl()

    @Test
    fun testNodeSize() {
        val node = newNode()
        assertEquals(1, node.size)

        repeat(2) {
            node.addChild(newNode())
        }
        assertEquals(3, node.size)

        repeat(2) {
            node.children[0].addChild(newNode())
            node.children[1].addChild(newNode())
        }
        assertEquals(7, node.size)
    }

    @Test
    fun testTreeSize_forCorrectness() {
        val root = AbstractNodeImpl()

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
        root.addChild(newNode)
        assertEquals(8, root.size)

        newNode.addChild(newNodeOfSize7())
        assertEquals(15, root.size)

        root.replaceChild(newNode, newNode())
        assertEquals(2, root.size)
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
        val root = AbstractNodeImpl()
        val z = root
        val a = newNode()
        val b = newNode()
        val c = newNode()
        val d = newNode()
        val e = newNode()

        root.addChild(a)
        root.addChild(b)
        a.addChild(c)
        a.addChild(d)
        a.addChild(e)

        val flattenedTree = root.flatten()
        val expected = listOf(z, a, c, d, e, b)
        assertEquals(expected, flattenedTree)
    }
}