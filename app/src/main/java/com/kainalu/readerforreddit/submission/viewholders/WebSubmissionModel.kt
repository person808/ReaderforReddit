package com.kainalu.readerforreddit.submission.viewholders

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.card.MaterialCardView
import com.kainalu.readerforreddit.R

@EpoxyModelClass(layout = R.layout.submission_web_item)
abstract class WebSubmissionModel : BaseSubmissionModel<WebSubmissionHolder>() {

     private class PreviewRequestListener(private val holder: WebSubmissionHolder) : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            resource?.toBitmap()?.let { bitmap ->
                Palette.from(bitmap).generate { palette ->
                    palette?.lightMutedSwatch?.let { swatch ->
                        holder.previewContainer.setCardBackgroundColor(swatch.rgb)
                        holder.domainTextView.setTextColor(swatch.bodyTextColor)
                    }
                }
            }
            return false
        }
    }

    @EpoxyAttribute lateinit var onLinkClick: View.OnClickListener

    override fun bind(holder: WebSubmissionHolder) {
        super.bind(holder)
        link.preview?.let {
            Glide.with(holder.previewImageView)
                .load(it.url)
                .listener(PreviewRequestListener(holder))
                .into(holder.previewImageView)
        }
        holder.previewContainer.setOnClickListener(onLinkClick)
        holder.domainTextView.text = link.domain
    }
}

class WebSubmissionHolder : BaseHolder() {
    val previewContainer by bind<MaterialCardView>(R.id.linkPreview)
    val previewImageView by bind<ImageView>(R.id.previewImageView)
    val domainTextView by bind<TextView>(R.id.domainTextView)
}
