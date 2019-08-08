package com.kainalu.readerforreddit.network

import com.kainalu.readerforreddit.network.annotations.LegacyRedditResponse
import com.kainalu.readerforreddit.network.annotations.RedditModel
import com.kainalu.readerforreddit.network.models.*
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

    @LegacyRedditResponse
    @RedditModel
    @GET("/api/morechildren")
    suspend fun getChildren(
        @Query("api_type") apiType: String = "json",
        @Query("children") children: String,
        @Query("link_id") linkId: String,
        @Query("sort") sort: String
    ): Response<List<SubmissionItem>>

    @GET("/api/v1/me")
    suspend fun me(): Response<Account>

    @RedditModel
    @GET("/user/{username}/about")
    suspend fun getUser(@Path("username") username: String): Response<Account>

    @RedditModel
    @GET("/subreddits/default")
    suspend fun getDefaultSubreddits(
        @Query("limit") limit: Int,
        @Query("after") after: String = "",
        @Query("before") before: String = ""
    ): Response<Listing<Subreddit>>

    @RedditModel
    @GET("/subreddits/mine/subscriber")
    suspend fun getSubscribedSubreddits(
        @Query("limit") limit: Int,
        @Query("after") after: String = "",
        @Query("before") before: String = ""
    ): Response<Listing<Subreddit>>
}