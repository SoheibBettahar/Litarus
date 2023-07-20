package com.soheibbettahar.litarus.data.database

import androidx.paging.PagingSource
import androidx.room.withTransaction
import com.google.android.gms.common.util.VisibleForTesting
import com.soheibbettahar.litarus.data.database.detail.DatabaseBookWithExtras
import com.soheibbettahar.litarus.data.database.paging.DatabaseBook
import com.soheibbettahar.litarus.data.database.paging.RemoteKeys
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

private const val TAG = "BooksLocalDataSourceImp";

class BooksLocalDataSourceImpl @Inject constructor(private val database: LitarusDatabase) :
    BooksLocalDataSource {

    override fun booksByNameOrAuthorAndCategoryAndLanguages(
        searchText: String, category: String, languages: String
    ): PagingSource<Int, DatabaseBook> = when {
        searchText.isNotBlank() && category.isNotBlank() && languages.isNotBlank() -> database.bookDao()
            .getBySearchAndCategoryAndLanguages(
                searchText.asQueryParam(), category.asQueryParam(), languages.asQueryParam()
            )
        searchText.isNotBlank() && category.isNotBlank() -> database.bookDao()
            .getBySearchAndCategory(searchText.asQueryParam(), category.asQueryParam())
        searchText.isNotBlank() && languages.isNotBlank() -> database.bookDao()
            .getBySearchAndLanguages(searchText.asQueryParam(), languages.asQueryParam())
        category.isNotBlank() && languages.isNotBlank() -> database.bookDao()
            .getByCategoryAndLanguages(category.asQueryParam(), languages.asQueryParam())
        searchText.isNotBlank() -> database.bookDao().getBySearch(searchText.asQueryParam())
        category.isNotBlank() -> database.bookDao().getByCategory(category.asQueryParam())
        languages.isNotBlank() -> database.bookDao().getByLanguages(languages.asQueryParam())
        else -> getAllBooks()
    }


    @VisibleForTesting
    private fun String.asQueryParam(): String = "%${this.replace(' ', '%')}%"


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

