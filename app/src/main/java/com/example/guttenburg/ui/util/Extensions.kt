package com.example.guttenburg.ui.util

import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.guttenburg.data.repository.Book


fun LazyPagingItems<Book>.isRefreshSuccess() = loadState.source.refresh is LoadState.NotLoading
        || loadState.mediator?.refresh is LoadState.NotLoading

fun LazyPagingItems<Book>.isRefreshEmpty() =
    loadState.refresh is LoadState.NotLoading && itemCount == 0

fun LazyPagingItems<Book>.isRefreshLoading() =
    loadState.mediator?.refresh == LoadState.Loading && itemCount == 0

fun LazyPagingItems<Book>.isRefreshError() = loadState.mediator?.refresh is LoadState.Error
        && itemCount == 0

fun LazyPagingItems<Book>.isAppendLoading() = (loadState.mediator?.append is LoadState.Loading)
        || (loadState.mediator?.refresh == LoadState.Loading && itemCount > 0)

fun LazyPagingItems<Book>.isAppendError() = (loadState.mediator?.append is LoadState.Error)
        || (loadState.mediator?.refresh is LoadState.Error && itemCount > 0)
