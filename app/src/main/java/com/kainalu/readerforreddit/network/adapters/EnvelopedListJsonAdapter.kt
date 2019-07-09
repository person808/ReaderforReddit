package com.kainalu.readerforreddit.network.adapters

import com.kainalu.readerforreddit.network.models.Enveloped
import com.squareup.moshi.*
import java.lang.reflect.Type

class EnvelopedListJsonAdapter<T>(private val delegate: JsonAdapter<T>): JsonAdapter<List<T>>() {

    override fun fromJson(reader: JsonReader): List<T>? {
        return reader.readArrayToList {
            delegate.fromJson(reader)
        }
    }

    override fun toJson(writer: JsonWriter, value: List<T>?) {
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
                val itemType = Types.getRawType(Types.collectionElementType(type, List::class.java))
                val delegate = moshi.nextAdapter<Any>(this, itemType, annotations)
                return EnvelopedListJsonAdapter(delegate)
            }
        }
    }
}