package com.kainalu.readerforreddit.auth

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kainalu.readerforreddit.network.Resource
import com.kainalu.readerforreddit.user.UserManager
import com.kainalu.readerforreddit.util.Event
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthViewModel @Inject constructor(private val userManager: UserManager) : ViewModel() {

    enum class NavigationEvent {
        POP_TO_FEED
    }

    private val _navigationLiveData = MediatorLiveData<Event<NavigationEvent>>()
    val navigationLiveData: LiveData<Event<NavigationEvent>>
        get() = _navigationLiveData

    fun handleCallback(url: Uri) {
        url.getQueryParameter("code")?.let { code ->
            viewModelScope.launch {
                _navigationLiveData.addSource(userManager.addUser(code)) {
                    when (it) {
                        is Resource.Success -> _navigationLiveData.postValue(Event(NavigationEvent.POP_TO_FEED))
                        is Resource.Loading -> Unit
                        is Resource.Error -> Unit // TODO
                    }
                }
            }
        }
    }
}