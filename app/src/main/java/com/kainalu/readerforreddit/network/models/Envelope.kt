package com.kainalu.readerforreddit.network.models

import com.squareup.moshi.JsonQualifier

data class Envelope<T>(val data: T)

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class Enveloped