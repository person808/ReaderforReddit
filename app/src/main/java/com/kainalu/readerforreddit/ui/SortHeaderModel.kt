package com.kainalu.readerforreddit.ui

import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.kainalu.readerforreddit.R
import com.kainalu.readerforreddit.util.KotlinEpoxyHolder

@EpoxyModelClass(layout = R.layout.sort_header)
abstract class SortHeaderModel : EpoxyModelWithHolder<Holder>() {

    @EpoxyAttribute var label: String? = null
    @EpoxyAttribute @StringRes var labelRes: Int? = null
    @EpoxyAttribute lateinit var onClick: View.OnClickListener

    override fun bind(holder: Holder) {
        super.bind(holder)
        if (label == null && labelRes == null) {
            throw IllegalStateException("Either label or labelRes must be non-null")
        }
        with(holder.label) {
            text = label ?: context.getString(labelRes!!)
            setOnClickListener(onClick)
        }
    }
}

class Holder : KotlinEpoxyHolder() {
    val label by bind<TextView>(R.id.labelTextView)
}