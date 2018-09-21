package org.ligi.ipfsdroid.tv

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import org.jetbrains.anko.doAsync
import org.ligi.ipfsdroid.model.BroadCasterWithFeed
import org.ligi.ipfsdroid.repository.Repository

/**
 * Created by WillowTree on 9/20/18.
 */
class MainFragmentViewModel : ViewModel() {

    private var broadCastersWithFeed: MutableLiveData<List<BroadCasterWithFeed>>? = null

    lateinit var repository: Repository

    fun getBroadCastersWithFeed(): LiveData<List<BroadCasterWithFeed>> {
        if (broadCastersWithFeed == null) {
            broadCastersWithFeed = MutableLiveData()
            loadBroadCasters()
        }
        return broadCastersWithFeed!!
    }

    private fun loadBroadCasters() {
        doAsync {
            broadCastersWithFeed?.postValue(repository.getBroadCastersWithFeeds())
        }
    }
}