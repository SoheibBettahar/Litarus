package com.soheibbettahar.litarus.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.soheibbettahar.litarus.data.database.paging.DatabaseBook
import com.soheibbettahar.litarus.data.database.BooksLocalDataSource
import com.soheibbettahar.litarus.data.database.paging.RemoteKeys
import com.soheibbettahar.litarus.data.network.RemoteDataSource
import com.soheibbettahar.litarus.data.network.guttendexApi.asDatabaseModel
import retrofit2.HttpException
import java.io.IOException


private const val TAG = "TrainingBooksRemoteMediator"

@OptIn(ExperimentalPagingApi::class)
class GetAllBooksRemoteMediator(
    private val bookRemoteDataSource: RemoteDataSource,
    private val booksLocalDataSource: BooksLocalDataSource,
) : RemoteMediator<Int, DatabaseBook>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, DatabaseBook>
    ): MediatorResult {

        val page: Int = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeysClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: GUTENDEX_STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeysForFirstItem(state)
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )
                prevKey
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeysForLastItem(state)
                val endOfPagination = remoteKeys != null && remoteKeys.nextKey == null
                val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(
                    endOfPaginationReached = endOfPagination
                )
                nextKey
            }
        }

        return try {
            val response = bookRemoteDataSource.getBooks(page)
            val networkBooks = response.results
            val endOfPaginationReached = networkBooks.isEmpty()
            booksLocalDataSource.withTransaction {

                if (loadType == LoadType.REFRESH) {
                    booksLocalDataSource.clearAllBooks()
                    booksLocalDataSource.clearAllRemoteKeys()
                }

                val prevKey = response.previousPage()
                val nextKey = response.nextPage()

                val databaseBooks = networkBooks.map{ it.asDatabaseModel(page) }
                val keys = networkBooks.map { (id) -> RemoteKeys(id, prevKey, nextKey) }


                booksLocalDataSource.insertAllBooks(databaseBooks)
                booksLocalDataSource.insertAllRemoteKeys(keys)
                MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
            }

        } catch (exception: IOException) {
            MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            MediatorResult.Error(exception)
        }


    }


    private suspend fun getRemoteKeysForLastItem(state: PagingState<Int, DatabaseBook>): RemoteKeys? {
        val lastNotEmptyPage = state.pages.lastOrNull { it.data.isNotEmpty() }
        val lastItem = lastNotEmptyPage?.data?.lastOrNull()
        val remoteKey =  lastItem?.let { (id) -> booksLocalDataSource.remoteKeysByBookId(id) }


        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { (id) -> booksLocalDataSource.remoteKeysByBookId(id) }
    }

    private suspend fun getRemoteKeysForFirstItem(state: PagingState<Int, DatabaseBook>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data
            ?.firstOrNull()?.let { (id) -> booksLocalDataSource.remoteKeysByBookId(id) }
    }

    private suspend fun getRemoteKeysClosestToCurrentPosition(state: PagingState<Int, DatabaseBook>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { bookId ->
                booksLocalDataSource.remoteKeysByBookId(bookId)
            }
        }
    }

}