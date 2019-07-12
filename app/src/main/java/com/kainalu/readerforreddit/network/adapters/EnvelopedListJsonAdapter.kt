package com.kainalu.readerforreddit.network.adapters

import com.kainalu.readerforreddit.network.models.Enveloped
import com.squareup.moshi.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class EnvelopedListJsonAdapter(private val delegate: JsonAdapter<Any>) : JsonAdapter<List<Any>>() {

    override fun fromJson(reader: JsonReader): List<Any>? {
        return reader.readArrayToList {
            delegate.fromJson(reader)
        }
    }

    override fun toJson(writer: JsonWriter, value: List<Any>?) {
        writer.writeArray(value) {
            delegate.toJson(it)
        }
    }

    companion object {
        val FACTORY = object : Factory {
            override fun create(type: Type, annotations: MutableSet<out Annotation>, moshi: Moshi): JsonAdapter<*>? {
                if (!List::class.java.isAssignableFrom(Types.getRawType(type))) {
                    return null
                }
                Types.nextAnnotations(annotations, Enveloped::class.java) ?: return null
                val itemType = (type as ParameterizedType).actualTypeArguments[0]
                // Get instance of EnvelopeJsonAdapter for this type
                val delegate = moshi.nextAdapter<Any>(this, itemType, annotations)
                return EnvelopedListJsonAdapter(delegate)
            }
        }
    }
}