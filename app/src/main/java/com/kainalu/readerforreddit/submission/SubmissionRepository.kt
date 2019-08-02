package com.kainalu.readerforreddit.submission

import android.util.Log
import com.kainalu.readerforreddit.network.ApiService
import com.kainalu.readerforreddit.network.Resource
import com.kainalu.readerforreddit.network.models.Link
import com.kainalu.readerforreddit.tree.SubmissionTree
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubmissionRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getSubmission(
        subreddit: String,
        threadId: String,
        sort: SubmissionSort
    ): Resource<SubmissionTree> {
        val response = apiService.getSubmission(subreddit, threadId, sort.urlString)
        return if (response.isSuccessful) {
            val body = response.body() ?: return Resource.Error(null, null)
            if (body.size != 2) {
                Log.e(TAG, "Response should contain 2 items. Found ${body.size} items.")
                return Resource.Error(null, null)
            }
            val link = body.first().children.first() as Link
            val comments = body.last().children
            val tree = SubmissionTree.Builder()
                .setComments(comments)
                .setRoot(link)
                .build()
            Resource.Success(tree)
        } else {
            Resource.Error(null, null)
        }
    }

    companion object {
        private const val TAG = "SubmissionRepository"
    }
}