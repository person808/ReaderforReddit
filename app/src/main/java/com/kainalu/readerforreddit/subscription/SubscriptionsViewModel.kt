package com.kainalu.readerforreddit.subscription

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class SubscriptionsViewModel @Inject constructor(
    private val subscriptionRepository: SubscriptionRepository
): ViewModel() {
    fun getDefaultSubreddits() {
        viewModelScope.launch {
            subscriptionRepository.getDefaultSubreddits()
        }
    }
}