package com.kainalu.readerforreddit.network.models

enum class DataType(val key: String) {
    LISTING("Listing"),
    COMMENT("t1"),
    ACCOUNT("t2"),
    LINK("t3"),
    MESSAGE("t4"),
    SUBREDDIT("t5"),
    AWARD("t6"),
    UNKNOWN("");

    companion object {
        fun get(key: String): DataType = values().find { it.key == key } ?: UNKNOWN
        val classMap = mapOf(
            Listing::class.java to LISTING,
            Comment::class.java to COMMENT,
            Link::class.java to LINK
            ).withDefault { UNKNOWN }
    }
}
