package com.kainalu.readerforreddit.network

import com.kainalu.readerforreddit.network.models.Token

interface SessionManager {
    fun saveToken(token: Token)
    fun getToken(): Token?
    fun deleteToken(token: Token)
    fun isLoggedIn(): Boolean
    fun getDeviceId(): String
}
