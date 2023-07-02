package com.soheibbettahar.litarus.data.repository.model

import android.net.Uri

data class BookWithExtras(
    val id: Long,
    val title: String,
    val description: String,
    val pageCount: Int = -1,
    val downloadUrl: String?,
    val fileExtension: String,
    val imageUrl: String,
    val downloadCount: Int = -1,
    val languages: String = "",
    val authors: String = "",
    val downloadId: Long?,
    val fileUri: Uri?
) {
    fun areInfoAvailable() = pageCount > 0 || downloadCount >-1 || languages.isNotEmpty()
}


