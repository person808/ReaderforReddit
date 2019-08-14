package com.kainalu.readerforreddit.subscription

import com.airbnb.epoxy.TypedEpoxyController
import com.kainalu.readerforreddit.network.models.Subreddit

class SubscriptionController(
    private val subredditClickListener: SubredditClickListener
) : TypedEpoxyController<List<Subreddit>>() {

    interface SubredditClickListener {
        fun onSubredditClicked(subreddit: Subreddit)
    }

    override fun buildModels(data: List<Subreddit>?) {
        data?.forEach {
            subreddit {
                subreddit(it)
                onClick { v -> subredditClickListener.onSubredditClicked(it) }
                id(it.id)
            }
        }
    }
}