package com.kainalu.readerforreddit.submission.viewholders

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.kainalu.readerforreddit.R
import com.kainalu.readerforreddit.util.KotlinEpoxyHolder

@EpoxyModelClass(layout = R.layout.view_more)
abstract class MoreModel : EpoxyModelWithHolder<MoreHolder>() {

    @EpoxyAttribute
    var depth: Int = 2
    @EpoxyAttribute
    var numChildren: Int = 0
    // Needs to be marked open to avoid compilation error
    @EpoxyAttribute
    open var isLoading: Boolean = false
    @EpoxyAttribute lateinit var onClick: View.OnClickListener

    override fun bind(holder: MoreHolder) {
        super.bind(holder)
        with(holder.container) {
            setOnClickListener(onClick)
            val indicatorWidth = context.resources.getDimensionPixelSize(R.dimen.comment_depth_indicator)
            setPaddingRelative(indicatorWidth * (depth - 2), 0, 0, 0)
        }
        setText(holder.textView)
    }

    override fun bind(holder: MoreHolder, previouslyBoundModel: EpoxyModel<*>) {
        super.bind(holder, previouslyBoundModel)
        setText(holder.textView)
    }

    private fun setText(textView: TextView) {
        with(textView) {
            text = if (isLoading) {
                context.getString(R.string.loading)
            } else {
                context.resources.getQuantityString(R.plurals.view_more, numChildren, numChildren)
            }
        }
    }
}

class MoreHolder : KotlinEpoxyHolder() {
    val container by bind<ConstraintLayout>(R.id.viewMoreContainer)
    val textView by bind<TextView>(R.id.viewMoreTextView)
}