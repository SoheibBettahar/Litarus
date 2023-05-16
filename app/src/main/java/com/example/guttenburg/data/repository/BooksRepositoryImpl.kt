package com.example.guttenburg.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.guttenburg.data.BooksPagingSource
import com.example.guttenburg.data.network.NetworkBook
import com.example.guttenburg.data.network.RemoteDataSource
import com.example.guttenburg.data.network.asExternalModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BooksRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val dispatcherIO: CoroutineDispatcher
) :
    BooksRepository {

    companion object {
        private const val NETWORK_BOOKS_PAGE_SIZE = 4
    }


    override fun getBooks(category: String, searchText: String): Flow<PagingData<Book>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_BOOKS_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { BooksPagingSource(remoteDataSource, category, searchText) }
        ).flow.map { pagingData -> pagingData.map(NetworkBook::asExternalModel) }
            .flowOn(dispatcherIO)

    }


    /*
    flow {
    val books =
        remoteDataSource.getBooks(page, category).results.map(NetworkBook::asExternalModel)
    emit(books)
}.flowOn(dispatcherIO)
     */


    override fun getBook(id: Long, title: String, author: String): Flow<BookWithExtras> = flow {
        val bookWithExtras = coroutineScope {
            val book = async { remoteDataSource.getBook(id) }
            val bookExtras = async {
                remoteDataSource.getBookExtras(
                    title,
                    author
                ).items.firstOrNull()?.bookInfo
            }

            BookWithExtras.from(book.await(), bookExtras.await())
        }
        emit(bookWithExtras)
    }.flowOn(dispatcherIO)

}