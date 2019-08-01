package com.kainalu.readerforreddit.network.annotations

import com.squareup.moshi.JsonQualifier

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@JsonQualifier
annotation class LegacyRedditResponse
