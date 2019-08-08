package com.kainalu.readerforreddit

import androidx.lifecycle.ViewModel
import com.kainalu.readerforreddit.user.LoggedOutUser
import com.kainalu.readerforreddit.user.UserData
import com.kainalu.readerforreddit.user.UserManager
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class ActivityViewModel @Inject constructor(private val userManager: UserManager) : ViewModel() {

    var users = emptyList<UserData>()
    var currentUser: UserData = LoggedOutUser()

    init {
        runBlocking {
            users = userManager.getUsers()
            currentUser = userManager.getCurrentUser()
        }
    }
}