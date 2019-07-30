package com.kainalu.readerforreddit.submission

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kainalu.readerforreddit.network.models.Comment
import com.kainalu.readerforreddit.network.models.HideableSubmissionItem
import kotlinx.coroutines.launch
import javax.inject.Inject

class SubmissionViewModel @Inject constructor(
    private val submissionRepository: SubmissionRepository
) : ViewModel() {

    private val _viewState = MutableLiveData<SubmissionViewState>().apply { value = SubmissionViewState() }
    val viewState: LiveData<SubmissionViewState>
        get() = _viewState
    private val currentViewState: SubmissionViewState
        get() = _viewState.value!!

    fun init(subreddit: String, threadId: String) {
        loadSubmission(subreddit, threadId, SubmissionSort.BEST)
    }

    private fun loadSubmission(subreddit: String, threadId: String, sort: SubmissionSort) {
        _viewState.value = currentViewState.copy(sort = sort)
        viewModelScope.launch {
            _viewState.postValue(
                currentViewState.copy(
                    subreddit = subreddit,
                    threadId = threadId,
                    submission = submissionRepository.getSubmission(subreddit, threadId, sort).data!!
                )
            )
        }
    }

    private fun setItemHiddenRecursive(root: HideableSubmissionItem, hidden: Boolean) {
        root.hidden = hidden
        if (root is Comment) {
            root.replies.children.forEach {
                setItemHiddenRecursive(it, hidden)
            }
        }
    }

    fun collapseComment(comment: Comment) {
        comment.collapsed = true
        comment.replies.children.forEach {
            setItemHiddenRecursive(it, true)
        }
        _viewState.value = currentViewState.copy(submission = currentViewState.submission)
    }

    fun expandComment(comment: Comment) {
        comment.collapsed = false
        comment.replies.children.forEach {
            setItemHiddenRecursive(it, false)
        }
        _viewState.value = currentViewState.copy(submission = currentViewState.submission)
    }

    fun setSort(sort: SubmissionSort) {
        if (sort != currentViewState.sort) {
            loadSubmission(currentViewState.subreddit, currentViewState.threadId, sort)
        }
    }
}