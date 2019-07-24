package com.kainalu.readerforreddit.feed

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Config
import androidx.paging.PagedList
import com.kainalu.readerforreddit.network.models.Link
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FeedViewModel @Inject constructor(private val feedRepository: FeedRepository) : ViewModel() {

    private val _feed = MutableLiveData<PagedList<Link>>()
    val feed: LiveData<PagedList<Link>>
        get() = _feed
    private val _viewState = MutableLiveData<FeedViewState>()
    val viewState: LiveData<FeedViewState>
        get() = _viewState
    private val currentViewState: FeedViewState
        get() = _viewState.value!!

    private val pagingConfig = Config(pageSize = 25, prefetchDistance = 50, enablePlaceholders = false)

    fun init(subreddit: String = ""): LiveData<PagedList<Link>> {
        _viewState.value = FeedViewState(
            subreddit = subreddit,
            sort = feedRepository.getDefaultSort(subreddit),
            availableSorts = feedRepository.getAvailableSorts(subreddit)
        )
        initDataSource(subreddit, currentViewState.sort, currentViewState.sortDuration)
        return feed
    }

    private fun initDataSource(subreddit: String, sort: SubredditSort, sortDuration: Duration) {
        _viewState.value = currentViewState.copy(loading = true)
        val sourceFactory = SubredditDataSourceFactory(
            feedRepository,
            subreddit,
            sort,
            sortDuration,
            viewModelScope
        )
        viewModelScope.launch {
            val pagedList = withContext(Dispatchers.IO) {
                PagedList.Builder(sourceFactory.create(), pagingConfig)
                    .setFetchExecutor(ArchTaskExecutor.getIOThreadExecutor())
                    .setNotifyExecutor(ArchTaskExecutor.getMainThreadExecutor())
                    .build()
            }

            _feed.value = pagedList
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
        initDataSource(currentViewState.subreddit, sort, duration)
    }
}