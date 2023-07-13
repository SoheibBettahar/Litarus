package com.soheibbettahar.litarus.download

import kotlinx.coroutines.flow.Flow


interface Downloader {
    fun downloadFile(downloadUrl: String, title: String, description: String, extension: String): Long

    fun cancelDownload(downloadId: Long)

    fun getFileUriString(downloadId: Long): String?

    fun getDownloadProgress(downloadId: Long): Flow<Float>

    fun getDownloadStatus(downloadId: Long): Flow<DownloadStatus>

}