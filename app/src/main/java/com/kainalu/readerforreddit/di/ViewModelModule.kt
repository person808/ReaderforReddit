package com.kainalu.readerforreddit.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kainalu.readerforreddit.AccountSwitcherViewModel
import com.kainalu.readerforreddit.auth.AuthViewModel
import com.kainalu.readerforreddit.feed.FeedViewModel
import com.kainalu.readerforreddit.submission.SubmissionViewModel
import com.kainalu.readerforreddit.subreddit.SubredditViewModel
import com.kainalu.readerforreddit.subscription.SubscriptionsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {
    @Binds fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AccountSwitcherViewModel::class)
    fun activityViewModel(viewModel: AccountSwitcherViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FeedViewModel::class)
    fun feedViewModel(viewModel: FeedViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SubmissionViewModel::class)
    fun submissionViewModel(viewModel: SubmissionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SubscriptionsViewModel::class)
    fun subscriptionsViewModel(viewModel: SubscriptionsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    fun authViewModel(viewModel: AuthViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SubredditViewModel::class)
    fun subredditViewModel(viewModel: SubredditViewModel): ViewModel
}