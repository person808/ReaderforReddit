package com.kainalu.readerforreddit.di

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen

class DaggerApplication : Application() {

    lateinit var component: SingletonComponent

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        component = DaggerSingletonComponent.factory().create(this)
        AndroidThreeTen.init(this)
    }

    companion object {
        private var INSTANCE: DaggerApplication? = null
        @JvmStatic
        fun get(): DaggerApplication = INSTANCE!!
    }
}

object Injector {
    @JvmStatic
    fun get(): SingletonComponent = DaggerApplication.get().component
}