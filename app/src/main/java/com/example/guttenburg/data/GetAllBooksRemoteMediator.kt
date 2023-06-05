package com.example.guttenburg.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.guttenburg.data.database.paging.DatabaseBook
import com.example.guttenburg.data.database.BooksLocalDataSource
import com.example.guttenburg.data.database.paging.RemoteKeys
import com.example.guttenburg.data.network.RemoteDataSource
import com.example.guttenburg.data.network.guttendexApi.asDatabaseModel
import retrofit2.HttpException
import java.io.IOException


private const val TAG = "TrainingBooksRemoteMediator"

@OptIn(ExperimentalPagingApi::class)
class GetAllBooksRemoteMediator(
    private val bookRemoteDataSource: RemoteDataSource,
    private val booksLocalDataSource: BooksLocalDataSource,
) : RemoteMediator<Int, DatabaseBook>() {

    override suspend fun initialize(): InitializeAction {
        Log.d(TAG, "initialize called()")
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

        Log.d(TAG, "load called(): loadType:$loadType")
        Log.d(TAG, "page:$page")

        return try {
            val response = bookRemoteDataSource.getBooks(page)
            val networkBooks = response.results
            Log.d(TAG, "fetchedPage:(next = ${response.nextPage()}, prev = ${response.previousPage()}, size = ${response.results.size}, firstId = ${response.results.firstOrNull()?.id}, lastId = ${response.results.lastOrNull()?.id})")

            val endOfPaginationReached = networkBooks.isEmpty()
            booksLocalDataSource.withTransaction {

                if (loadType == LoadType.REFRESH) {
                    booksLocalDataSource.clearAllBooks()
                    booksLocalDataSource.clearAllRemoteKeys()
                }

                val prevKey = response.previousPage()
                val nextKey = response.nextPage()
                Log.d(TAG, "prevKey: $prevKey, nextKey: $nextKey")

                val databaseBooks = networkBooks.map{ it.asDatabaseModel(page) }
                val keys = networkBooks.map { (id) -> RemoteKeys(id, prevKey, nextKey) }


                Log.d(TAG, "cached books size: ${booksLocalDataSource.booksSize()}")
                Log.d(TAG, "cached remoteKeys size:  ${booksLocalDataSource.remoteKeysSize()}")
                booksLocalDataSource.insertAllBooks(databaseBooks)
                booksLocalDataSource.insertAllRemoteKeys(keys)
                Log.d(TAG, "cached books size after insertion: ${booksLocalDataSource.booksSize()}")
                Log.d(TAG, "cached remoteKeys size after insertion:  ${booksLocalDataSource.remoteKeysSize()}")
                Log.d(TAG, "ids:  ${booksLocalDataSource.getAllBooks()}")
                Log.d(TAG, "************************************************************ LOADED SUCCESSFULLY *********************************************************************************************************")
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

        //Log.d(TAG, "")
        Log.d(TAG, "calling getRemoteKeysForLastItem:  (lastItemId = ${lastItem?.id}, remoteKey:$remoteKey)")
        //Log.d(TAG, "")

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