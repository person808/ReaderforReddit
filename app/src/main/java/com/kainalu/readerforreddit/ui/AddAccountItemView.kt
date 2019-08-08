package com.kainalu.readerforreddit.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.kainalu.readerforreddit.R

class AddAccountItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
): LinearLayout(context, attrs, defStyle, defStyleRes) {

    init {
        LayoutInflater.from(context).inflate(R.layout.add_account_item, this, true)
        isClickable = true
        isFocusable = true
        val height = context.resources.getDimensionPixelOffset(R.dimen.bottom_sheet_item_height)
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, height)
        orientation = HORIZONTAL
        addRipple()
    }
}