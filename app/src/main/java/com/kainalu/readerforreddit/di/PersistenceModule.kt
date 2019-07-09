package com.kainalu.readerforreddit.di

import android.content.Context
import android.content.SharedPreferences
import com.kainalu.readerforreddit.network.SessionManager
import com.kainalu.readerforreddit.network.SessionManagerImpl
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
        fun sessionManager(sessionManagerImpl: SessionManagerImpl): SessionManager
    }

    @Provides
    @Singleton
    @JvmStatic
    fun sharedPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences("pref", Context.MODE_PRIVATE)
}