package com.kainalu.readerforreddit.submission

import android.text.TextUtils
import com.kainalu.readerforreddit.network.ApiService
import com.kainalu.readerforreddit.network.Resource
import com.kainalu.readerforreddit.network.models.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubmissionRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getSubmission(
        subreddit: String,
        threadId: String,
        sort: SubmissionSort
    ): Resource<List<SubmissionItem>> {
        val response = apiService.getSubmission(subreddit, threadId, sort.urlString)
        return if (response.isSuccessful) {
            // There should be two listings, the first contains the submission link, the second has comments
            val data = mutableListOf<SubmissionItem>()
            response.body()?.forEach {
                flattenComments(it.children, data)
            }
            Resource.Success(data)
        } else {
            Resource.Error(emptyList(), null)
        }
    }

    suspend fun getChildren(
        link: Link,
        sort: SubmissionSort,
        more: More,
        commentList: MutableList<SubmissionItem>
    ): Resource<List<SubmissionItem>> {
        val children = TextUtils.join(",", more.children)
        val response = apiService.getChildren(children = children, linkId = link.name, sort = sort.urlString)
        return if (response.isSuccessful) {
            val parent = if (more.depth == 0) {
                null
            } else {
                commentList.find { it is Comment && it.name == more.parentId }
            }

            insertComments(parent as Comment?, more, response.body() ?: emptyList(), commentList)
            Resource.Success(commentList)
        } else {
            Resource.Error(commentList, null)
        }
    }

    private fun flattenComments(data: List<SubmissionItem>, output: MutableList<SubmissionItem>): List<SubmissionItem> {
        data.forEach { element ->
            flatten(element, output, false)
        }

        return output
    }

    /**
     * Recursively flattens the comment tree into a list and marks the children of
     * collapsed comments as `hidden`.
     *
     * @param root The root item to begin flattening from
     * @param output The list that the flattened tree is added to
     * @param shouldHide Controls whether the children of a comment should be hidden.
     */
    private fun flatten(root: SubmissionItem, output: MutableList<SubmissionItem>, shouldHide: Boolean) {
        if (root is HideableSubmissionItem) {
            root.hidden = shouldHide
        }
        output += root
        if (root is Comment) {
            root.replies.children.forEach {
                flatten(it, output, root.collapsed || root.hidden)
            }
        }
    }

    private fun insertComments(
        parent: Comment?,
        more: More,
        children: List<HideableSubmissionItem>,
        output: MutableList<SubmissionItem>
    ) {
        // Add children to parent comment
        parent?.let {
            val replies = parent.replies.children.toMutableList()
            val i = replies.indexOf(more)
            replies.removeAt(i)
            replies.addAll(i, children)

            // Replace old parent with new parent that has updated children
            val parentIndex = output.indexOf(parent)
            val newReplies = parent.replies.copy(children = replies)
            output.removeAt(parentIndex)
            output.add(parentIndex, parent.copy(replies = newReplies))
        }

        var index = output.indexOf(more)
        output.removeAt(index)
        children.forEach {
            index = insertItem(it, index, output)
        }
    }

    private fun insertItem(item: HideableSubmissionItem, index: Int, output: MutableList<SubmissionItem>): Int {
        var i = index
        output.add(i, item)
        if (item is Comment) {
            item.replies.children.forEach {
                i = insertItem(it, i + 1, output)
            }
        }
        return i + 1
    }

    companion object {
        private const val CHILDREN_TO_LOAD = 100
    }
}