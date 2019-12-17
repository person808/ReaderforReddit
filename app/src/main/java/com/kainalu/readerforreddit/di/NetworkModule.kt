package com.kainalu.readerforreddit.di

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
object NetworkModule {

    @Provides
    @JvmStatic
    @Singleton
    fun okHttp(): OkHttpClient = OkHttpClient.Builder().build()
}