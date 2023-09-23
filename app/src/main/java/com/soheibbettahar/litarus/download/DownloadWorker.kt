package com.soheibbettahar.litarus.download

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.soheibbettahar.litarus.MainActivity
import com.soheibbettahar.litarus.R
import com.soheibbettahar.litarus.data.repository.BooksRepository
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

) : CoroutineWorker(context, workerParams) {

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

        private const val NOTIFICATION_CHANNEL_ID = "11"
        private const val NOTIFICATION_CHANNEL_NAME = "Work Service"
    }

    @SuppressLint("RestrictedApi")
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val downloadId = inputData.getLong(DOWNLOAD_ID_KEY, -1)
                repository.addFileUriToDownloadedBook(downloadId)
                val success = Result.Success()
                success
            } catch (throwable: Throwable) {
                Result.Retry()
            }
        }

    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val pendingIntentFlag =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE else 0

        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setContentIntent(
                PendingIntent.getActivity(
                    context,
                    0,
                    Intent(context, MainActivity::class.java),
                    pendingIntentFlag
                )
            )

            .setSmallIcon(R.mipmap.ic_launcher)
            .setOngoing(true)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setContentTitle(context.getString(R.string.app_name))
            .setLocalOnly(true)
            .setVisibility(NotificationCompat.VISIBILITY_SECRET)
            .setContentText(context.getString(R.string.downloading_book))
            .build()
        return ForegroundInfo(1337, notification)
    }

}