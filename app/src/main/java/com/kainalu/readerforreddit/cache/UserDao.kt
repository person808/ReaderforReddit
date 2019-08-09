package com.kainalu.readerforreddit.cache

import androidx.room.*
import com.kainalu.readerforreddit.models.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    suspend fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): User

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg users: User)

    @Delete
    suspend fun delete(user: User)
}