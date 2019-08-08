package com.kainalu.readerforreddit.network

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
    suspend fun retrieveLoggedInToken(code: String, redirectUri: String): Token

    /**
     * Refreshes the saved token and saves the new token to disk.
     *
     * @return The new token.
     */
    suspend fun refreshToken(): Token

    /**
     * Saves a token to the disk.
     *
     * @param token The token to save
     */
    fun saveToken(token: Token)

    /**
     * Retrieves the token saved on disk.
     *
     * @return The token if there is one saved, null otherwise.
     */
    fun getToken(): Token?

    /**
     * Deletes a token from the disk.
     */
    fun deleteToken()
}
