package com.kainalu.readerforreddit.submission

import com.kainalu.readerforreddit.network.ApiService
import com.kainalu.readerforreddit.network.Resource
import com.kainalu.readerforreddit.network.models.Comment
import com.kainalu.readerforreddit.network.models.HideableSubmissionItem
import com.kainalu.readerforreddit.network.models.SubmissionItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubmissionRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getSubmission(
        subreddit: String,
        threadId: String,
        sort: SubmissionSort
    ): Resource<List<SubmissionItem>> {
        val response = apiService.getSubmission(subreddit, threadId, sort.name.toLowerCase())
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
}