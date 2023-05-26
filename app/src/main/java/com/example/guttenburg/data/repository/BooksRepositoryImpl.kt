package com.example.guttenburg.data.repository

import androidx.paging.*
import com.example.guttenburg.data.SearchBooksRemoteMediator
import com.example.guttenburg.data.GetAllBooksRemoteMediator
import com.example.guttenburg.data.database.DatabaseBook
import com.example.guttenburg.data.database.LocalDataSource
import com.example.guttenburg.data.database.asExternalModel
import com.example.guttenburg.data.network.RemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BooksRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val dispatcherIO: CoroutineDispatcher
) :
    BooksRepository {

    companion object {
        private const val NETWORK_BOOKS_PAGE_SIZE = 10
    }


    @OptIn(ExperimentalPagingApi::class)
    override fun getBooks(): Flow<PagingData<Book>> {
        val pagingSourceFactory = {localDataSource.getAllBooks()}


        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_BOOKS_PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = GetAllBooksRemoteMediator(remoteDataSource, localDataSource),
            pagingSourceFactory = pagingSourceFactory
        ).flow
            .map { pagingData -> pagingData.map(DatabaseBook::asExternalModel) }
            .flowOn(dispatcherIO)
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun searchBooks(category: String, searchText: String): Flow<PagingData<Book>> {
        val pagingSourceFactory = {localDataSource.booksByNameOrAuthorAndCategory(searchText, category)}


        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_BOOKS_PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = SearchBooksRemoteMediator(searchText, category, remoteDataSource, localDataSource),
            pagingSourceFactory = pagingSourceFactory
        ).flow
            .map { pagingData -> pagingData.map(DatabaseBook::asExternalModel) }
            .flowOn(dispatcherIO)
    }


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