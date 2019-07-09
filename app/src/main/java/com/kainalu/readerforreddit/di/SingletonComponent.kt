package com.kainalu.readerforreddit.di

import android.content.Context
import com.kainalu.readerforreddit.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiModule::class, AuthModule::class, PersistenceModule::class])
interface SingletonComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): SingletonComponent
    }

    fun inject(activity: MainActivity)
}