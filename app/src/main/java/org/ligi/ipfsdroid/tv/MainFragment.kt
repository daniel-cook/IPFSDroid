package org.ligi.ipfsdroid.tv

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v17.leanback.app.BrowseSupportFragment
import org.ligi.ipfsdroid.R
import android.util.DisplayMetrics
import android.support.v17.leanback.app.BackgroundManager
import android.support.v17.leanback.widget.*
import org.ligi.ipfsdroid.App
import org.ligi.ipfsdroid.model.BroadCasterWithFeed
import org.ligi.ipfsdroid.model.Feed
import org.ligi.ipfsdroid.repository.Repository
import javax.inject.Inject
import android.support.v17.leanback.widget.ListRow
import android.support.v17.leanback.widget.HeaderItem
import android.support.v17.leanback.widget.ArrayObjectAdapter




/**
 * Created by WillowTree on 9/18/18.
 */
class MainFragment : BrowseSupportFragment() {

    private var backgroundManager: BackgroundManager? = null
    private var defaultBackground: Drawable? = null
    private var metrics: DisplayMetrics? = null

    private var categoryRowAdapter: ArrayObjectAdapter? = null

    @Inject
    lateinit var repository: Repository

    companion object {
        const val NUM_ROWS = 4
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.component().inject(this)

        val viewModel = ViewModelProviders.of(this).get(MainFragmentViewModel::class.java)
        viewModel.repository = repository
        viewModel.getBroadCastersWithFeed().observe(this, Observer(::updateBroadCastersView))

        buildDummyAdapter()
    }

    fun updateBroadCastersView(data: List<BroadCasterWithFeed>?) {
        data?.let {
            buildRowsAdapter(it)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        prepareBackgroundManager()
        setupUIElements()
        setupEventListeners()
        prepareEntranceTransition()
    }

    private fun buildRowsAdapter(data: List<BroadCasterWithFeed>) {

        categoryRowAdapter?.clear()
        val videoItemPresenter = VideoItemPresenter(repository)

        for ((index, value: BroadCasterWithFeed) in data.withIndex()) {
            val listRowAdapter = ArrayObjectAdapter(videoItemPresenter)
            for (feedItem in value.feedsList.content) {
                listRowAdapter.add(feedItem)
            }
            val headerItem = HeaderItem(index.toLong(), value.broadcaster.name)
            categoryRowAdapter?.add(ListRow(headerItem, listRowAdapter))
        }

        adapter = categoryRowAdapter
    }

    /**
     * This hack is required in order to be able to update the rows on the screen when the ViewModel
     * is updated.  The reason for this is that inside BrowseSupportFragment, if mMainFragmentListRowDataAdapter
     * and mMainFragmentRowsAdapter are null when onCreateView() is called, the method updateMainFragmentRowsAdapter()
     * will never update the view on the screen.  Therefore the RowAdapter needs to be populated
     * with a ListRow at this time.  This could potentially be replaced with a ListRow that shows
     * a loading screen.
     */
    private fun buildDummyAdapter() {
        categoryRowAdapter = ArrayObjectAdapter(ListRowPresenter())
        for (i in 0 until NUM_ROWS) {
            val listRowAdapter = ArrayObjectAdapter(
                    StringPresenter())
            listRowAdapter.add("")
            val header = HeaderItem(i.toLong(), "")
            categoryRowAdapter?.add(ListRow(header, listRowAdapter))
        }
        adapter = categoryRowAdapter
    }

    private fun prepareBackgroundManager() {
        backgroundManager = BackgroundManager.getInstance(activity!!)
        backgroundManager?.attach(activity!!.window)
        defaultBackground = resources
                .getDrawable(R.drawable.default_background)
        metrics = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(metrics)
    }

    private fun setupUIElements() {
        badgeDrawable = activity!!.resources
                .getDrawable(R.drawable.interplanetary_banner)
        // Badge, when set, takes precedent over title
        title = getString(R.string.browse_title)
        headersState = BrowseSupportFragment.HEADERS_DISABLED
        isHeadersTransitionOnBackEnabled = true
        // set headers background color
        brandColor = resources.getColor(R.color.fastlane_background)
        // set search icon color
        searchAffordanceColor = resources.getColor(R.color.search_opaque)

    }

    fun updateBackground(feed: Feed) {
        // TODO get the thumbnail as a drawable to set the background
        /*
        Note: The implementation above is a simple example shown for purposes of illustration. When
        creating this function in your own app, you should consider running the background update
        action in a separate thread for better performance. In addition, if you are planning on
        updating the background in response to users scrolling through items, consider adding a
        time to delay a background image update until the user settles on an item. This technique
        avoids excessive background image updates.
         */
//        BackgroundManager.getInstance(activity).drawable = drawable
    }

    fun clearBackground() {
        BackgroundManager.getInstance(activity).drawable = defaultBackground
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
            if(item is Feed) {
                updateBackground(item)  //TODO could I get the actual image out of this?
            } else {
                clearBackground()
            }
        }
    }

    private fun updateRecommendations() {
        // TODO
//        val recommendationIntent = Intent(activity, UpdateRecommendationsService::class.java)
//        activity.startService(recommendationIntent)
    }

}