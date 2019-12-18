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
import com.kainalu.readerforreddit.tree.VisibilityState
import com.kainalu.readerforreddit.util.KotlinEpoxyHolder
import com.kainalu.readerforreddit.util.getFormattedString
import com.kainalu.readerforreddit.util.getPostTime
import org.threeten.bp.LocalDateTime

@EpoxyModelClass(layout = R.layout.comment)
abstract class CommentModel : EpoxyModelWithHolder<CommentHolder>() {

    @EpoxyAttribute
    lateinit var author: String
    @EpoxyAttribute
    lateinit var body: String
    @EpoxyAttribute
    lateinit var createdTime: LocalDateTime
    @EpoxyAttribute
    var depth: Int = 2
    @EpoxyAttribute
    var numChildren: Int = 0
    @EpoxyAttribute
    var score: Int = 0
    @EpoxyAttribute
    var scoreHidden: Boolean = false
    @EpoxyAttribute
    lateinit var visibility: VisibilityState
    @EpoxyAttribute
    lateinit var onClick: View.OnClickListener

    override fun bind(holder: CommentHolder) {
        super.bind(holder)
        with(holder.container) {
            val indicatorWidth =
                context.resources.getDimensionPixelSize(R.dimen.comment_depth_indicator)
            // Subtract 2 from depth because top level comments have a depth of 2 in our tree
            setPaddingRelative(indicatorWidth * (depth - 2), 0, 0, 0)
            setOnClickListener(onClick)
        }
        holder.authorTextView.text = author
        with(holder.timeTextView) {
            text = createdTime.getPostTime(context)
        }
        holder.bodyTextView.text = body
        collapseComment(holder)
    }

    override fun bind(holder: CommentHolder, previouslyBoundModel: EpoxyModel<*>) {
        super.bind(holder, previouslyBoundModel)
        collapseComment(holder)
    }

    private fun collapseComment(holder: CommentHolder) {
        if (visibility == VisibilityState.COLLAPSED) {
            holder.bodyTextView.maxLines = 1
            holder.bodyTextView.ellipsize = TextUtils.TruncateAt.END
            with(holder.scoreTextView) {
                text = context.getString(R.string.children_hidden, numChildren)
            }
            holder.scoreTimeSeparator.visibility = View.GONE
            holder.timeTextView.visibility = View.GONE
        } else if (visibility == VisibilityState.VISIBLE) {
            setScore(holder.scoreTextView)
            holder.bodyTextView.maxLines = Int.MAX_VALUE
            holder.bodyTextView.ellipsize = null
            holder.scoreTimeSeparator.visibility = View.VISIBLE
            holder.timeTextView.visibility = View.VISIBLE
        }
    }

    private fun setScore(textView: TextView) {
        with(textView) {
            text = if (scoreHidden) {
                context.getString(R.string.score_hidden)
            } else {
                context.resources.getQuantityString(
                    R.plurals.points,
                    score,
                    score.getFormattedString()
                )
            }
        }
    }
}

class CommentHolder : KotlinEpoxyHolder() {
    val container by bind<ConstraintLayout>(R.id.commentContainer)
    val authorTextView by bind<TextView>(R.id.authorTextView)
    val scoreTextView by bind<TextView>(R.id.scoreTextView)
    val scoreTimeSeparator by bind<TextView>(R.id.bulletSeparator2)
    val timeTextView by bind<TextView>(R.id.timeTextView)
    val bodyTextView by bind<TextView>(R.id.bodyTextView)
}