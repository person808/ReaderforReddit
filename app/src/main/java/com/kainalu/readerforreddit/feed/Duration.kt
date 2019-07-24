package com.kainalu.readerforreddit.feed

import com.kainalu.readerforreddit.R

/**
 * Represents different sorting durations
 *
 * @param string The string representation used in url requests
 * @param label The string resource used to display this value
 */
enum class Duration(val string: String, val label: Int) {
    HOUR("hour", R.string.past_hour),
    DAY("day", R.string.past_day),
    WEEK("week", R.string.past_week),
    MONTH("month", R.string.past_month),
    YEAR("year", R.string.past_year),
    ALL("all", R.string.past_all),
    NONE("", -1) // Label should never be used
}