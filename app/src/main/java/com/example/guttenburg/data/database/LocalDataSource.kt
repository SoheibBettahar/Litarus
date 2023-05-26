package com.example.guttenburg.data.database

import androidx.paging.PagingSource

interface LocalDataSource {


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

}