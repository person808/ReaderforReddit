package com.kainalu.readerforreddit.network.adapters

import com.kainalu.readerforreddit.network.models.Envelope
import com.kainalu.readerforreddit.network.models.Enveloped
import com.squareup.moshi.*
import java.lang.reflect.Type

class EnvelopeJsonAdapter(private val delegate: JsonAdapter<Envelope<*>>) : JsonAdapter<Any>() {

    override fun fromJson(reader: JsonReader): Any? {
        return delegate.fromJson(reader)?.data
    }

    override fun toJson(writer: JsonWriter, value: Any?) {
        delegate.toJson(writer, Envelope(value))
    }

    companion object {
        val FACTORY = object : Factory {
            override fun create(type: Type, annotations: MutableSet<out Annotation>, moshi: Moshi): JsonAdapter<*>? {
                val delegateAnnotations = Types.nextAnnotations(annotations, Enveloped::class.java) ?: return null
                val envelope = Types.newParameterizedType(Envelope::class.java, type)
                val delegate = moshi.nextAdapter<Envelope<*>>(this, envelope, delegateAnnotations)
                return EnvelopeJsonAdapter(delegate)
            }
        }
    }
}