package com.kainalu.readerforreddit.models

import com.kainalu.readerforreddit.network.models.Link

data class LinkDataImpl(private val link: Link) : LinkData {

    override val author = link.author
    override val createdUtc = link.createdUtc
    override val domain = link.domain
    override val id = link.id
    override val name = link.name
    override val numComments = link.numComments
    override val postHint = link.postHint
    override val preview = link.preview
    override val score = link.score
    override val selftext = link.selftext
    override val subreddit = link.subreddit
    override val title = link.title
    override val url = link.url
}