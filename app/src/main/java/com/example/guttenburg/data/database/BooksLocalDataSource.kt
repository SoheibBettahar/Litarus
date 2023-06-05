package com.example.guttenburg.data.database

import androidx.paging.PagingSource
import com.example.guttenburg.data.database.detail.DatabaseBookWithExtras
import com.example.guttenburg.data.database.paging.DatabaseBook
import com.example.guttenburg.data.database.paging.RemoteKeys
import kotlinx.coroutines.flow.Flow

interface BooksLocalDataSource {


    fun booksByNameOrAuthorAndCategory(
        searchText: String,
        category: String
    ): PagingSource<Int, DatabaseBook>

    fun getAllBooks(): PagingSource<Int, DatabaseBook>

    suspend fun insertAllBooks(books: List<DatabaseBook>)

    suspend fun insertBook(book: DatabaseBook)

    suspend fun deleteBook(book: DatabaseBook)

    suspend fun clearAllBooks()

    suspend fun insertAllRemoteKeys(remoteKeys: List<RemoteKeys>)

    suspend fun remoteKeysByBookId(bookId: Long): RemoteKeys?

    suspend fun clearAllRemoteKeys()

    suspend fun <R> withTransaction(block: suspend () -> R): R

    suspend fun booksSize(): Int

    suspend fun remoteKeysSize(): Int

    suspend fun insertDownloadKey(bookId: Long, downloadId: Long)

    suspend fun insertBookWithExtras(bookWithExtras: DatabaseBookWithExtras)

    suspend fun containsBookWithExtras(id: Long): Boolean

    fun getBookWithExtras(id: Long): Flow<DatabaseBookWithExtras>

    suspend fun getBookWithExtrasByDownloadId(id: Long): DatabaseBookWithExtras?

    suspend fun getBookWithExtrasById(id: Long): DatabaseBookWithExtras?

    suspend fun updateBookWithExtras(bookWithExtras: DatabaseBookWithExtras)

}