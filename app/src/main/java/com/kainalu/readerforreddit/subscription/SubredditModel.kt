package com.kainalu.readerforreddit.subscription

import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.kainalu.readerforreddit.GlideApp
import com.kainalu.readerforreddit.R
import com.kainalu.readerforreddit.network.models.Subreddit
import com.kainalu.readerforreddit.util.KotlinEpoxyHolder

@EpoxyModelClass(layout = R.layout.subreddit_list_item)
abstract class SubredditModel : EpoxyModelWithHolder<SubredditHolder>() {

    @EpoxyAttribute lateinit var subreddit: Subreddit

    override fun bind(holder: SubredditHolder) {
        super.bind(holder)
        holder.nameTextView.text = subreddit.displayName
        val colorGenerator = ColorGenerator.MATERIAL
        val color = colorGenerator.getColor(subreddit.id)
        val iconPlaceholder = TextDrawable.builder()
            .buildRect(subreddit.displayName.toUpperCase().first().toString(), color)
        holder.iconImageView.clipToOutline = true
        GlideApp.with(holder.iconImageView)
            .load(subreddit.iconUrl)
            .error(iconPlaceholder)
            .into(holder.iconImageView)
    }
}

class SubredditHolder : KotlinEpoxyHolder() {
    val nameTextView by bind<TextView>(R.id.nameTextView)
    val iconImageView by bind<ImageView>(R.id.iconImageView)
}