package org.ligi.ipfsdroid.activities.feed

import android.content.Intent
import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.feed_list_item.view.*
import kotlinx.android.synthetic.main.feed_list_item_in_playlist.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.ligi.ipfsdroid.R
import org.ligi.ipfsdroid.activities.player.ExoplayerActivity
import org.ligi.ipfsdroid.glide.GlideApp
import org.ligi.ipfsdroid.inflate
import org.ligi.ipfsdroid.model.Feed
import org.ligi.ipfsdroid.repository.PlaylistItem
import org.ligi.ipfsdroid.repository.Repository
import java.io.BufferedInputStream

/**
 * Created by WillowTree on 8/31/18.
 */
class FeedsRecyclerAdapter(private val items: List<Feed>, val repository: Repository, val playlist: List<PlaylistItem>, private val downloadCompleteListener: () -> Unit) : RecyclerView.Adapter<FeedsViewHolderBase>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedsViewHolderBase {
        return when (viewType) {
            STANDARD_VIEW -> {
                val inflatedView = parent.inflate(R.layout.feed_list_item, false)
                FeedsViewHolder(inflatedView)
            }
            IN_PLAYLIST_VIEW -> {
                val inflatedView = parent.inflate(R.layout.feed_list_item_in_playlist, false)
                FeedsViewHolderInPlaylist(inflatedView)
            }
            else -> {
                val inflatedView = parent.inflate(R.layout.feed_list_item, false)
                FeedsViewHolder(inflatedView)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: FeedsViewHolderBase, position: Int) {

        holder.nameText.text = items[position].title
        holder.descriptionText.text = items[position].description

        // TODO this feels hacky
        doAsync {
            repository.getImageBitmapFromHash(items[position].thumbNail) { it ->
                val bufferedInputStream = BufferedInputStream(it)
                val bmp = BitmapFactory.decodeStream(bufferedInputStream)

                uiThread {
                    GlideApp.with(holder.itemView.context)
                            .load(bmp)
                            .centerCrop()
                            .into(holder.thumbNailImageView)
                }
            }
        }

        when (holder.itemViewType) {
            STANDARD_VIEW -> {
                val feedsViewHolder = holder as FeedsViewHolder
                feedsViewHolder.downloadButton.setOnClickListener {
                    // download the item and add it to the playlist
                    holder.progressBar.visibility = View.VISIBLE
                    repository.insertPlaylistItem(items[position], downloadCompleteListener)
                }
            }

            IN_PLAYLIST_VIEW -> {
                val feedsViewHolder = holder as FeedsViewHolderInPlaylist
                feedsViewHolder.playAssetButton.setOnClickListener {
                    doAsync {
                        repository.movePlayListItem(items[position].link, 0)
                        it.context.startActivity(Intent(it.context, ExoplayerActivity::class.java))
                    }
                }
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        if (items[position].isInPlayList(playlist)) {
            return IN_PLAYLIST_VIEW
        }
        return STANDARD_VIEW
    }

    companion object {
        private const val STANDARD_VIEW = 0
        private const val IN_PLAYLIST_VIEW = 1
    }
}

open class FeedsViewHolderBase(view: View) : RecyclerView.ViewHolder(view) {
    val nameText = view.findViewById<TextView>(R.id.textViewName)
    val descriptionText = view.findViewById<TextView>(R.id.textViewDescription)
    val thumbNailImageView = view.findViewById<ImageView>(R.id.thumbnail)
}

class FeedsViewHolder(view: View) : FeedsViewHolderBase(view) {
    val downloadButton = view.downloadAssetButton
    val progressBar = view.progressBar
}

class FeedsViewHolderInPlaylist(view: View) : FeedsViewHolderBase(view) {
    val playAssetButton = view.playAssetButton
}