package com.kainalu.readerforreddit.submission.viewholders

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.kainalu.readerforreddit.R
import com.kainalu.readerforreddit.tree.MoreNode
import com.kainalu.readerforreddit.util.KotlinEpoxyHolder

@EpoxyModelClass(layout = R.layout.view_more)
abstract class MoreModel : EpoxyModelWithHolder<MoreHolder>() {

    @EpoxyAttribute lateinit var data: MoreNode
    @EpoxyAttribute lateinit var onClick: View.OnClickListener

    override fun bind(holder: MoreHolder) {
        super.bind(holder)
        with(holder.container) {
            setOnClickListener(onClick)
            val indicatorWidth = context.resources.getDimensionPixelSize(R.dimen.comment_depth_indicator)
            setPaddingRelative(indicatorWidth * (data.depth - 2), 0, 0, 0)
        }
        with(holder.textView) {
            text = context.resources.getQuantityString(R.plurals.view_more, data.count, data.count)
        }
    }
}

class MoreHolder : KotlinEpoxyHolder() {
    val container by bind<ConstraintLayout>(R.id.viewMoreContainer)
    val textView by bind<TextView>(R.id.viewMoreTextView)
}