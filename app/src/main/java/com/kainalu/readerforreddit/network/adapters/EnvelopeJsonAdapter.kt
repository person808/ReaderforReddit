package com.kainalu.readerforreddit.network.adapters

import com.kainalu.readerforreddit.network.models.DataType
import com.kainalu.readerforreddit.network.models.Envelope
import com.squareup.moshi.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class EnvelopeJsonAdapter(
    moshi: Moshi,
    type: Type
) : JsonAdapter<Envelope<*>>() {

    private val envelopedType = (type as ParameterizedType).actualTypeArguments[0]

    private val dataTypeDelegate: JsonAdapter<DataType> =
            moshi.adapter(DataType::class.java, emptySet(), "kind")

    private val dataDelegate: JsonAdapter<Any> =
            moshi.adapter(envelopedType, emptySet(), "data")

    override fun fromJson(reader: JsonReader): Envelope<*> {
        val path = reader.path
        val properties = reader.readJsonValue() as Map<*, *>? ?: throw JsonDataException("Expected object at $path")
        val kind = properties["kind"] ?: throw JsonDataException("Non-null value 'kind' was null at $path")
        val data = properties["data"] ?: throw JsonDataException("Non-null value 'data' was null at $path")

        val dataType =
            dataTypeDelegate.fromJsonValue(kind) ?: throw JsonDataException("Required property 'kind' missing at $path")

        DataType.classMap.getValue(Types.getRawType(envelopedType)).also { expectedType ->
            if (dataType != expectedType) {
                throw JsonTypeMismatchException("Expected kind ${expectedType.name} but got ${dataType.name} instead")
            }
        }

        val envelopedObj = dataDelegate.fromJsonValue(data)
            ?: throw JsonDataException("Required property 'data' missing at $path")

        return Envelope(kind = dataType, data = envelopedObj)
    }

    override fun toJson(writer: JsonWriter, value: Envelope<*>?) {
        if (value == null) {
            throw NullPointerException("value was null! Wrap in .nullSafe() to write nullable values.")
        }
        writer.writeObject {
            writer.name("kind")
            dataTypeDelegate.toJson(writer, value.kind)
            writer.name("data")
            dataDelegate.toJson(writer, value.data)
        }
    }

    companion object {
        val FACTORY = object : Factory {
            override fun create(type: Type, annotations: MutableSet<out Annotation>, moshi: Moshi): JsonAdapter<*>? {
                if (!Envelope::class.java.isAssignableFrom(Types.getRawType(type))) {
                    return null
                }

                return EnvelopeJsonAdapter(moshi, type)
            }
        }
    }
}
