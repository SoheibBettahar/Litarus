package com.soheibbettahar.litarus.data.repository

import androidx.paging.PagingData
import com.soheibbettahar.litarus.data.repository.model.Book
import com.soheibbettahar.litarus.data.repository.model.BookWithExtras
import com.soheibbettahar.litarus.download.DownloadStatus
import kotlinx.coroutines.flow.Flow


interface BooksRepository {
    fun searchBooks(searchText: String, category: String, languages: List<String>): Flow<PagingData<Book>>

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

}