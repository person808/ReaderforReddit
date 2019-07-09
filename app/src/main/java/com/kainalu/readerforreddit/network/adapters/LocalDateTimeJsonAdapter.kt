package com.kainalu.readerforreddit.network.adapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset

class LocalDateTimeJsonAdapter {
    @FromJson
    fun fromJson(long: Long): LocalDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(long), ZoneId.of("UTC"))

    @ToJson
    fun toJson(localDateTime: LocalDateTime): Long = localDateTime.toEpochSecond(ZoneOffset.UTC)
}