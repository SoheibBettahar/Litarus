package com.example.guttenburg.data.database.detail

import androidx.core.net.toUri
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.guttenburg.data.network.googleBooksApi.NetworkGoogleBookInfo
import com.example.guttenburg.data.network.guttendexApi.NetworkBook
import com.example.guttenburg.data.repository.model.BookWithExtras
import java.util.*

@Entity(tableName = "book_with_extras")
data class DatabaseBookWithExtras(
    @PrimaryKey val id: Long,
    val title: String,
    val description: String,
    val pageCount: Int?,
    val downloadUrl: String?,
    val fileExtension: String,
    val htmlPreviewLink: String?,
    val imageUrl: String,
    val downloadCount: Int? = null,
    val language: String? = null,
    val authors: String = "",
    val downloadId: Long? = null,
    val fileUriString: String? = null
) {

    companion object {
        fun from(book: NetworkBook, extras: NetworkGoogleBookInfo?): DatabaseBookWithExtras {
            val downloadUrl = book.formats?.applicationEpubZip ?: book.formats?.applicationPdf
            val fileExtension = book.formats?.applicationEpubZip?.let { ".epub" } ?: ".pdf"

            return DatabaseBookWithExtras(
                book.id,
                book.title ?: "",
                extras?.description ?: "",
                extras?.pageCount,
                imageUrl = book.formats?.imageJpeg ?: "",
                downloadUrl = book.formats?.applicationEpubZip ?: book.formats?.applicationPdf,
                fileExtension = fileExtension,
                htmlPreviewLink = book.formats?.textHtml,
                downloadCount = book.downloadCount,
                language = book.languages.firstOrNull().displayName(),
                authors = book.authors.map { it.name }.joinToString(separator = ", ")
            )
        }


        private fun String?.displayName(): String? {
            return this?.let { Locale(this).displayName }
        }
    }
}

fun DatabaseBookWithExtras.asExternalModel(): BookWithExtras = BookWithExtras(
    id,
    title,
    description,
    pageCount,
    imageUrl = imageUrl,
    downloadUrl = downloadUrl,
    downloadCount = downloadCount,
    fileExtension = fileExtension,
    language = language,
    authors = authors,
    fileUri = fileUriString?.toUri(),
    downloadId = downloadId
)