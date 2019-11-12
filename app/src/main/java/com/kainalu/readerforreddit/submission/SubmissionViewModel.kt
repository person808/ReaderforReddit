package com.kainalu.readerforreddit.submission

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kainalu.readerforreddit.network.Resource
import com.kainalu.readerforreddit.tree.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class SubmissionViewModel @Inject constructor(
    private val submissionRepository: SubmissionRepository
) : ViewModel() {

    private val _viewState =
        MediatorLiveData<SubmissionViewState>().apply { value = SubmissionViewState() }
    val viewState: LiveData<SubmissionViewState>
        get() = _viewState
    private val currentViewState: SubmissionViewState
        get() = _viewState.value!!

    fun init(subreddit: String, threadId: String) {
        if (currentViewState.submissionTree != null) {
            return
        }
        loadSubmission(subreddit, threadId, SubmissionSort.BEST)
    }

    fun getChildren(more: MoreNode) {
        viewModelScope.launch {
            val liveData = submissionRepository.loadChildren(
                currentViewState.submissionTree!!,
                currentViewState.sort!!,
                more
            )
            _viewState.addSource(liveData) {
                when (it) {
                    is Resource.Success -> {
                        val comments = currentViewState.submissionTree?.getFlattenedComments()
                        _viewState.postValue(currentViewState.copy(comments = comments))
                        _viewState.removeSource(liveData)
                    }
                    is Resource.Loading -> {
                        more.loading = true
                        _viewState.postValue(currentViewState) // Force update
                    }
                    is Resource.Error -> {
                        more.loading = false
                        _viewState.postValue(currentViewState) // Force update
                        _viewState.removeSource(liveData)
                    }
                }
            }
        }
    }

    fun refresh() {
        loadSubmission(
            currentViewState.subreddit,
            currentViewState.threadId,
            currentViewState.sort!!
        )
    }

    private fun loadSubmission(subreddit: String, threadId: String, sort: SubmissionSort) {
        _viewState.value = currentViewState.copy(sort = sort)
        viewModelScope.launch {
            val liveData = submissionRepository.getSubmission(subreddit, threadId, sort)
            _viewState.addSource(liveData) { submissionResource ->
                when (submissionResource) {
                    is Resource.Success -> {
                        _viewState.postValue(
                            currentViewState.copy(
                                subreddit = subreddit,
                                threadId = threadId,
                                submissionTree = submissionResource.data,
                                comments = submissionResource.data.getFlattenedComments(),
                                loading = false
                            )
                        )
                        _viewState.removeSource(liveData)
                    }
                    is Resource.Loading -> _viewState.postValue(currentViewState.copy(loading = true))
                    is Resource.Error -> {
                        _viewState.postValue(currentViewState.copy(loading = false))
                        _viewState.removeSource(liveData)
                    }
                }
            }

        }
    }

    private fun showCommentsRecursive(node: AbstractNode) {
        if (node is HideableItem) {
            if (node.visibility == VisibilityState.COLLAPSED_HIDDEN) {
                node.visibility = VisibilityState.COLLAPSED
            } else {
                node.visibility = VisibilityState.VISIBLE
                node.children.forEach {
                    showCommentsRecursive(it)
                }
            }
        }
    }

    private fun hideCommentsRecursive(node: AbstractNode) {
        if (node is HideableItem) {
            node.visibility = if (node.visibility == VisibilityState.COLLAPSED) {
                VisibilityState.COLLAPSED_HIDDEN
            } else {
                VisibilityState.HIDDEN
            }
        }
        node.children.forEach {
            hideCommentsRecursive(it)
        }
    }

    fun collapseComment(commentNode: CommentNode) {
        commentNode.visibility = VisibilityState.COLLAPSED
        commentNode.children.forEach {
            hideCommentsRecursive(it)
        }
        _viewState.value = currentViewState.copy(comments = currentViewState.comments)
    }

    fun expandComment(commentNode: CommentNode) {
        commentNode.visibility = VisibilityState.VISIBLE
        commentNode.children.forEach {
            showCommentsRecursive(it)
        }
        _viewState.value = currentViewState.copy(comments = currentViewState.comments)
    }

    fun setSort(sort: SubmissionSort) {
        if (sort != currentViewState.sort) {
            loadSubmission(currentViewState.subreddit, currentViewState.threadId, sort)
        }
    }
}