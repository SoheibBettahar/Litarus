package com.example.guttenburg.data.repository

import com.example.guttenburg.data.network.googleBooksApi.NetworkGoogleBookInfo
import com.example.guttenburg.data.network.guttendexApi.NetworkBook
import java.util.Locale

data class BookWithExtras(
    val id: Long,
    val title: String,
    val description: String,
    val pageCount: Int?,
    val epubDownloadUrl: String,
    val imageUrl: String,
    val downloadCount: Int? = null,
    val language: String? = null,
    val authors: String = ""
) {

    companion object {
        fun from(book: NetworkBook, extras: NetworkGoogleBookInfo?) =
            BookWithExtras(
                book.id,
                book.title ?: "",
                extras?.description ?: "",
                extras?.pageCount,
                imageUrl = book.formats?.imageJpeg ?: "",
                epubDownloadUrl = book.formats?.applicationEpubZip ?: "",
                downloadCount = book.downloadCount,
                language = book.languages.firstOrNull().displayName(),
                authors = book.authors.map { it.name }.joinToString(separator = ", ")
            )


        private fun String?.displayName(): String? {
            return this?.let { Locale(this).displayName }
        }
    }

    fun areInfoAvailable() = authors.isNotBlank() || downloadCount != null || language != null


}


