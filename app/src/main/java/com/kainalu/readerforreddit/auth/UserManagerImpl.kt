package com.kainalu.readerforreddit.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.kainalu.readerforreddit.BuildConfig
import com.kainalu.readerforreddit.cache.AppDatabase
import com.kainalu.readerforreddit.models.LoggedOutUser
import com.kainalu.readerforreddit.models.User
import com.kainalu.readerforreddit.models.UserData
import com.kainalu.readerforreddit.network.ApiService
import com.kainalu.readerforreddit.network.Resource
import com.kainalu.readerforreddit.network.TokenManager
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserManagerImpl @Inject constructor(
    private val apiService: ApiService,
    private val database: AppDatabase,
    private val tokenManager: TokenManager
) : UserManager {

    override fun isLoggedIn(): Boolean {
        return tokenManager.getActiveTokenId() != TokenManager.LOGGED_OUT_TOKEN_ID
    }

    override suspend fun getCurrentUser(): UserData {
        val userId = tokenManager.getActiveTokenId()
        return if (userId == TokenManager.LOGGED_OUT_TOKEN_ID) {
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

        try {
            val token = tokenManager.retrieveLoggedInToken(code, BuildConfig.REDIRECT_URI)
            // store the token temporarily with a temporary userId so we can request the actual userId
            tokenManager.saveToken(SavedToken.fromToken(TEMP_USER_ID, token))
            tokenManager.setActiveTokenId(TEMP_USER_ID)

            val user = User.fromAccount(apiService.me())
            database.userDao().insertAll(user)
            database.tokenDao().insertAll(SavedToken.fromToken(user.id, token))
            database.tokenDao().deleteByUserId(TEMP_USER_ID)
            tokenManager.setActiveTokenId(user.id)
            emit(Resource.Success(user))
        } catch (e: HttpException) {
            Log.e(TAG, e.message(), e)
            emit(Resource.Error<User>(error = e))
        }
    }

    override fun switchToUser(user: UserData) {
        tokenManager.setActiveTokenId(user.id)
    }

    companion object {
        private const val TEMP_USER_ID = "temp_user_id"
        private const val TAG = "UserManager"
    }
}