package com.kainalu.readerforreddit.network

import com.kainalu.readerforreddit.network.models.Link
import com.kainalu.readerforreddit.network.models.Listing
import com.kainalu.readerforreddit.network.models.RedditModel
import com.kainalu.readerforreddit.network.models.SubmissionItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @RedditModel
    @GET("/{sort}")
    suspend fun getSubreddit(
        @Path("sort") sort: String,
        @Query("t") sortDuration: String,
        @Query("after") after: String = ""
    ): Response<Listing<Link>>

    @RedditModel
    @GET("/r/{name}/{sort}")
    suspend fun getSubreddit(
        @Path("name") subreddit: String,
        @Path("sort") sort: String,
        @Query("t") sortDuration: String,
        @Query("after") after: String = ""
    ): Response<Listing<Link>>

    @RedditModel
    @GET("/r/{name}/comments/{id}")
    suspend fun getSubmission(
        @Path("name") subreddit: String,
        @Path("id") threadId: String,
        @Query("sort") sort: String
    ): Response<List<Listing<SubmissionItem>>>
}