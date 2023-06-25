package com.soheibbettahar.litarus.download

sealed class DownloadStatus {
    object NotDownloading : DownloadStatus()
    object Running : DownloadStatus()
    data class Paused(val reason: PauseReason) : DownloadStatus()
    object Pending : DownloadStatus()
    object Cancelled : DownloadStatus()
    object Successful : DownloadStatus()
    data class Failed(val error: DownloadError) : DownloadStatus()
}

sealed class DownloadError {
    object InsufficientSpaceError : DownloadError()
    object HttpError : DownloadError()
    object UnknownError : DownloadError()
}

sealed class PauseReason {
    object WaitingToRetry: PauseReason()
    object WaitingForNetwork: PauseReason()
    object QueuedForWifi: PauseReason()
    object Unknown: PauseReason()
}

