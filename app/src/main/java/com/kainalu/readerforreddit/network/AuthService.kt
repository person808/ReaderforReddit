package com.kainalu.readerforreddit.network

import com.kainalu.readerforreddit.network.models.Token
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthService {

    @POST("api/v1/access_token")
    @FormUrlEncoded
    suspend fun getToken(
        @Field("grant_type") grantType: String = "authorization_code",
        @Field("code") code: String,
        @Field("redirect_uri") redirectUri: String
    ): Token

    @POST("api/v1/access_token")
    @FormUrlEncoded
    suspend fun refreshToken(
        @Field("grant_type") grantType: String = "refresh_token",
        @Field("refresh_token") refreshToken: String
    ): Token

    @POST("api/v1/access_token")
    @FormUrlEncoded
    suspend fun getLoggedOutToken(
        @Field("grant_type") grantType: String = "https://oauth.reddit.com/grants/installed_client",
        @Field("device_id") deviceId: String
    ): Token
}