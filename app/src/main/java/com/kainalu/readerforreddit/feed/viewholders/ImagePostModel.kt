package com.kainalu.readerforreddit.feed.viewholders

import android.view.View
import android.widget.ImageView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.bumptech.glide.request.RequestOptions
import com.kainalu.readerforreddit.GlideApp
import com.kainalu.readerforreddit.R
import com.kainalu.readerforreddit.network.models.PreviewInfo
import com.bumptech.glide.request.target.Target as GlideTarget


@EpoxyModelClass(layout = R.layout.feed_link_image_item)
abstract class ImagePostModel : BaseLinkModel<ImagePostHolder>() {

    @EpoxyAttribute
    var preview: PreviewInfo? = null
    @EpoxyAttribute
    lateinit var url: String

    override fun bind(holder: ImagePostHolder) {
        super.bind(holder)
        holder.imageView.visibility = if (preview == null) View.GONE else View.VISIBLE
        holder.imageView.clipToOutline = true
        preview?.let {
            GlideApp.with(holder.imageView)
                .load(url)
                .apply(RequestOptions().override(GlideTarget.SIZE_ORIGINAL))
                .into(holder.imageView)
        }
    }
}

class ImagePostHolder : BaseHolder() {
    val imageView by bind<ImageView>(R.id.linkImageView)
}
