package com.kainalu.readerforreddit.submission

import androidx.annotation.StringRes
import com.kainalu.readerforreddit.R

enum class SubmissionSort(@StringRes val label: Int) {
    BEST(R.string.best),
    TOP(R.string.top),
    NEW(R.string.new_sort),
    CONTROVERSIAL(R.string.controversial),
    OLD(R.string.old),
    QA(R.string.q_and_a)
}