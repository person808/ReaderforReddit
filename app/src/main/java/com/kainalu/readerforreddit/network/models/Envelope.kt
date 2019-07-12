package com.kainalu.readerforreddit.network.models

import com.squareup.moshi.JsonClass
import com.squareup.moshi.JsonQualifier

@JsonClass(generateAdapter = true)
data class Envelope<T>(val data: T)

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class Enveloped