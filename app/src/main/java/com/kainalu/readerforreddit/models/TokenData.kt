package com.kainalu.readerforreddit.models

interface TokenData {
    val accessToken: String
    val tokenType: String
    val secondsUntilExpiration: Long
    val scope: String
    val refreshToken: String?
}