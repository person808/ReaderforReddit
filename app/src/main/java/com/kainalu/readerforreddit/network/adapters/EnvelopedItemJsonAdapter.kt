package com.kainalu.readerforreddit.network.adapters

import android.util.Log
import com.kainalu.readerforreddit.network.models.DataType
import com.kainalu.readerforreddit.network.models.Envelope
import com.kainalu.readerforreddit.network.models.Enveloped
import com.squareup.moshi.*
import java.lang.reflect.Type

class EnvelopedItemJsonAdapter(private val delegate: JsonAdapter<Envelope<*>>) : JsonAdapter<Any>() {

    override fun fromJson(reader: JsonReader): Any? {
        return try {
            delegate.fromJson(reader)?.data
        } catch (e: JsonTypeMismatchException) {
            Log.d(TAG, "Mismatched JSON types. ${e.message}")
            null
        }
    }

    override fun toJson(writer: JsonWriter, value: Any?) {
        // we dont really care about serializing so just put UNKNOWN here
        delegate.toJson(writer, Envelope(DataType.UNKNOWN, value))
    }

    companion object {
        private val TAG = "EnvelopedItemAdapter"
        val FACTORY = object : Factory {
            override fun create(type: Type, annotations: MutableSet<out Annotation>, moshi: Moshi): JsonAdapter<*>? {
                val delegateAnnotations = Types.nextAnnotations(annotations, Enveloped::class.java) ?: return null
                val envelope = Types.newParameterizedType(Envelope::class.java, type)
                val delegate = moshi.nextAdapter<Envelope<*>>(this, envelope, delegateAnnotations)
                return EnvelopedItemJsonAdapter(delegate)
            }
        }
    }
}