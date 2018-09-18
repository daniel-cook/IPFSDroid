package org.ligi.ipfsdroid.activities.player

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import org.jetbrains.anko.doAsync
import org.ligi.ipfsdroid.repository.PlaylistItem
import org.ligi.ipfsdroid.repository.Repository

/**
 * Created by WillowTree on 9/11/18.
 */
class PlayerViewModel : ViewModel() {  // TODO rename this to match up with ExoPlayer

    lateinit var repository: Repository

    lateinit var dataSourceFactory: DefaultDataSourceFactory

    private var liveMediaSource: MutableLiveData<ConcatenatingMediaSource>? = null

    // TODO this method is obsolete if the old style player is no longer going to be used
    fun getPlaylist() : LiveData<List<PlaylistItem>>? {
        return repository.getPlaylist()
    }

    fun getConcatMediaItems() : LiveData<ConcatenatingMediaSource>? {
        if(liveMediaSource == null) {
            liveMediaSource = MutableLiveData()
            loadMedia()
        }
        return liveMediaSource!!
    }

    private fun loadMedia() {
        doAsync {
            liveMediaSource?.postValue(repository.getConcatMediaSourceFromPlaylist(dataSourceFactory))
        }
    }

}