package com.kainalu.readerforreddit.network

import com.kainalu.readerforreddit.auth.SavedToken
import com.kainalu.readerforreddit.models.TokenData
import com.kainalu.readerforreddit.network.models.Token

interface TokenManager {

    /**
     * Retrieves an unauthenticated token for use when there is no logged in user.
     * This method does not save the token. Use [saveToken] to save the token to disk.
     *
     * @return The unauthenticated token.
     */
    suspend fun retrieveLoggedOutToken(): Token


    /**
     * Retrieves an authenticated token for a logged in user.
     * This method does not save the token. Use [saveToken] to save the token to disk.
     *
     * @return The authenticated token.
     */
    suspend fun retrieveLoggedInToken(code: String, redirectUri: String): TokenData

    /**
     * Refreshes the saved token for a user and saves the new token to disk.
     *
     * @param userId The userId of the user associated with the token we are trying to refresh.
     * @return The new token.
     */
    suspend fun refreshToken(userId: String): TokenData

    fun getActiveTokenId(): String
    fun setActiveTokenId(userId: String)

    /**
     * Saves a token to the disk.
     *
     * @param token The token to save
     */
    suspend fun saveToken(token: SavedToken)

    /**
     * Retrieves the token for a user saved on disk.
     *
     * @param userId The userId of the user associated with the token we are trying to retrieve.
     * @return The token if there is one saved, null otherwise.
     */
    suspend fun getToken(userId: String): TokenData?

    /**
     * Deletes a token for a user from the disk.
     *
     * @param userId The userId of the user associated with the token to delete.
     */
    suspend fun deleteToken(userId: String)

    companion object {
        const val LOGGED_OUT_TOKEN_ID = "logged_out"
    }
}
