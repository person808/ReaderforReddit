package com.kainalu.readerforreddit.network.models

import org.threeten.bp.LocalDateTime

/**
 * Interface for all objects that are user created.
 */
interface Created {
    /** The time in local epoch seconds */
    val created: LocalDateTime
    /** The time in UTC epoch seconds */
    val createdUtc: LocalDateTime
}