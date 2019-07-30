package com.kainalu.readerforreddit.submission.viewholders

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.kainalu.readerforreddit.R
import com.kainalu.readerforreddit.network.models.Comment
import com.kainalu.readerforreddit.network.models.countChildren
import com.kainalu.readerforreddit.util.KotlinEpoxyHolder

@EpoxyModelClass(layout = R.layout.collapsed_comment)
abstract class CollapsedCommentModel : EpoxyModelWithHolder<CollapsedCommentHolder>() {

    @EpoxyAttribute lateinit var comment: Comment
    @EpoxyAttribute lateinit var onClick: View.OnClickListener

    override fun bind(holder: CollapsedCommentHolder) {
        super.bind(holder)
        with(holder.container) {
            val indicatorWidth = context.resources.getDimensionPixelSize(R.dimen.comment_depth_indicator)
            setPaddingRelative(indicatorWidth * (comment.depth - 1), 0, 0, 0)
            setOnClickListener(onClick)
        }
        holder.authorTextView.text = comment.author
        with(holder.hiddenTextView) {
            text = context.getString(R.string.children_hidden, comment.countChildren())
        }
        holder.previewTextView.text = comment.body
    }
}

class CollapsedCommentHolder : KotlinEpoxyHolder() {
    val container by bind<LinearLayout>(R.id.commentContainer)
    val authorTextView by bind<TextView>(R.id.authorTextView)
    val hiddenTextView by bind<TextView>(R.id.hiddenTextView)
    val previewTextView by bind<TextView>(R.id.previewTextView)
}
