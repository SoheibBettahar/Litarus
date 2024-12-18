package com.soheibbettahar.litarus.data.network.guttendexApi

import com.soheibbettahar.litarus.data.database.paging.DatabaseBook
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


fun NetworkBook.asDatabaseModel(page: Int): DatabaseBook {

    return DatabaseBook(
        id,
        title ?: "",
        authors = authors.mapNotNull { it.name }.joinToString(separator = "#"),
        imageUrl = formats?.imageJpeg ?: "",
        subjects = getSubjectsText(subjects),
        languages = languages.joinToString(","),
        page = page,
    )
}


private fun getSubjectsText(subjects: List<String>): String {
    val subjectSet = mutableSetOf<String>()

    for (subject in subjects) {
        val genresInSubject = subject.split(" -- ")

        for (genre in genresInSubject) subjectSet.add(genre)
    }

    return subjectSet.joinToString(", ")
}


