package com.kainalu.readerforreddit.models

import com.kainalu.readerforreddit.network.models.PreviewInfo
import org.threeten.bp.LocalDateTime

/**
 * Interface to represent fields we extract from the Reddit API for link data.
 *
 * @see [com.kainalu.readerforreddit.network.models.Link]
 */
interface LinkData {
    val author: String
    val createdUtc: LocalDateTime
    val domain: String?
    val id: String
    val name: String
    val numComments: Int?
    val postHint: String?
    val preview: PreviewInfo?
    val score: Int
    val selftext: String
    val subreddit: String
    val title: String
    val url: String
}

