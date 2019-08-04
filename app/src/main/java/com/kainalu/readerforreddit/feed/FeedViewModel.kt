package com.kainalu.readerforreddit.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.kainalu.readerforreddit.models.LinkData
import kotlinx.coroutines.launch
import javax.inject.Inject

class FeedViewModel @Inject constructor(private val feedRepository: FeedRepository) : ViewModel() {

    private val _feed = MutableLiveData<PagedList<LinkData>>()
    val feed: LiveData<PagedList<LinkData>>
        get() = _feed
    private val _viewState = MutableLiveData<FeedViewState>()
    val viewState: LiveData<FeedViewState>
        get() = _viewState
    private val currentViewState: FeedViewState
        get() = _viewState.value!!

    fun init(subreddit: String = "") {
        _viewState.value = FeedViewState(
            subreddit = subreddit,
            sort = feedRepository.getDefaultSort(subreddit),
            availableSorts = feedRepository.getAvailableSorts(subreddit)
        )
        initDataSource(subreddit, currentViewState.sort, currentViewState.sortDuration)
    }

    private fun initDataSource(subreddit: String, sort: SubredditSort, sortDuration: Duration) {
        _viewState.value = currentViewState.copy(loading = true)

        viewModelScope.launch {
            _feed.value = feedRepository.getPagedList(subreddit, sort, sortDuration, viewModelScope)
            _viewState.value = currentViewState.copy(loading = false)
        }
    }

    fun refresh() {
        with(currentViewState) {
            initDataSource(subreddit, sort, sortDuration)
        }
    }

    fun setSort(sort: SubredditSort, duration: Duration = Duration.NONE) {
        if (currentViewState.sort == sort && currentViewState.sortDuration == duration) {
            return
        }

        _viewState.value = currentViewState.copy(sort = sort, sortDuration = duration)
        refresh()
    }
}