package com.example.guttenburg.data.repository

import androidx.paging.PagingData
import com.example.guttenburg.download.DownloadStatus
import com.example.guttenburg.reader.models.EpubBook
import kotlinx.coroutines.flow.Flow


interface BooksRepository {
    fun searchBooks(category: String, searchText: String): Flow<PagingData<Book>>

    fun getBooks(): Flow<PagingData<Book>>

    suspend fun downloadBook(book: BookWithExtras)

     suspend fun fetchBookWithExtras(
        id: Long,
        title: String,
        author: String
    )

     fun getBookWithExtras(id: Long): Flow<BookWithExtras>

    suspend fun containsBookWithExtras(id: Long): Boolean

    suspend fun addFileUriToDownloadedBook(downloadId: Long)

    suspend fun removeFileUriFromBook(book: BookWithExtras)

    fun getDownloadProgress(downloadId: Long): Flow<Float>

    fun getDownloadStatus(downloadId: Long): Flow<DownloadStatus>

    fun cancelDownload(downloadId: Long)

    fun loadEbook(bookId: Long): Flow<EpubBook>
}