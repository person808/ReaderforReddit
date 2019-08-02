package com.kainalu.readerforreddit.submission.viewholders

import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.kainalu.readerforreddit.R
import com.kainalu.readerforreddit.tree.CommentNode
import com.kainalu.readerforreddit.util.KotlinEpoxyHolder
import com.kainalu.readerforreddit.util.getFormattedString
import com.kainalu.readerforreddit.util.getPostTime

@EpoxyModelClass(layout = R.layout.comment)
abstract class CommentModel : EpoxyModelWithHolder<CommentHolder>() {

    @EpoxyAttribute lateinit var comment: CommentNode
    @EpoxyAttribute lateinit var onClick: View.OnClickListener

    override fun bind(holder: CommentHolder) {
        super.bind(holder)
        with(comment.data) {
            with(holder.container) {
                val indicatorWidth = context.resources.getDimensionPixelSize(R.dimen.comment_depth_indicator)
                setPaddingRelative(indicatorWidth * (depth - 1), 0, 0, 0)
                setOnClickListener(onClick)
            }
            holder.authorTextView.text = author
            with(holder.timeTextView) {
                text = createdUtc.getPostTime(context)
            }
            holder.bodyTextView.text = body
            collapseComment(holder)
        }
    }

    override fun bind(holder: CommentHolder, previouslyBoundModel: EpoxyModel<*>) {
        super.bind(holder, previouslyBoundModel)
        collapseComment(holder)
    }

    private fun collapseComment(holder: CommentHolder) {
        if (comment.data.collapsed) {
            holder.bodyTextView.maxLines = 1
            holder.bodyTextView.ellipsize = TextUtils.TruncateAt.END
            with(holder.scoreTextView) {
                text = context.getString(R.string.children_hidden, comment.countChildren())
            }
        } else {
            setScore(holder.scoreTextView)
            holder.bodyTextView.maxLines = Int.MAX_VALUE
            holder.bodyTextView.ellipsize = null
        }
    }

    private fun setScore(textView: TextView) {
        with(textView) {
            with(comment.data) {
                text = if (comment.data.scoreHidden) {
                    context.getString(R.string.score_hidden)
                } else {
                    context.resources.getQuantityString(R.plurals.points, score, score.getFormattedString())
                }
            }
        }
    }
}

class CommentHolder : KotlinEpoxyHolder() {
    val container by bind<ConstraintLayout>(R.id.commentContainer)
    val authorTextView by bind<TextView>(R.id.authorTextView)
    val scoreTextView by bind<TextView>(R.id.scoreTextView)
    val timeTextView by bind<TextView>(R.id.timeTextView)
    val bodyTextView by bind<TextView>(R.id.bodyTextView)
}