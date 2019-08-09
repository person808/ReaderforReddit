package com.kainalu.readerforreddit.auth

import androidx.lifecycle.LiveData
import com.kainalu.readerforreddit.models.User
import com.kainalu.readerforreddit.models.UserData
import com.kainalu.readerforreddit.network.Resource

interface UserManager {

    /**
     * Returns true if the user is logged in, false otherwise.
     */
    fun isLoggedIn(): Boolean

    /**
     * Returns a list of all saved users in the order they were added.
     * The last user in the list represents an anonymous, logged out user.
     */
    suspend fun getUsers(): List<UserData>

    /**
     * Returns the current user's data.
     */
    suspend fun getCurrentUser(): UserData

    /**
     * Adds a user to the database and sets that user as the active user.
     */
    suspend fun addUser(code: String): LiveData<Resource<User>>

    /**
     * Sets a user as the active user.
     *
     * @param user The user to set as active.
     */
    fun switchToUser(user: UserData)
}