package com.kainalu.readerforreddit.user

interface UserManager {

    fun isLoggedIn(): Boolean
    suspend fun getUsers(): List<UserData>
    suspend fun getCurrentUser(): UserData
    fun switchToUser(username: String)
}