package com.kainalu.readerforreddit.submission.viewholders

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.kainalu.readerforreddit.R
import com.kainalu.readerforreddit.network.models.Comment
import com.kainalu.readerforreddit.util.KotlinEpoxyHolder
import com.kainalu.readerforreddit.util.getFormattedString
import com.kainalu.readerforreddit.util.getPostTime

@EpoxyModelClass(layout = R.layout.comment)
abstract class CommentModel : EpoxyModelWithHolder<CommentHolder>() {

    @EpoxyAttribute lateinit var comment: Comment
    @EpoxyAttribute lateinit var onClick: View.OnClickListener

    override fun bind(holder: CommentHolder) {
        super.bind(holder)
        with(comment) {
            with(holder.container) {
                val indicatorWidth = context.resources.getDimensionPixelSize(R.dimen.comment_depth_indicator)
                setPaddingRelative(indicatorWidth * (depth - 1), 0, 0, 0)
                setOnClickListener(onClick)
            }
            holder.authorTextView.text = author
            with(holder.scoreTextView) {
                text = if (scoreHidden) {
                    context.getString(R.string.score_hidden)
                } else {
                    context.resources.getQuantityString(R.plurals.points, score, score.getFormattedString())
                }
            }
            with(holder.timeTextView) {
                text = createdUtc.getPostTime(context)
            }
            holder.bodyTextView.text = comment.body
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