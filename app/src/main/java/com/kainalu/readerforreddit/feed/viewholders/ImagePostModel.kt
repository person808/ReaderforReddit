package com.kainalu.readerforreddit.feed.viewholders

import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import com.airbnb.epoxy.EpoxyModelClass
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.kainalu.readerforreddit.GlideApp
import com.kainalu.readerforreddit.R
import com.kainalu.readerforreddit.util.setDrawableTint
import com.bumptech.glide.request.target.Target as GlideTarget


@EpoxyModelClass(layout = R.layout.feed_link_image_item)
abstract class ImagePostModel : BaseLinkModel<ImagePostHolder>() {

    private fun getRequestListener(holder: ImagePostHolder): RequestListener<Drawable> =
        object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: GlideTarget<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: GlideTarget<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                resource?.toBitmap()?.let { bitmap ->
                    Palette.Builder(bitmap).maximumColorCount(24).generate { palette ->
                        palette?.mutedSwatch?.let { swatch ->
                            holder.infoLayout.setBackgroundColor(swatch.rgb)
                            holder.titleTextView.setTextColor(swatch.titleTextColor)
                            holder.subredditTextView.setTextColor(swatch.titleTextColor)
                            holder.authorTextView.setTextColor(swatch.bodyTextColor)
                            holder.scoreTextView.setTextColor(swatch.bodyTextColor)
                            holder.commentTextView.setTextColor(swatch.bodyTextColor)
                            holder.commentTextView.setDrawableTint(swatch.bodyTextColor)
                            holder.upvoteButton.setColorFilter(swatch.bodyTextColor, PorterDuff.Mode.SRC_ATOP)
                            holder.downvoteButton.setColorFilter(swatch.bodyTextColor, PorterDuff.Mode.SRC_ATOP)
                        }
                    }
                }
                return false
            }
        }

    override fun bind(holder: ImagePostHolder) {
        super.bind(holder)
        holder.imageView.visibility = if (link.preview == null) View.GONE else View.VISIBLE
        link.preview?.let {
            GlideApp.with(holder.imageView)
                .load(link.url)
                .apply(RequestOptions().override(GlideTarget.SIZE_ORIGINAL))
                .listener(getRequestListener(holder))
                .into(holder.imageView)
        }
    }
}

class ImagePostHolder : BaseHolder() {
    val imageView by bind<ImageView>(R.id.linkImageView)
    val infoLayout by bind<ViewGroup>(R.id.infoLayout)
}
