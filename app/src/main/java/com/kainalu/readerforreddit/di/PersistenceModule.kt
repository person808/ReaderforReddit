package com.kainalu.readerforreddit.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.kainalu.readerforreddit.cache.AppDatabase
import com.kainalu.readerforreddit.network.TokenManager
import com.kainalu.readerforreddit.network.TokenManagerImpl
import com.kainalu.readerforreddit.user.UserManager
import com.kainalu.readerforreddit.user.UserManagerImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [PersistenceModule.Declarations::class])
object PersistenceModule {

    @Module
    interface Declarations {
        @Binds
        @Singleton
        fun sessionManager(sessionManagerImpl: TokenManagerImpl): TokenManager

        @Binds
        @Singleton
        fun userManager(userManagerImpl: UserManagerImpl): UserManager
    }

    @Provides
    @Singleton
    @JvmStatic
    fun sharedPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences("pref", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    @JvmStatic
    fun roomDatabase(context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "cache").build()
}