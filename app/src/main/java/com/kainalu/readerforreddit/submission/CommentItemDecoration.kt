package com.kainalu.readerforreddit.submission

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyViewHolder
import com.kainalu.readerforreddit.submission.viewholders.CommentModel
import com.kainalu.readerforreddit.submission.viewholders.MoreModel

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

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val holder = parent.getChildViewHolder(view)
        val shouldInset = holder is EpoxyViewHolder && holder.model !is CommentModel && holder.model !is MoreModel
        if (divider != null && shouldInset) {
            outRect.set(0, 0, 0, divider.intrinsicHeight)
        } else {
            outRect.set(0, 0, 0, 0)
        }
    }

    companion object {
        private val ATTRS = intArrayOf(android.R.attr.listDivider)
    }
}
