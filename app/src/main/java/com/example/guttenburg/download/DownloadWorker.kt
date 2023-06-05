package com.example.guttenburg.download

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.example.guttenburg.data.repository.BooksRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Duration

private const val TAG = "DownloadWorker"
private const val DOWNLOAD_ID_KEY = "downloadId"

@HiltWorker
class DownloadWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted workerParams: WorkerParameters,
    val repository: BooksRepository

) :
    CoroutineWorker(context, workerParams) {

    companion object {
        fun enqueueRequest(context: Context, downloadId: Long) {
            val inputData = Data.Builder()
                .putLong(DOWNLOAD_ID_KEY, downloadId)
                .build()

            val workRequest: WorkRequest = OneTimeWorkRequestBuilder<DownloadWorker>()
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.LINEAR, Duration.ofSeconds(3))
                .build()

            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }

    @SuppressLint("RestrictedApi")
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val downloadId = inputData.getLong(DOWNLOAD_ID_KEY, -1)
                Log.d(TAG, "doWork: downloadId: $downloadId")
                repository.addFileUriToDownloadedBook(downloadId)
                Result.Success()
            } catch (throwable: Throwable) {
                Result.Retry()
            }
        }

    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        //TODO: show download notification on android versions < 12 otherwise the app will crash
        //see: https://stackoverflow.com/questions/69627330/expedited-workrequests-require-a-listenableworker-to-provide-an-implementation-f
        //see: https://developer.android.com/guide/background/persistent/getting-started/define-work#backwards-compat
        return super.getForegroundInfo()
    }

}