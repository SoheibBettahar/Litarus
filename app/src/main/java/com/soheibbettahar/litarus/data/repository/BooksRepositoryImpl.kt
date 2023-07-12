package com.soheibbettahar.litarus.data.repository

import android.util.Log
import androidx.paging.*
import com.soheibbettahar.litarus.data.GetAllBooksRemoteMediator
import com.soheibbettahar.litarus.data.SearchBooksRemoteMediator
import com.soheibbettahar.litarus.data.database.BooksLocalDataSource
import com.soheibbettahar.litarus.data.database.detail.DatabaseBookWithExtras
import com.soheibbettahar.litarus.data.database.detail.asExternalModel
import com.soheibbettahar.litarus.data.database.paging.DatabaseBook
import com.soheibbettahar.litarus.data.database.paging.asExternalModel
import com.soheibbettahar.litarus.data.network.RemoteDataSource
import com.soheibbettahar.litarus.data.repository.model.Book
import com.soheibbettahar.litarus.data.repository.model.BookWithExtras
import com.soheibbettahar.litarus.download.DownloadStatus
import com.soheibbettahar.litarus.download.Downloader
import com.soheibbettahar.litarus.util.analytics.AnalyticsHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "BooksRepositoryImpl"

@Singleton
class BooksRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val booksLocalDataSource: BooksLocalDataSource,
    private val downloader: Downloader,
    private val analyticsHelper: AnalyticsHelper,
    private val dispatcherIO: CoroutineDispatcher
) : BooksRepository {

    companion object {
        private const val NETWORK_BOOKS_PAGE_SIZE = 30
    }


    @OptIn(ExperimentalPagingApi::class)
    override fun searchBooks(
        searchText: String,
        category: String,
        languages: List<String>
    ): Flow<PagingData<Book>> {
        val formattedLanguages = languages.joinToString(separator = ",")

        analyticsHelper.logSearchBook(
            term = searchText,
            category = category,
            languages = formattedLanguages
        )

        val pagingSourceFactory =
            {
                booksLocalDataSource.booksByNameOrAuthorAndCategoryAndLanguages(
                    searchText,
                    category,
                    formattedLanguages
                )
            }

        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_BOOKS_PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = SearchBooksRemoteMediator(
                searchText,
                category,
                formattedLanguages,
                remoteDataSource,
                booksLocalDataSource
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
            .map { pagingData -> pagingData.map(DatabaseBook::asExternalModel) }
            .flowOn(dispatcherIO)
    }

    override suspend fun fetchBookWithExtras(id: Long, title: String, author: String) =
        withContext(dispatcherIO) {
            coroutineScope {
                val book = async { remoteDataSource.getBook(id) }
                val bookExtras = async {
                    remoteDataSource.getBookExtras(
                        title,
                        author
                    ).items.firstOrNull()?.bookInfo
                }

                val bookPlaceHolder = book.await()
                val bookWithExtrasPlaceHolder = bookExtras.await()

                val databaseBookWithExtras =
                    DatabaseBookWithExtras.from(bookPlaceHolder, bookWithExtrasPlaceHolder)
                booksLocalDataSource.insertBookWithExtras(databaseBookWithExtras)
            }
        }


    override fun getBookWithExtras(id: Long) = booksLocalDataSource.getBookWithExtras(id)

        .map(DatabaseBookWithExtras::asExternalModel)
        .flowOn(dispatcherIO)


    override suspend fun containsBookWithExtras(id: Long): Boolean {
        return withContext(dispatcherIO) { booksLocalDataSource.containsBookWithExtras(id) }
    }


    override suspend fun downloadBook(book: BookWithExtras) {
        withContext(dispatcherIO) {
            analyticsHelper.logDownloadBook(book = book)

            book.downloadUrl?.let { downloadUrl ->
                val downloadId = downloader.downloadFile(
                    downloadUrl,
                    book.title,
                    book.authors,
                    book.fileExtension
                )

                booksLocalDataSource.getBookWithExtrasById(book.id)?.copy(downloadId = downloadId)
                    ?.let { booksLocalDataSource.updateBookWithExtras(bookWithExtras = it) }

            }
        }
    }

    override suspend fun addFileUriToDownloadedBook(downloadId: Long) {
        withContext(dispatcherIO) {
            val bookWithExtras =
                booksLocalDataSource.getBookWithExtrasByDownloadId(downloadId) ?: return@withContext
            val fileUriString = downloader.getFileUriString(downloadId)

            booksLocalDataSource.updateBookWithExtras(
                bookWithExtras.copy(
                    downloadId = null,
                    fileUriString = fileUriString
                )
            )
        }
    }

    override suspend fun removeFileUriFromBook(book: BookWithExtras) {
        withContext(dispatcherIO) {
            withContext(dispatcherIO) {
                val databaseBook = booksLocalDataSource.getBookWithExtrasById(book.id)!!
                booksLocalDataSource.updateBookWithExtras(databaseBook.copy(fileUriString = null))
            }
        }
    }

    override fun cancelDownload(book: BookWithExtras) {
        analyticsHelper.logCancelDownloadBook(book = book)

        downloader.cancelDownload(downloadId = book.downloadId!!)
    }

    override fun getDownloadProgress(downloadId: Long): Flow<Float> =
        downloader.getDownloadProgress(downloadId = downloadId).flowOn(dispatcherIO)

    override fun getDownloadStatus(downloadId: Long): Flow<DownloadStatus> {
        return downloader.getDownloadStatus(downloadId = downloadId).flowOn(dispatcherIO)
    }

}