package com.soheibbettahar.litarus.download

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.*
import com.soheibbettahar.litarus.data.repository.BooksRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


private const val TAG = "DownloadReceiver"

@Singleton
@AndroidEntryPoint
class DownloadReceiver @Inject constructor() :
    BroadcastReceiver() {

    @Inject
    lateinit var booksRepository: BooksRepository

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
            val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)
            if (downloadId != -1L) {
                Log.d(TAG, "onReceive: downloadID: $downloadId ACTION_DOWNLOAD_COMPLETE called")
                DownloadWorker.enqueueRequest(context, downloadId)

            }
        }

    }

}