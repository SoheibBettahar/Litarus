package com.example.guttenburg.data.database

import androidx.paging.PagingSource
import androidx.room.withTransaction
import com.example.guttenburg.R

class LocalDataSourceImpl(private val database: GuttenburgDatabase) :
    LocalDataSource {

    override fun booksByNameOrAuthorAndCategory(
        searchText: String,
        category: String
    ): PagingSource<Int, DatabaseBook> {
        val searchTextString = "%${searchText.replace(' ', '%')}%"
        val categoryString = "%${category.replace(' ', '%')}%"
        return database.booksDao().booksByNameOrAuthorAndCategory(
            category = categoryString,
            searchText = searchTextString
        )
    }

    override suspend fun insertAllBooks(books: List<DatabaseBook>) {
        database.booksDao().insertAll(books)
    }

    override suspend fun insertBook(book: DatabaseBook) {
        database.booksDao().insert(book)
    }

    override suspend fun deleteBook(book: DatabaseBook) {
        database.booksDao().delete(book)
    }

    override suspend fun clearAllBooks() {
        database.booksDao().clear()
    }

    override fun getAllBooks(): PagingSource<Int, DatabaseBook> {
        return database.booksDao().getAll()
    }

    override suspend fun insertAllRemoteKeys(remoteKeys: List<RemoteKeys>) {
        database.remoteKeysDao().insertAll(remoteKeys)
    }

    override suspend fun remoteKeysByBookId(bookId: Long): RemoteKeys? {
        return database.remoteKeysDao().remoteKeysByBookId(bookId)
    }

    override suspend fun clearAllRemoteKeys() {
        database.remoteKeysDao().clear()
    }

    override suspend fun <R> withTransaction(block: suspend () -> R): R = database.withTransaction (block)
    override suspend fun booksSize(): Int {
        return database.booksDao().size()
    }

    override suspend fun remoteKeysSize(): Int {
        return database.remoteKeysDao().size()
    }

}

