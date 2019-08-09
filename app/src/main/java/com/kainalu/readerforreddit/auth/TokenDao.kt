package com.kainalu.readerforreddit.auth

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TokenDao {
    @Query("SELECT * FROM savedToken WHERE userId = :userId LIMIT 1")
    suspend fun getById(userId: String?): SavedToken?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg token: SavedToken)

    @Query("DELETE FROM savedToken WHERE userId = :userId")
    suspend fun deleteByUserId(userId: String)
}