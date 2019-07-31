package com.kainalu.readerforreddit.submission

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

/**
 * A [RecyclerView.ItemDecoration] for comments.
 *
 * @see DividerItemDecoration
 */
class CommentItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val a = context.obtainStyledAttributes(ATTRS)
    private val divider = a.getDrawable(0)

    init {
        a.recycle()
    }

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(canvas, parent, state)

        if (parent.layoutManager == null || divider == null) {
            return
        }

        val bounds = Rect()
        canvas.save()
        var left: Int
        val right: Int
        //noinspection AndroidLintNewApi - NewApi lint fails to handle overrides.
        if (parent.clipToPadding) {
            left = parent.paddingLeft
            right = parent.width - parent.paddingRight
            canvas.clipRect(
                left, parent.paddingTop, right,
                parent.height - parent.paddingBottom
            )
        } else {
            right = parent.width
        }

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            parent.getDecoratedBoundsWithMargins(child, bounds)
            val bottom = bounds.top - child.translationY.roundToInt()
            val top = bottom - divider.intrinsicHeight
            left = child.paddingLeft
            divider.setBounds(left, top, right, bottom)
            divider.draw(canvas)
        }
        canvas.restore()
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.set(0, 0, 0, 0)
    }

    companion object {
        private val ATTRS = intArrayOf(android.R.attr.listDivider)
    }
}
