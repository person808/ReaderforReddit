package com.kainalu.readerforreddit.submission.viewholders

import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyModelClass
import com.kainalu.readerforreddit.R

@EpoxyModelClass(layout = R.layout.submission_self_item)
abstract class SelfSubmissionModel : BaseSubmissionModel<SelfPostHolder>() {

    override fun bind(holder: SelfPostHolder) {
        super.bind(holder)
        with(holder.selfTextView) {
            visibility = if (link.selftext.isEmpty()) View.GONE else View.VISIBLE
            text = link.selftext
        }
    }
}

class SelfPostHolder : BaseHolder() {
    val selfTextView by bind<TextView>(R.id.selfTextView)
}