package com.kainalu.readerforreddit.submission

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kainalu.readerforreddit.network.models.HideableSubmissionItem
import com.kainalu.readerforreddit.tree.AbstractSubmissionNode
import com.kainalu.readerforreddit.tree.CommentNode
import com.kainalu.readerforreddit.tree.LinkNode
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
/*
    fun getChildren(more: More) {
        viewModelScope.launch {
            val result = submissionRepository.getChildren(
                currentViewState.link!!,
                currentViewState.sort!!,
                more,
                currentViewState.comments.toMutableList()
            )
            _viewState.postValue(currentViewState.copy(comments = result.data!!))
        }
    }*/

    private fun loadSubmission(subreddit: String, threadId: String, sort: SubmissionSort) {
        _viewState.value = currentViewState.copy(sort = sort)
        viewModelScope.launch {
            val tree = submissionRepository.getSubmission(subreddit, threadId, sort).data!!
            val (link, comments) = tree.flatten().partition { it is LinkNode }
            _viewState.postValue(
                currentViewState.copy(
                    subreddit = subreddit,
                    threadId = threadId,
                    submissionTree = tree,
                    link = link.first() as LinkNode,
                    comments = comments
                )
            )
        }
    }

    private fun setItemHiddenRecursive(node: AbstractSubmissionNode<*>, hidden: Boolean) {
        val item = node.data
        if (item is HideableSubmissionItem) {
            item.hidden = hidden
        }
        node.children.forEach {
            setItemHiddenRecursive(it, hidden)
        }
    }

    fun collapseComment(commentNode: CommentNode) {
        commentNode.data.collapsed = true
        commentNode.children.forEach {
            setItemHiddenRecursive(it, true)
        }
        _viewState.value = currentViewState.copy(comments = currentViewState.comments)
    }

    fun expandComment(commentNode: CommentNode) {
        commentNode.data.collapsed = false
        commentNode.children.forEach {
            setItemHiddenRecursive(it, false)
        }
        _viewState.value = currentViewState.copy(comments = currentViewState.comments)
    }

    fun setSort(sort: SubmissionSort) {
        if (sort != currentViewState.sort) {
            loadSubmission(currentViewState.subreddit, currentViewState.threadId, sort)
        }
    }
}