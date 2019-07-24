package com.kainalu.readerforreddit.feed.viewholders

import android.view.View
import android.widget.ImageView
import com.airbnb.epoxy.EpoxyModelClass
import com.bumptech.glide.request.RequestOptions
import com.kainalu.readerforreddit.GlideApp
import com.kainalu.readerforreddit.R
import com.bumptech.glide.request.target.Target as GlideTarget


@EpoxyModelClass(layout = R.layout.feed_link_image_item)
abstract class ImagePostModel : BaseLinkModel<ImagePostHolder>() {

    override fun bind(holder: ImagePostHolder) {
        super.bind(holder)
        holder.imageView.visibility = if (link.preview == null) View.GONE else View.VISIBLE
        link.preview?.let {
            GlideApp.with(holder.imageView)
                .load(link.url)
                .apply(RequestOptions().override(GlideTarget.SIZE_ORIGINAL))
                .into(holder.imageView)
        }
    }
}

class ImagePostHolder : BaseHolder() {
    val imageView by bind<ImageView>(R.id.linkImageView)
}
