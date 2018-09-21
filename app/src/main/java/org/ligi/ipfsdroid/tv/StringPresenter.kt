package org.ligi.ipfsdroid.tv

import android.support.v17.leanback.widget.Presenter
import android.view.ViewGroup
import android.widget.TextView

/**
 * Created by WillowTree on 9/20/18.
 */
class StringPresenter : Presenter() {
    override fun onCreateViewHolder(parent: ViewGroup?): ViewHolder {
        val textView = TextView(parent?.getContext())
        textView.isFocusable = true
        textView.isFocusableInTouchMode = true
//        textView.background = parent.getContext().resources.getDrawable(R.drawable.text_bg)
        return Presenter.ViewHolder(textView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder?, item: Any?) {
        (viewHolder?.view as TextView).text = item.toString()
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder?) {
        // no op
    }

}