package org.ligi.ipfsdroid.di

import android.content.Context
import android.os.Build
import android.support.annotation.NonNull
import android.util.Log
import dagger.Module
import dagger.Provides
import io.ipfs.kotlin.IPFS
import okhttp3.OkHttpClient
import org.ligi.ipfsdroid.repository.Repository
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class AppModule(val context: Context) {

    @Singleton
    @Provides
    @NonNull
    fun provideContext() : Context {
        return context
    }

    @Singleton
    @Provides
    internal fun provideOkhttp() = OkHttpClient.Builder().let { builder ->
        builder.connectTimeout(1000, TimeUnit.SECONDS)
        builder.readTimeout(1000, TimeUnit.SECONDS)
        builder.build()
    }

    @Singleton
    @Provides
    internal fun provideIPFS(providedOkHttp: OkHttpClient): IPFS {
        // The Localhost ip address is different on the emulator than on a physical device
        return if(Build.MODEL.contains("google_sdk") ||
                Build.MODEL.contains("Emulator") ||
                Build.MODEL.contains("Android SDK")) {
            IPFS(okHttpClient = providedOkHttp, base_url = "http://10.0.2.2:5001/api/v0/")
        } else {
            IPFS(okHttpClient = providedOkHttp)
        }
    }


    @Singleton
    @Provides
    internal fun provideRepository(ipfs: IPFS) = Repository(ipfs = ipfs)

}
