package com.kainalu.readerforreddit.auth

import android.content.SharedPreferences
import androidx.core.content.edit
import com.kainalu.readerforreddit.cache.AppDatabase
import com.kainalu.readerforreddit.models.TokenData
import com.kainalu.readerforreddit.network.AuthService
import com.kainalu.readerforreddit.network.TokenManager
import com.kainalu.readerforreddit.network.models.Token
import java.util.*
import javax.inject.Inject

class RoomTokenManager @Inject constructor(
    private val authService: AuthService,
    private val database: AppDatabase,
    private val sharedPreferences: SharedPreferences
) : TokenManager {

    override suspend fun retrieveLoggedInToken(code: String, redirectUri: String): Token {
        return authService.getToken(code = code, redirectUri = redirectUri)
    }

    override suspend fun refreshToken(userId: String): Token {
        val refreshToken = database.tokenDao().getById(userId)?.refreshToken
        val token = if (refreshToken == null) {
            authService.getLoggedOutToken(deviceId = getDeviceId())
        } else {
            authService.refreshToken(refreshToken = refreshToken)
        }
        saveToken(SavedToken.fromToken(userId, token))
        return token
    }

    override suspend fun retrieveLoggedOutToken(): Token {
        return authService.getLoggedOutToken(deviceId = getDeviceId())
    }

    override fun getActiveTokenId(): String {
        return sharedPreferences.getString(ACTIVE_ID, TokenManager.LOGGED_OUT_TOKEN_ID)!!
    }

    override fun setActiveTokenId(userId: String) {
        sharedPreferences.edit {
            putString(ACTIVE_ID, userId)
        }
    }

    override suspend fun saveToken(token: SavedToken) {
        database.tokenDao().insertAll(token)
    }

    override suspend fun getToken(userId: String): TokenData? =
        database.tokenDao().getById(userId)

    override suspend fun deleteToken(userId: String) {
        database.tokenDao().deleteByUserId(userId)
    }

    private fun getDeviceId(): String {
        var id = sharedPreferences.getString(DEVICE_ID, null)
        if (id == null) {
            id = UUID.randomUUID().toString()
            sharedPreferences.edit { putString(DEVICE_ID, id) }
        }
        return id
    }

    companion object {
        private const val ACTIVE_ID = "active_id"
        private const val DEVICE_ID = "device_id"
    }
}