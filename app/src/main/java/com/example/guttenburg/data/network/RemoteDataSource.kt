package com.example.guttenburg.data.network

interface RemoteDataSource {

    suspend fun getBooks(page: Int, category: String, searchText: String): NetworkBooksPage
    suspend fun getBook(id: Long): NetworkBook
    suspend fun getBookExtras(title: String, author: String): NetworkGoogleBookPage

}