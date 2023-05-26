package com.example.guttenburg.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.guttenburg.data.network.RemoteDataSource
import com.example.guttenburg.data.network.guttendexApi.NetworkBook

private const val TAG = "BooksPagingSource"

/*
class BooksPagingSource(
    private val backend: RemoteDataSource,
    private val category: String,
    private val searchText: String
) :
    PagingSource<Int, NetworkBook>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NetworkBook> {
        return try {
            val nextPageNumber = params.key ?: GUTENDEX_STARTING_PAGE_INDEX
            val response = backend.getBooks(nextPageNumber, category, searchText)

            LoadResult.Page(
                data = response.results,
                prevKey = response.previousPage(), // Only paging forward.
                nextKey = response.nextPage()
            )

        } catch (t: Throwable) {
            Log.d(TAG, "load Error: $t")
            LoadResult.Error(t)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, NetworkBook>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}

 */