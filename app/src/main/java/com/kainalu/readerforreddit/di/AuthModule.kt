package com.kainalu.readerforreddit.di

import com.kainalu.readerforreddit.BuildConfig
import com.kainalu.readerforreddit.network.AuthService
import dagger.Module
import dagger.Provides
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
object AuthModule {

    @Provides
    @Singleton
    @Named("auth")
    @JvmStatic
    fun okhttp(): OkHttpClient {
        val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
        val headersInterceptor = Interceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .addHeader("User-Agent", "unix:MyRedditTestApp:v1.0.0")
                .method(original.method, original.body)
                .build()
            chain.proceed(request)
        }
        val authenticator = object : Authenticator {
            override fun authenticate(route: Route?, response: Response): Request? {
                if (response.request.header("Authorization") != null) {
                    return null // Give up, we've already attempted to authenticate.
                }

                val credential = Credentials.basic(BuildConfig.CLIENT_ID, "")
                return response.request.newBuilder()
                    .header("Authorization", credential)
                    .build()
            }
        }

        return OkHttpClient.Builder()
            .authenticator(authenticator)
            .addInterceptor(headersInterceptor)
            .addInterceptor(logger)
            .build()
    }

    @Provides
    @Singleton
    @Named("auth")
    @JvmStatic
    fun authRetrofit(@Named("auth") client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://www.reddit.com")
        .addConverterFactory(MoshiConverterFactory.create())
        .client(client)
        .build()

    @Provides
    @Singleton
    @JvmStatic
    fun authService(@Named("auth") retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)
}