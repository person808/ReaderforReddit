package com.kainalu.readerforreddit.di

import com.kainalu.readerforreddit.network.ApiService
import com.kainalu.readerforreddit.network.AuthService
import com.kainalu.readerforreddit.network.SessionManager
import com.kainalu.readerforreddit.network.adapters.*
import com.kainalu.readerforreddit.network.models.Comment
import com.kainalu.readerforreddit.network.models.Link
import com.kainalu.readerforreddit.network.models.More
import com.kainalu.readerforreddit.network.models.Token
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
object ApiModule {

    @Provides
    @Singleton
    @Named("api")
    @JvmStatic
    fun okhttp(authService: AuthService, sessionManager: SessionManager): OkHttpClient {
        val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

        fun refreshToken(): Token = runBlocking {
            val token = authService.getLoggedOutToken(deviceId = sessionManager.getDeviceId())
            sessionManager.saveToken(token)
            return@runBlocking token
        }

        // Adds the required headers and the query to receive unescaped json
        val interceptor = Interceptor { chain ->
            val original = chain.request()
            val url = original.url.newBuilder()
                .addQueryParameter("raw_json", "1")
                .build()
            val credential = sessionManager.getToken()?.accessToken ?: refreshToken().accessToken

            val request = original.newBuilder()
                .addHeader("User-Agent", "unix:MyRedditTestApp:v1.0.0")
                .addHeader("Authorization", "bearer $credential")
                .url(url)
                .method(original.method, original.body)
                .build()
            val response = chain.proceed(request)

            if (response.code == 401) { // Unauthorized
                response.close()
                val newRequest = original.newBuilder()
                    .addHeader("Authorization", "bearer ${refreshToken().accessToken}")
                    .build()
                return@Interceptor chain.proceed(newRequest)
            }

            response
        }

        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(logger)
            .build()
    }

    @Provides
    @Singleton
    @JvmStatic
    fun moshi(): Moshi {
        return Moshi.Builder()
            .add(RedditModelListJsonAdapter.FACTORY)
            .add(
                RedditJsonAdapterFactory.of("kind", "data")
                    .withType(Comment::class.java, "t1")
                    .withType(Link::class.java, "t3")
                    .withType(More::class.java, "more")
            )
            .add(EditInfoJsonAdapter())
            .add(LocalDateTimeJsonAdapter())
            .add(PreviewInfoJsonAdapter())
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    @Named("api")
    @JvmStatic
    fun apiRetrofit(@Named("api") client: OkHttpClient, moshi: Moshi): Retrofit = Retrofit.Builder()
        .baseUrl("https://oauth.reddit.com")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(client)
        .build()

    @Provides
    @Singleton
    @JvmStatic
    fun apiService(@Named("api") retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)
}