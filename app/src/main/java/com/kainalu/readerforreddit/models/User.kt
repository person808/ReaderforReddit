package com.kainalu.readerforreddit.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.kainalu.readerforreddit.di.Injector
import com.kainalu.readerforreddit.network.models.Account
import com.kainalu.readerforreddit.network.models.Subreddit
import com.kainalu.readerforreddit.user.UserRepository
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import javax.inject.Inject

@Entity
@Parcelize
data class User(
    @PrimaryKey
    override val id: String,
    @ColumnInfo
    override val name: String,
    @ColumnInfo
    override val username: String,
    @ColumnInfo
    override val commentKarma: Int,
    @ColumnInfo
    override val linkKarma: Int
) : UserData {

    @Ignore
    @IgnoredOnParcel
    @Inject
    lateinit var userRepository: UserRepository

    init {
        Injector.get().inject(this)
    }

    override val totalKarma: Int?
        get() = commentKarma + linkKarma

    override suspend fun getSubscriptions(): List<Subreddit> {
        return userRepository.getUserSubscriptions()
    }

    companion object {

        fun fromAccount(account: Account): User {
            return User(
                id = account.id,
                name = account.name,
                username = account.username,
                commentKarma = account.commentKarma,
                linkKarma = account.linkKarma
            )
        }
    }
}