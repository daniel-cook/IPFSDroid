package org.ligi.ipfsdroid.tv.details

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import org.ligi.ipfsdroid.R
import org.ligi.ipfsdroid.model.Feed

class VideoItemDetailsActivity : FragmentActivity() {

    companion object {
        const val EXTRA_FEED = "feed"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_item_details)

        val feed = intent.extras.get(EXTRA_FEED) as Feed

        supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, VideoItemDetailsFragment.newInstance(feed))
                .commit()
    }

}
