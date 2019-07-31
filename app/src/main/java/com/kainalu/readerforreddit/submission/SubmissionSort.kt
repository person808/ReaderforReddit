package com.kainalu.readerforreddit.submission

import androidx.annotation.StringRes
import com.kainalu.readerforreddit.R

enum class SubmissionSort(@StringRes val label: Int, val urlString: String) {
    BEST(R.string.best, "confidence"),
    TOP(R.string.top, "top"),
    NEW(R.string.new_sort, "new"),
    CONTROVERSIAL(R.string.controversial, "controversial"),
    OLD(R.string.old, "old"),
    QA(R.string.q_and_a, "qa")
}