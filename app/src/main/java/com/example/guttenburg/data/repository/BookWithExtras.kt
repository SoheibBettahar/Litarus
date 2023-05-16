package com.example.guttenburg.data.repository

import com.example.guttenburg.data.network.NetworkBook
import com.example.guttenburg.data.network.NetworkGoogleBookInfo
import java.util.Locale

data class BookWithExtras(
    val id: Long,
    val title: String,
    val description: String? = null,
    val pageCount: Int? = null,
    val texPlain: String,
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
                extras?.pageCount ?: -1,
                imageUrl = book.formats?.imageJpeg ?: "",
                texPlain = book.formats?.textPlain ?: "",
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


