package org.ligi.ipfsdroid.activities.player

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import org.ligi.ipfsdroid.R
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import kotlinx.android.synthetic.main.activity_exoplayer.*
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import org.ligi.ipfsdroid.App
import org.ligi.ipfsdroid.repository.PlaylistItem
import org.ligi.ipfsdroid.repository.Repository
import javax.inject.Inject


class ExoplayerActivity : AppCompatActivity() {

    @Inject
    lateinit var repository: Repository

    private var exoPlayer: SimpleExoPlayer? = null

    private var playWhenReady: Boolean = false

    private var currentWindow: Int = 0

    private var playbackPosition: Long = 0

    private val TAG = ExoplayerActivity::class.qualifiedName

    private var uri : Uri? = null

    private var currentlyPlayingItem: PlaylistItem? = null

    private val bandwidthMeter = DefaultBandwidthMeter()

    private lateinit var dataSourceFactory : DefaultDataSourceFactory

    private var concatenatingMediaSource: ConcatenatingMediaSource? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exoplayer)

        App.component().inject(this)

        dataSourceFactory = DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "interPlanetaryBroadcast"), bandwidthMeter)

        val viewModel = ViewModelProviders.of(this).get(PlayerViewModel::class.java)

        // TODO setting these from outside is a code smell, there has to be a better way to construct the view model, else bite the bullet and use Dagger which seems rather involved for this
        viewModel.repository = repository
        viewModel.dataSourceFactory = dataSourceFactory
        viewModel.getConcatMediaItems()?.observe(this, Observer {
            concatenatingMediaSource = it
            initializePlayer()
        })
    }

    // TODO player state is not properly maintained when returning from the background

    // TODO how do I show chainplay indicator?  Worthwhile?

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) { // On 24 and up, multi-window support dictates that we have the player initialized when visible but not active
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUi()
        if ((Util.SDK_INT <= 23 || exoPlayer == null)) {  // The differences here seem to be an optimization. It would work on all systems if done in onStart, but we want to wait as long as possible to load up these resources
            initializePlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        // Before API 24 there is no guarantee of onStop being called, so we need to release the player
        // in onPause()
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        // After API 23 there is a guarantee of onStop being called, so we can release resources here
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }

    private fun initializePlayer() {
        exoPlayer = ExoPlayerFactory.newSimpleInstance(
                DefaultRenderersFactory(this),
                DefaultTrackSelector(), DefaultLoadControl())

        video_view.player = exoPlayer

        exoPlayer?.let { simpleExoPlayer ->
            simpleExoPlayer.playWhenReady = playWhenReady
            simpleExoPlayer.seekTo(currentWindow, playbackPosition)
            concatenatingMediaSource?.let {
                simpleExoPlayer.prepare(it, true, false)
                logPlayerStatus("initializePlayer()")
            }
        }
    }

    private fun releasePlayer() {
        exoPlayer?.let {
            playbackPosition = it.currentPosition
            currentWindow = it.currentWindowIndex
            playWhenReady = it.playWhenReady
            it.release()
        }
        logPlayerStatus("releasePlayer()")
        exoPlayer = null
    }

    private fun logPlayerStatus(prefix: String) {
        Log.d(TAG, "$prefix -- Playback Position = $playbackPosition; PlaywhenReady = $playWhenReady")
    }
    /**
     * Provides a full screen experience
     */
    private fun hideSystemUi() {

        // TODO we need to unhide this when the user clicks on the screen
//        video_view.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
//                or View.SYSTEM_UI_FLAG_FULLSCREEN
//                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }
}
