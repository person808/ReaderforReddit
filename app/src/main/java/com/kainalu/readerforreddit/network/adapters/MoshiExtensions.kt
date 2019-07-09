package com.kainalu.readerforreddit.network.adapters

import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter

fun JsonReader.skipNameAndValue() {
    skipName()
    skipValue()
}

inline fun JsonReader.readObject(body: () -> Unit) {
    beginObject()
    while (hasNext()) {
        body()
    }
    endObject()
}

inline fun JsonReader.readArray(body: () -> Unit) {
    beginArray()
    while (hasNext()) {
        body()
    }
    endArray()
}

inline fun <T> JsonReader.readArrayToList(body: () -> T?): List<T> {
    val result = mutableListOf<T>()
    beginArray()

    while (hasNext()) {
        body()?.let { result.add(it) }
    }
    endArray()

    return result
}

inline fun JsonWriter.writeObject(body: () -> Unit) {
    beginObject()
    body()
    endObject()
}

inline fun <T> JsonWriter.writeArray(values: List<T>?, body: (value: T) -> String) {
    beginArray()
    values?.forEach { value(body(it)) }
    endArray()
}