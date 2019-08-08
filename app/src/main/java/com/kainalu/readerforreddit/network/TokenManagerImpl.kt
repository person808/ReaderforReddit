package com.kainalu.readerforreddit.network

import android.content.SharedPreferences
import androidx.core.content.edit
import com.kainalu.readerforreddit.network.models.Token
import java.util.*
import javax.inject.Inject

class TokenManagerImpl @Inject constructor(private val sharedPreferences: SharedPreferences)
    : TokenManager {

    override fun saveToken(token: Token) {
        sharedPreferences.edit {
            token.apply {
                putString(ACCESS_TOKEN, accessToken)
                putString(TOKEN_TYPE, tokenType)
                putLong(DURATION, secondsUntilExpiration)
                putString(SCOPE, scope)
                putString(REFRESH_TOKEN, refreshToken)
            }
        }
    }

    override fun getToken(): Token? =
        sharedPreferences.run {
            val accessToken = getString(ACCESS_TOKEN, null) ?: return null
            val tokenType = getString(TOKEN_TYPE, null) ?: return null
            val duration = getLong(DURATION, -1)
            val scope = getString(SCOPE, null) ?: return null
            val refreshToken = getString(REFRESH_TOKEN, null)
            Token(
                accessToken = accessToken,
                tokenType = tokenType,
                secondsUntilExpiration = duration,
                scope = scope,
                refreshToken = refreshToken
            )
        }

    override fun deleteToken(token: Token) {
        sharedPreferences.edit(commit = true) {
            listOf(ACCESS_TOKEN, TOKEN_TYPE, DURATION, SCOPE, REFRESH_TOKEN).map { remove(it) }
        }
    }

    override fun getDeviceId(): String {
        var id = sharedPreferences.getString(DEVICE_ID, null)
        if (id == null) {
            id = UUID.randomUUID().toString()
            sharedPreferences.edit { putString(DEVICE_ID, id) }
        }
        return id
    }

    companion object {
        private const val ACCESS_TOKEN = "access_token"
        private const val TOKEN_TYPE = "token_type"
        private const val DURATION = "duration"
        private const val SCOPE = "scope"
        private const val REFRESH_TOKEN = "refresh_token"
        private const val DEVICE_ID = "device_id"
    }
}