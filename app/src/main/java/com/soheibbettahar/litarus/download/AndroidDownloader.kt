package com.soheibbettahar.litarus.download

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.app.DownloadManager.Request
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.webkit.MimeTypeMap
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.scan
import java.io.File
import java.lang.Long.min
import javax.inject.Inject

private const val TAG = "AndroidDownloader"

class AndroidDownloader @Inject constructor(@ApplicationContext val context: Context) : Downloader {

    private val downloadManager: DownloadManager by lazy { context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager }

    override fun downloadFile(
        downloadUrl: String,
        title: String,
        description: String,
        extension: String
    ): Long {
        val downloadUri = Uri.parse(downloadUrl)
        val request = Request(downloadUri).apply {
            setTitle(title)
            setDescription(description)
            setNotificationVisibility(Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setAllowedOverMetered(true)
            setAllowedOverRoaming(true)
            setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                formattedFileName(title, extension)
            )
        }

        return  downloadManager.enqueue(request)
    }

    override fun cancelDownload(downloadId: Long) {
        downloadManager.remove(downloadId)
    }


    private fun parseMimeType(url: String): String {
        val file = File(url)
        val map = MimeTypeMap.getSingleton()
        val ext = MimeTypeMap.getFileExtensionFromUrl(file.name)
        var type = map.getMimeTypeFromExtension(ext)
        type = type ?: "*/*"

        return type
    }

    private fun formattedFileName(fileName: String, extension: String): String =
        fileName.trim()
            .lowercase()
            .replace(Regex("[,:;.'_#` ]+"), "-") + extension

    private fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    // Checks if a volume containing external storage is available to at least read.
    fun isExternalStorageReadable(): Boolean {
        return Environment.getExternalStorageState() in setOf(
            Environment.MEDIA_MOUNTED,
            Environment.MEDIA_MOUNTED_READ_ONLY
        )
    }


    @SuppressLint("Range")
    override fun getFileUriString(downloadId: Long): String? {
        val cursor: Cursor =
            downloadManager.query(DownloadManager.Query().setFilterById(downloadId))

        return if (cursor.moveToFirst()) cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)) else null
    }


    override fun getDownloadStatus(downloadId: Long): Flow<DownloadStatus> =
        computeDownloadStatus(downloadId)
            .scan(DownloadStatus.NotDownloading) { prev: DownloadStatus, curr: DownloadStatus ->
                if (prev == DownloadStatus.NotDownloading && curr == DownloadStatus.Cancelled) prev
                else curr
            }

    @SuppressLint("Range")
    override fun getDownloadProgress(downloadId: Long): Flow<Float> = callbackFlow {
        var cursor: Cursor? = null
        var isDownloadFinished = false

        while (!isDownloadFinished) {
            cursor = downloadManager.query(DownloadManager.Query().setFilterById(downloadId))

            //download cancelled
            if (!cursor.moveToFirst()) {
                trySend(0f)
                isDownloadFinished = true
                close(CancellationException("Download Cancelled"))
            } else {

                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))

                //download failed
                if (status == DownloadManager.STATUS_FAILED) {
                    trySend(0f)
                    isDownloadFinished = true
                    close(CancellationException("Download Failed"))
                } else {
                    //download running, paused, pending or successful
                    val progress = computeDownloadProgress(cursor)
                    trySend(progress)
                }

                //download successful
                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    isDownloadFinished = true
                    close(CancellationException("Download Completed Successfully"))
                }

            }

            cursor?.close()
            delay(100)
        }

        awaitClose { cursor?.let { if (!it.isClosed) it.close() } }
    }


    @SuppressLint("Range")
    private fun computeDownloadStatus(downloadId: Long): Flow<DownloadStatus> = callbackFlow {
        var cursor: Cursor? = null
        var isDownloadFinished = false

        while (!isDownloadFinished) {
            cursor = downloadManager.query(DownloadManager.Query().setFilterById(downloadId))

            val status = if (cursor.moveToFirst())
                when (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
                    DownloadManager.STATUS_RUNNING -> DownloadStatus.Running
                    DownloadManager.STATUS_PAUSED -> {
                        val pauseReason = computePauseReason(cursor)
                        DownloadStatus.Paused(pauseReason)
                    }
                    DownloadManager.STATUS_SUCCESSFUL -> DownloadStatus.Successful
                    DownloadManager.STATUS_FAILED -> {
                        val error = computeDownloadError(cursor)
                        DownloadStatus.Failed(error)
                    }

                    else -> DownloadStatus.Pending
                }
            else
                DownloadStatus.Cancelled

            trySend(status)
            cursor?.close()


            if (status == DownloadStatus.Successful || status == DownloadStatus.Cancelled || status is DownloadStatus.Failed) {
                isDownloadFinished = true
                close()
            }

            delay(500)
        }

        awaitClose { cursor?.let { if (!it.isClosed) it.close() } }
    }

    @SuppressLint("Range")
    private fun computeDownloadProgress(cursor: Cursor): Float {
        val totalBytes: Long = min(
            cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)),
            Long.MAX_VALUE
        )

        val downloadedBytes: Long =
            cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))

        return (downloadedBytes.toFloat() / totalBytes.toFloat())
    }


    @SuppressLint("Range")
    private fun computeDownloadError(cursor: Cursor): DownloadError {
        return when (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_REASON))) {
            DownloadManager.ERROR_INSUFFICIENT_SPACE, DownloadManager.ERROR_DEVICE_NOT_FOUND -> DownloadError.InsufficientSpaceError
            DownloadManager.ERROR_HTTP_DATA_ERROR, DownloadManager.ERROR_UNHANDLED_HTTP_CODE, DownloadManager.ERROR_TOO_MANY_REDIRECTS -> DownloadError.HttpError
            else -> DownloadError.UnknownError
        }
    }

    @SuppressLint("Range")
    private fun computePauseReason(cursor: Cursor): PauseReason {
        return when (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_REASON))) {
            DownloadManager.PAUSED_WAITING_TO_RETRY -> PauseReason.WaitingToRetry
            DownloadManager.PAUSED_QUEUED_FOR_WIFI -> PauseReason.QueuedForWifi
            DownloadManager.PAUSED_WAITING_FOR_NETWORK -> PauseReason.WaitingForNetwork
            else -> PauseReason.Unknown
        }
    }

}