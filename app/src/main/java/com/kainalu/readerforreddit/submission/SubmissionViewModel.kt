package com.kainalu.readerforreddit.submission

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kainalu.readerforreddit.tree.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class SubmissionViewModel @Inject constructor(
    private val submissionRepository: SubmissionRepository
) : ViewModel() {

    private val _viewState = MutableLiveData(SubmissionViewState())
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
            val result = submissionRepository.loadChildren(
                currentViewState.link!!,
                currentViewState.sort!!,
                more,
                currentViewState.submissionTree!!
            )
            _viewState.postValue(currentViewState.copy(
                comments = currentViewState.submissionTree?.getDataPair()?.second
            ))
        }
    }

    private fun loadSubmission(subreddit: String, threadId: String, sort: SubmissionSort) {
        _viewState.value = currentViewState.copy(sort = sort)
        viewModelScope.launch {
            val tree = submissionRepository.getSubmission(subreddit, threadId, sort).data!!
            val (link, comments) = tree.getDataPair()
            _viewState.postValue(
                currentViewState.copy(
                    subreddit = subreddit,
                    threadId = threadId,
                    submissionTree = tree,
                    link = link,
                    comments = comments
                )
            )
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