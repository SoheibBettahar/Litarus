package com.example.guttenburg.data

import android.util.Log
import androidx.paging.*
import com.example.guttenburg.data.database.DatabaseBook
import com.example.guttenburg.data.database.LocalDataSource
import com.example.guttenburg.data.database.RemoteKeys
import com.example.guttenburg.data.network.RemoteDataSource
import com.example.guttenburg.data.network.guttendexApi.asDatabaseModel
import retrofit2.HttpException
import java.io.IOException


const val GUTENDEX_STARTING_PAGE_INDEX = 1
private const val TAG = "BooksRemoteMediator"

@OptIn(ExperimentalPagingApi::class)
class SearchBooksRemoteMediator(
    private val searchText: String,
    private val category: String,
    private val bookRemoteDataSource: RemoteDataSource,
    private val booksLocalDataSource: LocalDataSource,
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
                Log.d(TAG, "load: endOfPagination = $endOfPagination")
                val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(
                    endOfPaginationReached = endOfPagination
                )
                nextKey
            }
        }

        Log.d(TAG, "load called(): loadType:$loadType")
        Log.d(TAG, "page:$page")

        return try {
            val response = bookRemoteDataSource.searchBooks(page, category, searchText)
            val networkBooks = response.results
            Log.d(
                TAG,
                "fetchedPage:(next = ${response.nextPage()}, prev = ${response.previousPage()}, size = ${response.results.size}, firstId = ${response.results.firstOrNull()?.id}, lastId = ${response.results.lastOrNull()?.id})"
            )

            val endOfPaginationReached = networkBooks.isEmpty()
            booksLocalDataSource.withTransaction {

                if (loadType == LoadType.REFRESH) {
                    booksLocalDataSource.clearAllBooks()
                    booksLocalDataSource.clearAllRemoteKeys()
                }

                val prevKey = response.previousPage()
                val nextKey = response.nextPage()

                val databaseBooks = networkBooks.map { it.asDatabaseModel(page) }
                val keys = networkBooks.map { (id) -> RemoteKeys(id, prevKey, nextKey) }

                Log.d(TAG, "cached books size: ${booksLocalDataSource.booksSize()}")
                Log.d(TAG, "cached remoteKeys size:  ${booksLocalDataSource.remoteKeysSize()}")
                booksLocalDataSource.insertAllBooks(databaseBooks)
                booksLocalDataSource.insertAllRemoteKeys(keys)
                Log.d(TAG, "cached books size after insertion: ${booksLocalDataSource.booksSize()}")
                Log.d(
                    TAG,
                    "cached remoteKeys size after insertion:  ${booksLocalDataSource.remoteKeysSize()}"
                )
                Log.d(TAG, "ids:  ${booksLocalDataSource.getAllBooks()}")
                Log.d(
                    TAG,
                    "************************************************************ LOADED SUCCESSFULLY *********************************************************************************************************"
                )

                MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
            }

        } catch (exception: IOException) {
            Log.d(TAG, "load: $exception")
            MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            Log.d(TAG, "load: $exception")
            MediatorResult.Error(exception)
        }


    }


    private suspend fun getRemoteKeysForLastItem(state: PagingState<Int, DatabaseBook>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { (id) -> booksLocalDataSource.remoteKeysByBookId(id) }
    }

    private suspend fun getRemoteKeysForFirstItem(state: PagingState<Int, DatabaseBook>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { (id) -> booksLocalDataSource.remoteKeysByBookId(id) }
    }

    private suspend fun getRemoteKeysClosestToCurrentPosition(state: PagingState<Int, DatabaseBook>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { bookId ->
                booksLocalDataSource.remoteKeysByBookId(bookId)
            }
        }
    }

}