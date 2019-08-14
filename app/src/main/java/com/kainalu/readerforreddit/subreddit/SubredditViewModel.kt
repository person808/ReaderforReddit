package com.kainalu.readerforreddit.subreddit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.kainalu.readerforreddit.feed.Duration
import com.kainalu.readerforreddit.feed.FeedRepository
import com.kainalu.readerforreddit.feed.SubredditSort
import com.kainalu.readerforreddit.models.LinkData
import kotlinx.coroutines.launch
import javax.inject.Inject

class SubredditViewModel @Inject constructor(private val feedRepository: FeedRepository) : ViewModel() {

    private val _feed = MutableLiveData<PagedList<LinkData>>()
    val feed: LiveData<PagedList<LinkData>>
        get() = _feed
    private val _viewState = MutableLiveData<SubredditViewState>()
    val viewState: LiveData<SubredditViewState>
        get() = _viewState
    private val currentViewState: SubredditViewState
        get() = _viewState.value!!

    fun init(subreddit: String) {
        if (_viewState.value != null) {
            return
        }
        _viewState.value = SubredditViewState(
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