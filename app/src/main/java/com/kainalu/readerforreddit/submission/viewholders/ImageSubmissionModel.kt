package com.kainalu.readerforreddit.submission.viewholders

import android.view.View
import android.widget.ImageView
import androidx.core.view.updateLayoutParams
import com.airbnb.epoxy.EpoxyModelClass
import com.bumptech.glide.request.RequestOptions
import com.kainalu.readerforreddit.GlideApp
import com.kainalu.readerforreddit.R
import com.bumptech.glide.request.target.Target as GlideTarget


@EpoxyModelClass(layout = R.layout.submission_image_item)
abstract class ImageSubmissionModel : BaseSubmissionModel<ImageSubmissionHolder>() {

    override fun bind(holder: ImageSubmissionHolder) {
        super.bind(holder)
        holder.imageView.visibility = if (link.preview == null) View.GONE else View.VISIBLE
        link.preview?.let { previewInfo ->
            // Make sure we measure the view's dimensions after layout by using view.post()
            // Otherwise we might receive a width of 0
            holder.imageView.post {
                // Resize the imageview so that it can fit the whole image while
                // maintaining aspect ration
                val imageWidthRatio = holder.imageView.width.toDouble() / previewInfo.width
                val imageViewHeightPx = previewInfo.height * imageWidthRatio
                holder.imageView.updateLayoutParams { height = imageViewHeightPx.toInt() }
            }
            GlideApp.with(holder.imageView)
                .load(link.url)
                .apply(RequestOptions().override(GlideTarget.SIZE_ORIGINAL))
                .into(holder.imageView)
        }
    }
}

class ImageSubmissionHolder : BaseHolder() {
    val imageView by bind<ImageView>(R.id.linkImageView)
}
