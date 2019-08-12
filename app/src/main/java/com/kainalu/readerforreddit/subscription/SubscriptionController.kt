package com.kainalu.readerforreddit.subscription

import com.airbnb.epoxy.TypedEpoxyController
import com.kainalu.readerforreddit.network.models.Subreddit

class SubscriptionController : TypedEpoxyController<List<Subreddit>>() {

    override fun buildModels(data: List<Subreddit>?) {
        data?.forEach {
            subreddit {
                subreddit(it)
                id(it.id)
            }
        }
    }
}