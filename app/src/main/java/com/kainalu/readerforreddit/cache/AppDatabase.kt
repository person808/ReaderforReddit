package com.kainalu.readerforreddit.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kainalu.readerforreddit.user.User

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
