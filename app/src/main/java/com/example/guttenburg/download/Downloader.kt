package com.example.guttenburg.download

import kotlinx.coroutines.flow.Flow


interface Downloader {
    fun downloadFile(url: String, title: String, description: String): Long

    fun cancelDownload(downloadId: Long)

    fun getFileUriString(downloadId: Long): String?

    fun getDownloadProgress(downloadId: Long): Flow<Float>

    fun getDownloadStatus(downloadId: Long): Flow<DownloadStatus>

}