package com.example.guttenburg.download

sealed class DownloadStatus {
    object NotDownloading : DownloadStatus()
    object Running : DownloadStatus()
    object Paused : DownloadStatus()
    object Pending : DownloadStatus()
    object Cancelled : DownloadStatus()
    object Successful : DownloadStatus()
    data class Failed(val error: DownloadError): DownloadStatus()
}

sealed class DownloadError {
    object InsufficientSpaceError : DownloadError()
    object HttpError : DownloadError()
    object UnknownError : DownloadError()
}