package com.kainalu.readerforreddit.network.adapters

import com.kainalu.readerforreddit.network.models.Enveloped
import com.squareup.moshi.*
import java.lang.reflect.Type

class EnvelopeJsonAdapter<T>(private val delegate: JsonAdapter<T>) : JsonAdapter<T>() {

    override fun fromJson(reader: JsonReader): T? {
        var result: T? = null
        if (reader.peek() == JsonReader.Token.BEGIN_OBJECT) {
            reader.readObject {
                when (reader.selectName(NAMES)) {
                    0 -> result = delegate.fromJson(reader)
                    else -> reader.skipNameAndValue()
                }
            }
        } else {
            result = delegate.fromJson(reader)
        }

        return result
    }

    override fun toJson(writer: JsonWriter, value: T?) {
        writer.writeObject {
            writer.name(DATA_KEY).value(delegate.toJson(value))
        }
    }

    companion object {
        private const val DATA_KEY = "data"
        private val NAMES = JsonReader.Options.of(DATA_KEY)

        val FACTORY = object : Factory {
            override fun create(type: Type, annotations: MutableSet<out Annotation>, moshi: Moshi): JsonAdapter<*>? {
                val delegateAnnotations = Types.nextAnnotations(annotations, Enveloped::class.java) ?: return null
                val delegate = moshi.nextAdapter<Any>(this, type, delegateAnnotations)
                return EnvelopeJsonAdapter(delegate)
            }
        }
    }
}