package com.kainalu.readerforreddit.user

import com.kainalu.readerforreddit.di.Injector
import com.kainalu.readerforreddit.network.models.Subreddit
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import javax.inject.Inject

@Parcelize
data class LoggedOutUser(
    override val id: String? = null,
    override val name: String? = null,
    override val username: String? = null,
    override val commentKarma: Int? = null,
    override val linkKarma: Int? = null,
    override val totalKarma: Int? = null
) : UserData {


    @IgnoredOnParcel
    @Inject
    lateinit var userRepository: UserRepository

    init {
        Injector.get().inject(this)
    }

    override suspend fun getSubscriptions(): List<Subreddit> {
        return userRepository.getDefaultSubscriptions()
    }
}