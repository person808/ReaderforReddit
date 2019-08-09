package com.kainalu.readerforreddit.network.models

import com.kainalu.readerforreddit.models.TokenData
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Token(
    @Json(name = "access_token")
    override val accessToken: String,
    @Json(name = "token_type")
    override val tokenType: String,
    @Json(name = "expires_in")
    override val secondsUntilExpiration: Long,
    @Json(name = "scope")
    override val scope: String,
    @Json(name = "refresh_token")
    override val refreshToken: String? = null
) : TokenData
