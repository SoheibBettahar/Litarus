package com.soheibbettahar.litarus.data.network

import com.soheibbettahar.litarus.data.network.googleBooksApi.NetworkGoogleBookPage
import com.soheibbettahar.litarus.data.network.guttendexApi.NetworkBook
import com.soheibbettahar.litarus.data.network.guttendexApi.NetworkBooksPage

interface RemoteDataSource {

    suspend fun searchBooks(page: Int, searchText: String, category: String, languages: String): NetworkBooksPage
    suspend fun getBooks(page: Int): NetworkBooksPage
    suspend fun getBook(id: Long): NetworkBook
    suspend fun getBookExtras(title: String, author: String): NetworkGoogleBookPage

}