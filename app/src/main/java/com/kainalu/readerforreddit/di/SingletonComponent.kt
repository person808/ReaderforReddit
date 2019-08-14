package com.kainalu.readerforreddit.di

import android.content.Context
import com.kainalu.readerforreddit.MainActivity
import com.kainalu.readerforreddit.auth.AuthFragment
import com.kainalu.readerforreddit.feed.FeedFragment
import com.kainalu.readerforreddit.models.LoggedOutUser
import com.kainalu.readerforreddit.models.User
import com.kainalu.readerforreddit.submission.SubmissionFragment
import com.kainalu.readerforreddit.subreddit.SubredditFragment
import com.kainalu.readerforreddit.subscription.SubscriptionsFragment
import com.kainalu.readerforreddit.ui.AccountSwitcherDialog
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiModule::class, AuthModule::class, PersistenceModule::class, ViewModelModule::class])
interface SingletonComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): SingletonComponent
    }

    fun inject(activity: MainActivity)
    fun inject(fragment: FeedFragment)
    fun inject(fragment: SubmissionFragment)
    fun inject(fragment: SubscriptionsFragment)
    fun inject(fragment: AccountSwitcherDialog)
    fun inject(fragment: AuthFragment)
    fun inject(fragment: SubredditFragment)
    fun inject(user: User)
    fun inject(user: LoggedOutUser)
}