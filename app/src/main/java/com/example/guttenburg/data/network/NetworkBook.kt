package com.example.guttenburg.data.network

import android.util.Log
import com.example.guttenburg.data.repository.Book
import com.squareup.moshi.Json

private const val TAG = "NetworkBook"

data class NetworkBook(
    @Json(name = "id") val id: Long,
    @Json(name = "title") val title: String? = null,
    @Json(name = "authors") val authors: List<NetworkPerson> = arrayListOf(),
    @Json(name = "translators") val translators: List<NetworkPerson> = arrayListOf(),
    @Json(name = "subjects") val subjects: List<String> = arrayListOf(),
    @Json(name = "bookshelves") val bookshelves: List<String> = arrayListOf(),
    @Json(name = "languages") val languages: List<String> = arrayListOf(),
    @Json(name = "copyright") val copyright: Boolean? = null,
    @Json(name = "media_type") val mediaType: String? = null,
    @Json(name = "formats") val formats: NetworkFormats? = null,
    @Json(name = "download_count") val downloadCount: Int? = null
)


fun NetworkBook.asExternalModel(): Book{
    Log.d(TAG, "NetworkBook: $this")

    return Book(
        id,
        title ?: "",
        authors = authors.mapNotNull { it.name },
        imageUrl = formats?.imageJpeg ?: ""
    )
}
