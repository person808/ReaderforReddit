package com.kainalu.readerforreddit.network.adapters

import com.kainalu.readerforreddit.network.models.RedditModel
import com.squareup.moshi.*
import java.io.IOException
import java.lang.reflect.Type

/**
 * A JsonAdapter factory for objects that include type information in the JSON. When decoding JSON
 * Moshi uses this type information to determine which class to decode to. When encoding Moshi uses
 * the object’s class to determine what type information to include.
 *
 * Many reddit responses are wrapped like so:
 * `
 * {
 *     "kind": "Listing",
 *     "data": {...}
 * }
 * `
 *
 * To correctly deserialize the correct type we configure this adapter like this:
 * `
 * Moshi moshi = new Moshi.Builder()
 *      .add(
 *           RedditJsonAdapterFactory.of("kind", "data")
 *                .withType(Comment::class.java, "t1")
 *                .withType(Link::class.java, "t3")
 *       )
 *      .build();
 * `
 * This class imposes strict requirements on its use:
 *
 *  * Types must be annotated with [RedditModel]
 *  * Types must encode as JSON objects.
 *  * Type information must be in the wrapper object. Each message must have a type label like
 * `kind` whose value is a string like `t1` that identifies which type to use.
 *  * Each type identifier must be unique.
 *
 *
 * If an unknown subtype is encountered when decoding, this will throw a [JsonDataException].
 * If an unknown type is encountered when encoding, this will throw an [IllegalArgumentException].
 */

class RedditJsonAdapterFactory constructor(
    private val labelKey: String,
    private val dataKey: String,
    private val labelToType: MutableMap<String, Type> = mutableMapOf(),
    private val excludedTypes: MutableSet<Type> = mutableSetOf()
) : JsonAdapter.Factory {

    /**
     * Returns a new factory that decodes instances of `type`. When an unknown type is found
     * during encoding an [IllegalArgumentException] will be thrown. When an unknown label
     * is found during decoding a [JsonDataException] will be thrown.
     */
    fun <T : Any> withType(type: Class<T>, label: String): RedditJsonAdapterFactory {
        if (label in labelToType) {
            throw IllegalArgumentException("Labels must be unique.")
        }
        if (type in excludedTypes) {
            throw IllegalArgumentException("Type $type is excluded from this adapter.")
        }
        labelToType[label] = type
        return RedditJsonAdapterFactory(labelKey, dataKey, labelToType, excludedTypes)
    }

    /**
     * Returns a new factory that will ignore instances of `type`.
     */
    fun <T : Any> excludeType(type: Class<T>): RedditJsonAdapterFactory {
        if (type in labelToType.values) {
            throw IllegalArgumentException("Type $type has a label associated with it. Either remove the label or don't exclude this type.")
        }
        excludedTypes.add(type)
        return RedditJsonAdapterFactory(labelKey, dataKey, labelToType, excludedTypes)
    }

    override fun create(type: Type, annotations: Set<Annotation>, moshi: Moshi): JsonAdapter<*>? {
        val delegateAnnotations = Types.nextAnnotations(annotations, RedditModel::class.java) ?: return null

        if (Types.getRawType(type) in excludedTypes) {
            return null
        }

        val typeToAdapter = mutableMapOf<Type, JsonAdapter<Any>>()
        for ((_, dataType) in labelToType) {
            typeToAdapter[dataType] = moshi.nextAdapter(this, dataType, delegateAnnotations)
        }

        return PolymorphicJsonAdapter(
            labelKey,
            dataKey,
            labelToType,
            typeToAdapter
        )
    }

    class PolymorphicJsonAdapter(
        private val labelKey: String,
        private val dataKey: String,
        private val labelToType: Map<String, Type>,
        private val typeToAdapter: Map<Type, JsonAdapter<Any>>
    ) : JsonAdapter<Any>() {

        @Throws(IOException::class)
        override fun fromJson(reader: JsonReader): Any? {
            val path = reader.path
            val properties = reader.readJsonValue() as Map<*, *>? ?: throw JsonDataException("Expected object at $path")
            val label = properties[labelKey] as String?
                ?: throw JsonDataException("Non-null value '$labelKey' was null at $path")
            val data = properties[dataKey] ?: throw JsonDataException("Non-null value '$dataKey' was null at $path")

            return getAdapterForLabel(label).fromJsonValue(data)
        }

        @Throws(IOException::class)
        override fun toJson(writer: JsonWriter, value: Any?) {
            val type: Type? = value?.javaClass
            val labels = labelToType.filterValues { it == type }.keys
            val label = if (labels.isNotEmpty()) {
                labels.first()
            } else {
                throw IllegalArgumentException(
                    "Expected one of ${labelToType.values} but found $value, a"
                            + "$type. Register this subtype."
                )
            }

            writer.beginObject()
            writer.name(labelKey).value(label)
            writer.name(dataKey)
            typeToAdapter[type]?.toJson(writer, value)
            writer.endObject()
        }

        private fun getAdapterForLabel(label: String): JsonAdapter<Any> {
            val type = labelToType[label] ?: throw JsonDataException(
                "Expected one of ${labelToType.keys}"
                        + " for key '$labelKey' but found $label. Register a subtype for this label."
            )
            return typeToAdapter[type] ?: throw IllegalArgumentException("No adapter registered for type $type")
        }

        override fun toString(): String {
            return "PolymorphicJsonAdapter($labelKey)"
        }
    }

    companion object {

        /**
         * @param labelKey The key in the JSON object whose value determines the type to which to map the
         * JSON object.
         * @param dataKey The key in the JSON object whose value contains the data to deserialize
         */
        fun of(labelKey: String, dataKey: String): RedditJsonAdapterFactory {
            return RedditJsonAdapterFactory(
                labelKey,
                dataKey
            )
        }
    }
}
