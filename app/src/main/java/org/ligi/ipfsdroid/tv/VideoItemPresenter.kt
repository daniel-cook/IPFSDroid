package org.ligi.ipfsdroid.tv

import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.support.v17.leanback.widget.ImageCardView
import android.support.v17.leanback.widget.Presenter
import android.support.v4.content.ContextCompat
import android.view.ViewGroup
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.ligi.ipfsdroid.R
import org.ligi.ipfsdroid.glide.GlideApp
import org.ligi.ipfsdroid.model.Feed
import org.ligi.ipfsdroid.repository.Repository
import java.io.BufferedInputStream


/**
 * Created by WillowTree on 9/20/18.
 */
class VideoItemPresenter(val repository: Repository) : Presenter() {

    private var mSelectedBackgroundColor = -1
    private var mDefaultBackgroundColor = -1
    private var mDefaultCardImage: Drawable? = null

    override fun onCreateViewHolder(parent: ViewGroup?): ViewHolder {
        parent?.let {
            mDefaultBackgroundColor = ContextCompat.getColor(it.context, R.color.default_background)
            mSelectedBackgroundColor = ContextCompat.getColor(it.context, R.color.selected_background)
            mDefaultCardImage = it.resources.getDrawable(R.drawable.movie, null)
        }

        val cardView = object : ImageCardView(parent?.context) {
            override fun setSelected(selected: Boolean) {
                updateCardBackgroundColor(this, selected)
                super.setSelected(selected)
            }
        }

        cardView.isFocusable = true
        cardView.isFocusableInTouchMode = true
        updateCardBackgroundColor(cardView, false)
        return Presenter.ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder?, item: Any?) {
        val feedItem = item as Feed

        val cardView = viewHolder?.view as ImageCardView
        cardView.titleText = feedItem.title
        cardView.contentText = feedItem.description
        val res = cardView.resources
        val width = res.getDimensionPixelSize(R.dimen.card_width)
        val height = res.getDimensionPixelSize(R.dimen.card_height)
        cardView.setMainImageDimensions(width, height)

        doAsync {
            repository.getImageBitmapFromHash(feedItem.thumbNail) { it ->
                val bufferedInputStream = BufferedInputStream(it)
                val bmp = BitmapFactory.decodeStream(bufferedInputStream)

                uiThread {
                    GlideApp.with(cardView.context)
                            .load(bmp)
                            .centerCrop()
                            .into(cardView.mainImageView)
                }
            }
        }
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder?) {
        // no op
    }


    private fun updateCardBackgroundColor(view: ImageCardView, selected: Boolean) {
        val color = if (selected) mSelectedBackgroundColor else mDefaultBackgroundColor

        // Both background colors should be set because the view's
        // background is temporarily visible during animations.
        view.setBackgroundColor(color)
        view.setInfoAreaBackgroundColor(color)
    }
}