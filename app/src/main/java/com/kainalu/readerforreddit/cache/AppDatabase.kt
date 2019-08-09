package com.kainalu.readerforreddit.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kainalu.readerforreddit.auth.SavedToken
import com.kainalu.readerforreddit.auth.TokenDao
import com.kainalu.readerforreddit.models.User

@Database(entities = [SavedToken::class, User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tokenDao(): TokenDao
    abstract fun userDao(): UserDao
}
