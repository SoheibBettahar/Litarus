package com.example.guttenburg.data.repository

import android.net.Uri

data class BookWithExtras(
    val id: Long,
    val title: String,
    val description: String,
    val pageCount: Int?,
    val epubDownloadUrl: String?,
    val imageUrl: String,
    val downloadCount: Int? = null,
    val language: String? = null,
    val authors: String = "",
    val downloadId: Long?,
    val fileUri: Uri?
) {
    fun areInfoAvailable() = authors.isNotBlank() || downloadCount != null || language != null


}


