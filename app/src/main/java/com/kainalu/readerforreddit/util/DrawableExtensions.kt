package com.kainalu.readerforreddit.util

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.widget.TextView

fun TextView.setDrawableTint(color: Int) {
    compoundDrawablesRelative.forEach { drawable ->
        drawable?.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    }
}