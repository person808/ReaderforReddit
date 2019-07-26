package com.kainalu.readerforreddit.network.adapters

import com.kainalu.readerforreddit.network.models.Listing
import com.squareup.moshi.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ListingJsonAdapter(private val childrenDelegate: JsonAdapter<List<Any>>) : JsonAdapter<Listing<Any>>() {

    private val wrapperOptions = JsonReader.Options.of("data")
    private val options = JsonReader.Options.of("children", "before", "after")

    override fun fromJson(reader: JsonReader): Listing<Any>? {
        var children: List<Any>? = null
        var before: String? = null
        var after: String? = null

        // Some api responses return an empty string instead of an empty listing so we must check for that
        if (reader.peek() == JsonReader.Token.STRING) {
            reader.nextString()
            return Listing.empty()
        }

        reader.readObject {
            when (reader.selectName(wrapperOptions)) {
                0 -> {
                    reader.readObject {
                        when (reader.selectName(options)) {
                            0 -> children = childrenDelegate.fromJson(reader)
                            1 -> before = reader.nextStringOrNull()
                            2 -> after = reader.nextStringOrNull()
                            -1 -> reader.skipNameAndValue()
                        }
                    }
                }
                -1 -> reader.skipNameAndValue()
            }
        }

        return Listing(
            children = children ?: throw JsonDataException("Required property 'children' missing at ${reader.path}"),
            before = before,
            after = after
        )
    }

    override fun toJson(writer: JsonWriter, value: Listing<Any>?) {
        if (value == null) {
            throw NullPointerException("value was null! Wrap in .nullSafe() to write nullable values.")
        }
        writer.writeObject {
            writer.name("kind").value("Listing")
            writer.name("data")
            writer.writeObject {
                writer.name("children")
                childrenDelegate.toJson(writer, value.children)
                writer.name("before").value(value.before)
                writer.name("after").value(value.after)
            }
        }
    }

    companion object {
        val FACTORY = object : Factory {
            override fun create(type: Type, annotations: MutableSet<out Annotation>, moshi: Moshi): JsonAdapter<*>? {
                if (!Types.getRawType(type).isAssignableFrom(Listing::class.java)) {
                    return null
                }
                val typeArgs = (type as ParameterizedType).actualTypeArguments
                val listType = Types.newParameterizedType(List::class.java, *typeArgs)
                val childrenAnnotations = Types.getFieldJsonQualifierAnnotations(Listing::class.java, "children")
                val childrenDelegete = moshi.adapter<List<Any>>(listType, childrenAnnotations)
                return ListingJsonAdapter(childrenDelegete)
            }
        }
    }
}