package com.kainalu.readerforreddit.auth

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kainalu.readerforreddit.models.TokenData

@Entity
data class SavedToken(
    @PrimaryKey
    val userId: String,
    @ColumnInfo
    override val accessToken: String,
    @ColumnInfo
    override val tokenType: String,
    @ColumnInfo
    override val secondsUntilExpiration: Long,
    @ColumnInfo
    override val scope: String,
    @ColumnInfo
    override val refreshToken: String? = null
) : TokenData {
    companion object {
        fun fromToken(userId: String, token: TokenData): SavedToken {
            return SavedToken(
                userId = userId,
                accessToken = token.accessToken,
                tokenType = token.tokenType,
                secondsUntilExpiration = token.secondsUntilExpiration,
                scope = token.scope,
                refreshToken = token.refreshToken
            )
        }
    }
}
