package com.example.guttenburg.ui

import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.guttenburg.data.repository.Book

fun LazyPagingItems<Book>.isRefreshSuccess() = loadState.refresh is LoadState.NotLoading
fun LazyPagingItems<Book>.isRefreshLoading() = loadState.refresh is LoadState.Loading
fun LazyPagingItems<Book>.isRefreshError() = loadState.refresh is LoadState.Error
fun LazyPagingItems<Book>.isPrependLoading() = loadState.prepend is LoadState.Loading
fun LazyPagingItems<Book>.isPrependError() = loadState.prepend is LoadState.Error
fun LazyPagingItems<Book>.isAppendLoading() = loadState.append is LoadState.Loading
fun LazyPagingItems<Book>.isAppendError() = loadState.append is LoadState.Error
