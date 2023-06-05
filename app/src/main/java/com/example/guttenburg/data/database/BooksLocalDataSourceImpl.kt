package com.example.guttenburg.data.database

import androidx.paging.PagingSource
import androidx.room.withTransaction
import com.example.guttenburg.data.database.detail.DatabaseBookWithExtras
import com.example.guttenburg.data.database.paging.DatabaseBook
import com.example.guttenburg.data.database.paging.RemoteKeys
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BooksLocalDataSourceImpl @Inject constructor(private val database: GuttenburgDatabase) :
    BooksLocalDataSource {

    override fun booksByNameOrAuthorAndCategory(
        searchText: String,
        category: String
    ): PagingSource<Int, DatabaseBook> {
        val searchTextString = "%${searchText.replace(' ', '%')}%"
        val categoryString = "%${category.replace(' ', '%')}%"
        return database.bookDao().booksByNameOrAuthorAndCategory(
            category = categoryString,
            searchText = searchTextString
        )
    }

    override suspend fun insertAllBooks(books: List<DatabaseBook>) {
        database.bookDao().insertAll(books)
    }

    override suspend fun insertBook(book: DatabaseBook) {
        database.bookDao().insert(book)
    }

    override suspend fun deleteBook(book: DatabaseBook) {
        database.bookDao().delete(book)
    }

    override suspend fun clearAllBooks() {
        database.bookDao().clear()
    }

    override fun getAllBooks(): PagingSource<Int, DatabaseBook> {
        return database.bookDao().getAll()
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

    override suspend fun <R> withTransaction(block: suspend () -> R): R =
        database.withTransaction(block)

    override suspend fun booksSize(): Int {
        return database.bookDao().size()
    }

    override suspend fun remoteKeysSize(): Int {
        return database.remoteKeysDao().size()
    }

    override suspend fun insertDownloadKey(bookId: Long, downloadId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun insertBookWithExtras(bookWithExtras: DatabaseBookWithExtras) {
        database.bookWithExtrasDao().insert(bookWithExtras)
    }

    override suspend fun containsBookWithExtras(id: Long): Boolean {
        return database.bookWithExtrasDao().contains(id)
    }

    override fun getBookWithExtras(id: Long): Flow<DatabaseBookWithExtras> {
        return database.bookWithExtrasDao().get(id)
    }

    override suspend fun getBookWithExtrasByDownloadId(id: Long): DatabaseBookWithExtras? {
        return database.bookWithExtrasDao().getByDownloadId(id)
    }

    override suspend fun getBookWithExtrasById(id: Long): DatabaseBookWithExtras? {
        return database.bookWithExtrasDao().getById(id)
    }

    override suspend fun updateBookWithExtras(bookWithExtras: DatabaseBookWithExtras) {
        database.bookWithExtrasDao().update(bookWithExtras)
    }

}

