package com.kainalu.readerforreddit.network.adapters

import com.kainalu.readerforreddit.network.models.EditInfo
import com.kainalu.readerforreddit.network.models.Edited
import com.kainalu.readerforreddit.network.models.NotEdited
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader
import com.squareup.moshi.ToJson
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset


class EditInfoJsonAdapter {
    @FromJson
    fun fromJson(reader: JsonReader): EditInfo {
        val value = reader.readJsonValue()
        return when (value) {
            is Long -> Edited(LocalDateTime.ofInstant(Instant.ofEpochSecond(value), ZoneId.of("UTC")))
            else -> NotEdited()
        }
    }

    @ToJson
    fun toJson(editInfo: EditInfo): Long {
        return when (editInfo) {
            is Edited -> editInfo.time.toEpochSecond(ZoneOffset.UTC)
            // this should technically be false, but we never convert to Json anyway so it doesn't matter
            else -> -1
        }
    }
}