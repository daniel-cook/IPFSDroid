package org.ligi.ipfsdroid.tv

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.View
import io.ipfs.kotlin.model.VersionInfo
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.ligi.ipfsdroid.*
import org.ligi.ipfsdroid.repository.Repository
import org.ligi.kaxt.setVisibility
import org.ligi.kaxtui.alert
import javax.inject.Inject

class TvActivity : FragmentActivity() {

    private val ipfsDaemon = IPFSDaemon(this)

    @Inject
    lateinit var repository: Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tv)

        App.component().inject(this)

        if(ipfsDaemon.isReady() && State.isDaemonRunning) {
            showBrowseFragment(savedInstanceState)
        } else {
            startService(Intent(this, IPFSDaemonService::class.java))

            State.isDaemonRunning = true

            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("starting daemon")
            progressDialog.show()


            doAsync {

                var version: VersionInfo? = null
                while (version == null) {
                    try {
                        version = repository.getIpfsVersion()
                        version?.let { ipfsDaemon.getVersionFile().writeText(it.Version) }
                    } catch (ignored: Exception) {
                    }
                }

                uiThread {
                    progressDialog.dismiss()
                    showBrowseFragment(savedInstanceState)
                }
            }
        }

        downloadIPFSButton.setOnClickListener {
            ipfsDaemon.download(this, runInit = true) {
                ipfsDaemon.getVersionFile().writeText(assets.open("version").reader().readText())
                refresh()
            }
        }

        updateIPFSButton.setOnClickListener {
            if (State.isDaemonRunning) {
                alert("Please stop daemon first")
            } else {
                ipfsDaemon.download(this, runInit = false) {
                    daemonButton.callOnClick()
                    refresh()
                }
            }
        }

        refresh()
    }

    private fun showBrowseFragment(savedInstanceState: Bundle?) {
        hideAllButtons()

        if(savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_container, MainFragment())
                    .commit()
        }
    }

    private fun hideAllButtons() {
        downloadIPFSButton.visibility = View.GONE
        updateIPFSButton.visibility = View.GONE
    }

    private fun refresh() {
        downloadIPFSButton.setVisibility(!ipfsDaemon.isReady())
        val currentVersionText = ipfsDaemon.getVersionFile().let {
            if (it.exists()) it.readText() else ""
        }
        val availableVersionText = assets.open("version").reader().readText()
        updateIPFSButton.setVisibility(ipfsDaemon.isReady()
                && (currentVersionText.isEmpty() || (currentVersionText != availableVersionText)))
    }
}
