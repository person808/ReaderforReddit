package com.kainalu.readerforreddit.user

import androidx.lifecycle.LiveData
import com.kainalu.readerforreddit.network.Resource
import com.kainalu.readerforreddit.network.models.Token

interface UserManager {

    fun isLoggedIn(): Boolean
    suspend fun getUsers(): List<UserData>
    suspend fun getCurrentUser(): UserData
    suspend fun addUser(code: String): LiveData<Resource<User>>
    fun switchToUser(user: UserData, newToken: Token)
}