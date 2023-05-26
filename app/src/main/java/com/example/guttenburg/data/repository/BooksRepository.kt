package com.example.guttenburg.data.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow


interface BooksRepository {
    fun searchBooks(category: String, searchText: String): Flow<PagingData<Book>>

    fun getBooks(): Flow<PagingData<Book>>

    fun getBook(id: Long, title: String, author: String): Flow<BookWithExtras>
}