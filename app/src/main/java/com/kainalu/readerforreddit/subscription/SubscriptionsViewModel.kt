package com.kainalu.readerforreddit.subscription

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kainalu.readerforreddit.auth.UserManager
import com.kainalu.readerforreddit.models.UserData
import com.kainalu.readerforreddit.network.models.Subreddit
import kotlinx.coroutines.launch
import javax.inject.Inject

class SubscriptionsViewModel @Inject constructor(
    private val userManager: UserManager
): ViewModel() {

    private val _subscriptions = MutableLiveData<List<Subreddit>>()
    val subscriptions: LiveData<List<Subreddit>>
        get() = _subscriptions
    private lateinit var user: UserData

    init {
        getSubscriptions()
    }

    private fun withUser(f: suspend (UserData) -> Unit) {
        viewModelScope.launch {
            if (!::user.isInitialized) {
                user = userManager.getCurrentUser()
            }
            f.invoke(user)
        }
    }

    fun getSubscriptions() {
        withUser { user ->
            // TODO handle HttpExceptions when retrieving subscriptions
            _subscriptions.postValue(user.getSubscriptions().sortedBy { it.displayName.toLowerCase() })
        }
    }
}