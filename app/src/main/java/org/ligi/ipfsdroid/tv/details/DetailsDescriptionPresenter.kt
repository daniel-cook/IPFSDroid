package org.ligi.ipfsdroid.tv.details

import android.support.v17.leanback.widget.AbstractDetailsDescriptionPresenter
import org.ligi.ipfsdroid.model.Feed

/**
 * Created by WillowTree on 9/21/18.
 */
class DetailsDescriptionPresenter : AbstractDetailsDescriptionPresenter() {

    override fun onBindDescription(vh: ViewHolder?, item: Any?) {

        val feed = item as Feed

        vh?.let {
            it.title.text = feed.title
            it.subtitle.text = feed.fileName
            it.body.text = feed.description
        }

    }

}