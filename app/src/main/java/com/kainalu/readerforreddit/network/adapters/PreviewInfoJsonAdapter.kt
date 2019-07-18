package com.kainalu.readerforreddit.network.adapters

import com.kainalu.readerforreddit.network.models.PreviewInfo
import com.squareup.moshi.*

class PreviewInfoJsonAdapter {

    private val options = JsonReader.Options.of("images", "source", "url", "height", "width")

    @FromJson
    fun fromJson(reader: JsonReader): PreviewInfo {
        var url: String? = null
        var height: Int? = null
        var width: Int? = null

        // So this data is deeply nested
        reader.readObject {
            when (reader.selectName(options)) {
                0 -> reader.readArray {
                    reader.readObject {
                        when (reader.selectName(options)) {
                            1 -> reader.readObject {
                                when (reader.selectName(options)) {
                                    2 -> url = reader.nextString()
                                    3 -> height = reader.nextInt()
                                    4 -> width = reader.nextInt()
                                    -1 -> reader.skipNameAndValue()
                                }
                            }
                            -1 -> reader.skipNameAndValue()
                        }
                    }
                }
                -1 -> reader.skipNameAndValue()
            }
        }

        return PreviewInfo(
            url = url ?: throw JsonDataException("Required property 'url' missing at ${reader.path}"),
            height = height ?: throw JsonDataException("Required property 'height' missing at ${reader.path}"),
            width = width ?: throw JsonDataException("Required property 'width' missing at ${reader.path}")
        )
    }

    @ToJson
    fun toJson(writer: JsonWriter, value: PreviewInfo?) {
        // Not gonna bother with this one for now
    }
}