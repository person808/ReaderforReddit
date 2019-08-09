package com.kainalu.readerforreddit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kainalu.readerforreddit.user.UserData
import com.kainalu.readerforreddit.user.UserManager
import kotlinx.coroutines.launch
import javax.inject.Inject

class AccountSwitcherViewModel @Inject constructor(private val userManager: UserManager) : ViewModel() {

    private val _userLiveData = MutableLiveData<Pair<UserData, List<UserData>>>()
    val userLiveData: LiveData<Pair<UserData, List<UserData>>>
        get() = _userLiveData

    init {
        viewModelScope.launch {
            val currentUser = userManager.getCurrentUser()
            val users = userManager.getUsers()
            _userLiveData.postValue(Pair(currentUser, users))
        }
    }
}