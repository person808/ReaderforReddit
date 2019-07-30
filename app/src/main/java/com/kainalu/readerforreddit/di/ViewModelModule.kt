package com.kainalu.readerforreddit.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kainalu.readerforreddit.feed.FeedViewModel
import com.kainalu.readerforreddit.submission.SubmissionViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {
    @Binds fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(FeedViewModel::class)
    fun feedViewModel(viewModel: FeedViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SubmissionViewModel::class)
    fun submissionViewModel(viewModel: SubmissionViewModel): ViewModel
}