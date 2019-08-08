package com.kainalu.readerforreddit.user

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.kainalu.readerforreddit.BuildConfig
import com.kainalu.readerforreddit.cache.AppDatabase
import com.kainalu.readerforreddit.network.ApiService
import com.kainalu.readerforreddit.network.Resource
import com.kainalu.readerforreddit.network.TokenManager
import com.kainalu.readerforreddit.network.models.Token
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserManagerImpl @Inject constructor(
    private val apiService: ApiService,
    private val database: AppDatabase,
    private val sharedPreferences: SharedPreferences,
    private val tokenManager: TokenManager
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

    override suspend fun addUser(code: String): LiveData<Resource<User>> = liveData {
        emit(Resource.Loading<User>(null))

        val token = tokenManager.retrieveLoggedInToken(code, BuildConfig.REDIRECT_URI)
        tokenManager.deleteToken()
        tokenManager.saveToken(token)
        val response = apiService.me()
        if (response.isSuccessful) {
            val user = User.fromAccount(response.body()!!)
            database.userDao().insertAll(user)
            sharedPreferences.edit {
                putString(CURRENT_USER_ID, user.id)
            }
            emit(Resource.Success(user))
        } else {
            emit(Resource.Error<User>(error = HttpException(response)))
        }
    }

    override fun switchToUser(user: UserData, newToken: Token) {
        sharedPreferences.edit {
            putString(CURRENT_USER_ID, user.id)
        }
        tokenManager.deleteToken()
        tokenManager.saveToken(newToken)
    }

    companion object {
        private const val CURRENT_USER_ID = "current_user_id"
    }
}