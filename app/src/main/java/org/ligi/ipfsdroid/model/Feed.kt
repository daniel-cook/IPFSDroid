package org.ligi.ipfsdroid.model

import android.os.Parcel
import android.os.Parcelable
import org.ligi.ipfsdroid.repository.PlaylistItem

/**
 * Created by WillowTree on 8/29/18.
 */
data class Feed(val title: String,
                val description: String,
                val fileName: String,
                val link: String,
                val thumbNail: String) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    fun isInPlayList(playlist: List<PlaylistItem>) : Boolean {
        for (item in playlist) {
            if (item.hash == link) {
                return true
            }
        }
        return false
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(fileName)
        parcel.writeString(link)
        parcel.writeString(thumbNail)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Feed> {
        override fun createFromParcel(parcel: Parcel): Feed {
            return Feed(parcel)
        }

        override fun newArray(size: Int): Array<Feed?> {
            return arrayOfNulls(size)
        }
    }
}