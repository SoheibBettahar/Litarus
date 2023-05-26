package com.example.guttenburg.data.network

import com.example.guttenburg.data.network.googleBooksApi.NetworkGoogleBookPage
import com.example.guttenburg.data.network.guttendexApi.NetworkBook
import com.example.guttenburg.data.network.guttendexApi.NetworkBooksPage

interface RemoteDataSource {

    suspend fun searchBooks(page: Int, category: String, searchText: String): NetworkBooksPage
    suspend fun getBooks(page: Int): NetworkBooksPage
    suspend fun getBook(id: Long): NetworkBook
    suspend fun getBookExtras(title: String, author: String): NetworkGoogleBookPage

}