package com.kainalu.readerforreddit.user

import android.content.SharedPreferences
import com.kainalu.readerforreddit.cache.AppDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserManagerImpl @Inject constructor(
    private val database: AppDatabase,
    private val sharedPreferences: SharedPreferences
): UserManager {

    override fun isLoggedIn(): Boolean {
        return sharedPreferences.getString(CURRENT_USER_ID, null) != null
    }

    override suspend fun getCurrentUser(): UserData {
        val userId = sharedPreferences.getString(CURRENT_USER_ID, null)
        return if (userId == null) {
            LoggedOutUser()
        } else {
            database.userDao().getById(userId)
        }
    }

    override suspend fun getUsers(): List<UserData> {
        val savedUsers: MutableList<UserData> = database.userDao().getAll().toMutableList()
        savedUsers.add(LoggedOutUser())
        return savedUsers
    }

    override fun switchToUser(username: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        private const val CURRENT_USER_ID = "current_user_id"
    }
}