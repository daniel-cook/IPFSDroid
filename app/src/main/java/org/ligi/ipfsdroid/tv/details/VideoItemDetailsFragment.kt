package org.ligi.ipfsdroid.tv.details

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v17.leanback.app.DetailsSupportFragment
import android.support.v17.leanback.widget.*
import android.support.v17.leanback.widget.ListRow
import android.support.v17.leanback.widget.HeaderItem
import android.support.v17.leanback.widget.ArrayObjectAdapter
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.ligi.ipfsdroid.App
import org.ligi.ipfsdroid.model.Feed
import org.ligi.ipfsdroid.repository.Repository
import org.ligi.ipfsdroid.tv.StringPresenter
import java.io.BufferedInputStream
import javax.inject.Inject


/**
 * Created by WillowTree on 9/21/18.
 */
class VideoItemDetailsFragment : DetailsSupportFragment() {

    lateinit var rowsAdapter: ArrayObjectAdapter

    lateinit var feed: Feed

    @Inject
    lateinit var repository: Repository

    companion object {

        private const val ARG_FEED = "video_item_details_frag_arg"

        fun newInstance(feed: Feed): VideoItemDetailsFragment {
            val args: Bundle = Bundle()
            args.putParcelable(ARG_FEED, feed)
            val fragment = VideoItemDetailsFragment()
            fragment.arguments = args
            return fragment
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.component().inject(this)

        feed = arguments?.getParcelable<Feed>(ARG_FEED)!!

        doAsync {
            repository.getImageBitmapFromHash(feed.thumbNail) { it ->
                val bufferedInputStream = BufferedInputStream(it)
                val bmp = BitmapFactory.decodeStream(bufferedInputStream)

                uiThread {
                    buildDetails(BitmapDrawable(bmp))
                }
            }
        }

    }

    private fun buildDetails(bitmapDrawable: BitmapDrawable) {

        // TODO build in some more useful details and add means to click through to a video

        val selector = ClassPresenterSelector()
        val rowPresenter = FullWidthDetailsOverviewRowPresenter(DetailsDescriptionPresenter())

        selector.addClassPresenter(DetailsOverviewRow::class.java, rowPresenter)
        selector.addClassPresenter(ListRow::class.java, ListRowPresenter())

        rowsAdapter = ArrayObjectAdapter(selector)

        val detailsOverviewRow = DetailsOverviewRow(feed)

        detailsOverviewRow.imageDrawable = bitmapDrawable
        detailsOverviewRow.addAction(Action(1, "Watch Video"))
        detailsOverviewRow.addAction(Action(2, "Some Other Action"))
        detailsOverviewRow.addAction(Action(3, "Yet Another Action"))
        rowsAdapter.add(detailsOverviewRow)

        // Add a Related items row
        val listRowAdapter = ArrayObjectAdapter(
                StringPresenter())
        listRowAdapter.add("Media Item 1")
        listRowAdapter.add("Media Item 2")
        listRowAdapter.add("Media Item 3")
        val header = HeaderItem(0, "Related Items")
        rowsAdapter.add(ListRow(header, listRowAdapter))

        adapter = rowsAdapter
    }
}