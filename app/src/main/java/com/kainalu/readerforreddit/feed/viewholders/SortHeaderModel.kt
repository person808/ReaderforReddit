package com.kainalu.readerforreddit.feed.viewholders

import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.kainalu.readerforreddit.R
import com.kainalu.readerforreddit.util.KotlinEpoxyHolder

@EpoxyModelClass(layout = R.layout.sort_header)
abstract class SortHeaderModel : EpoxyModelWithHolder<Holder>() {

    @EpoxyAttribute lateinit var label: String
    @EpoxyAttribute lateinit var onClick: View.OnClickListener

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.label.text = label
        holder.label.setOnClickListener(onClick)
    }
}

class Holder : KotlinEpoxyHolder() {
    val label by bind<TextView>(R.id.labelTextView)
}