package com.kainalu.readerforreddit.network.adapters

import com.kainalu.readerforreddit.network.annotations.LegacyRedditResponse
import com.squareup.moshi.*
import java.lang.reflect.Type

class LegacyRedditResponseJsonAdapter(private val delegate: JsonAdapter<Any>) : JsonAdapter<Any>() {

    private val options = JsonReader.Options.of("json", "data", "things")

    override fun fromJson(reader: JsonReader): Any? {
        var result: Any? = null
        reader.readObject {
            when (reader.selectName(options)) {
                0 -> reader.readObject {
                    when (reader.selectName(options)) {
                        1 -> reader.readObject {
                            when (reader.selectName(options)) {
                                2 -> result = delegate.fromJson(reader)
                                -1 -> reader.skipNameAndValue()
                            }
                        }
                        -1 -> reader.skipNameAndValue()
                    }
                }
                -1 -> reader.skipNameAndValue()
            }
        }

        return result
    }

    override fun toJson(writer: JsonWriter, value: Any?) {
        writer.writeObject {
            writer.name("json")
            writer.writeObject {
                writer.name("data")
                writer.writeObject {
                    writer.name("things")
                    delegate.toJson(writer, value)
                }
            }
        }
    }

    companion object {
        val FACTORY = object : Factory {
            override fun create(type: Type, annotations: MutableSet<out Annotation>, moshi: Moshi): JsonAdapter<*>? {
                val delegateAnnotations = Types.nextAnnotations(annotations, LegacyRedditResponse::class.java) ?: return null
                val delegate = moshi.nextAdapter<Any>(this, type, delegateAnnotations)
                return LegacyRedditResponseJsonAdapter(delegate)
            }
        }
    }
}