package org.ligi.ipfsdroid.tv

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v17.leanback.app.BrowseSupportFragment
import org.ligi.ipfsdroid.R
import android.util.DisplayMetrics
import android.support.v17.leanback.app.BackgroundManager
import android.content.Intent
import android.support.v17.leanback.widget.*
import android.view.View


/**
 * Created by WillowTree on 9/18/18.
 */
class MainFragment : BrowseSupportFragment() {

    // TODO this needs to have a ViewModel like BrowseActivity does and use that to load the data up into the adapter

    private var mBackgroundManager: BackgroundManager? = null
    private var mDefaultBackground: Drawable? = null
    private var mMetrics: DisplayMetrics? = null

    private var mCategoryRowAdapter: ArrayObjectAdapter? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        loadVideoData()
        prepareBackgroundManager()
        setupUIElements()
        setupEventListeners()
        prepareEntranceTransition()

        mCategoryRowAdapter = ArrayObjectAdapter(ListRowPresenter())
        adapter = mCategoryRowAdapter
    }

    private fun loadVideoData() {
//        TODO
    }


    private fun prepareBackgroundManager() {
        mBackgroundManager = BackgroundManager.getInstance(activity!!)
        mBackgroundManager?.attach(activity!!.window)
        mDefaultBackground = resources
                .getDrawable(R.drawable.default_background)
        mMetrics = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(mMetrics)
    }

    private fun setupUIElements() {
        badgeDrawable = activity!!.resources
                .getDrawable(R.drawable.videos_by_google_banner)
        // Badge, when set, takes precedent over title
        title = getString(R.string.browse_title)
        headersState = BrowseSupportFragment.HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true
        // set headers background color
        brandColor = resources.getColor(R.color.fastlane_background)
        // set search icon color
        searchAffordanceColor = resources.getColor(R.color.search_opaque)
    }

    private fun setupEventListeners() {
        setOnSearchClickedListener {
//            val intent = Intent(activity, SearchActivity::class.java)
//            startActivity(intent)
        }

        setOnItemViewClickedListener { itemViewHolder, item, rowViewHolder, row ->
            // TODO
        }

        setOnItemViewSelectedListener { itemViewHolder, item, rowViewHolder, row ->
            // TODO
        }
    }

    private fun updateRecommendations() {
        // TODO
//        val recommendationIntent = Intent(activity, UpdateRecommendationsService::class.java)
//        activity.startService(recommendationIntent)
    }

}