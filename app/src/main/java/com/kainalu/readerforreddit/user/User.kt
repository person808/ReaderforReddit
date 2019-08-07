package com.kainalu.readerforreddit.user

import com.kainalu.readerforreddit.di.Injector
import com.kainalu.readerforreddit.network.models.Account
import com.kainalu.readerforreddit.network.models.Subreddit
import javax.inject.Inject

class User(account: Account) : UserData {

    @Inject lateinit var userRepository: UserRepository

    init {
        Injector.get().inject(this)
    }

    override val id: String = account.id
    override val name = account.name
    override val username = account.username
    override val commentKarma = account.commentKarma
    override val linkKarma = account.linkKarma
    override val totalKarma: Int?
        get() = commentKarma + linkKarma

    override suspend fun getSubscriptions(): List<Subreddit> {
        return userRepository.getDefaultSubscriptions()
    }
}