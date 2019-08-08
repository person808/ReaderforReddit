package com.kainalu.readerforreddit.network

import com.kainalu.readerforreddit.network.models.Token

interface TokenManager {
    fun saveToken(token: Token)
    fun getToken(): Token?
    fun deleteToken(token: Token)
    fun getDeviceId(): String
}
