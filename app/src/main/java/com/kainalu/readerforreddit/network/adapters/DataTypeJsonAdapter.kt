package com.kainalu.readerforreddit.network.adapters

import com.kainalu.readerforreddit.network.models.DataType
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class DataTypeJsonAdapter {

    @FromJson
    fun fromJson(key: String): DataType = DataType.get(key)

    @ToJson
    fun toJson(dataType: DataType): String = dataType.key
}