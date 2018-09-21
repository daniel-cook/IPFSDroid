package org.ligi.ipfsdroid.tv.details

import android.os.Bundle
import android.support.v17.leanback.app.DetailsSupportFragment
import android.support.v17.leanback.widget.*
import org.ligi.ipfsdroid.R
import android.support.v17.leanback.widget.ListRow
import android.support.v17.leanback.widget.HeaderItem
import android.support.v17.leanback.widget.ArrayObjectAdapter
import org.ligi.ipfsdroid.model.Feed
import org.ligi.ipfsdroid.tv.StringPresenter


/**
 * Created by WillowTree on 9/21/18.
 */
class VideoItemDetailsFragment : DetailsSupportFragment() {

    lateinit var rowsAdapter: ArrayObjectAdapter

    lateinit var feed: Feed

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

        feed = arguments?.getParcelable<Feed>(ARG_FEED)!!

        buildDetails()
    }

    private fun buildDetails() {

        val selector = ClassPresenterSelector()
        val rowPresenter = FullWidthDetailsOverviewRowPresenter(DetailsDescriptionPresenter())

        selector.addClassPresenter(DetailsOverviewRow::class.java, rowPresenter)
        selector.addClassPresenter(ListRow::class.java, ListRowPresenter())

        rowsAdapter = ArrayObjectAdapter(selector)

        val detailsOverviewRow = DetailsOverviewRow(feed)

        detailsOverviewRow.imageDrawable = activity?.resources?.getDrawable(R.drawable.ic_play_arrow)
        detailsOverviewRow.addAction(Action(1, "Buy $9.99"))
        detailsOverviewRow.addAction(Action(2, "Rent $2.99"))
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