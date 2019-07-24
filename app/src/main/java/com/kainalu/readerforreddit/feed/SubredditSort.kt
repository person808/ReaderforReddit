package com.kainalu.readerforreddit.feed

import androidx.annotation.StringRes
import com.kainalu.readerforreddit.R

/**
 * Represents different sort types for subreddits
 *
 * @param label The string resource used to display this value
 */
enum class SubredditSort(@StringRes val label: Int) {
    BEST(R.string.best_posts),
    HOT(R.string.hot_posts),
    NEW(R.string.new_posts),
    RISING(R.string.rising_posts),
    TOP(R.string.top_posts),
    CONTROVERSIAL(R.string.controversial_posts)
}
