package com.kainalu.readerforreddit.network.adapters

import com.kainalu.readerforreddit.network.models.EditInfo
import com.kainalu.readerforreddit.network.models.Edited
import com.kainalu.readerforreddit.network.models.NotEdited
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset


class EditInfoJsonAdapter {

    @FromJson
    fun fromJson(reader: JsonReader): EditInfo {
        return when (val value = reader.readJsonValue()) {
            is Double -> Edited(LocalDateTime.ofInstant(Instant.ofEpochSecond(value.toLong()), ZoneId.of("UTC")))
            else -> NotEdited()
        }
    }

    @ToJson
    fun toJson(writer: JsonWriter, value: EditInfo?) {
        when (value) {
            is Edited -> writer.value(value.time.toEpochSecond(ZoneOffset.UTC))
            is NotEdited -> writer.value(false)
        }
    }
}