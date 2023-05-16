package com.example.guttenburg.data.network

import com.example.guttenburg.BuildConfig

class RemoteDataSourceImpl(
    private val booksService: BooksService,
    private val googleBooksService: GoogleBooksService
) : RemoteDataSource {

    override suspend fun getBooks(
        page: Int,
        category: String,
        searchText: String
    ): NetworkBooksPage {
        val queryMap = mutableMapOf(
            "page" to "$page",
            "topic" to category,
            "search" to searchText,
        )

        return booksService.getBooks(queryMap)
    }

    override suspend fun getBook(id: Long): NetworkBook = booksService.getBook(id)


    override suspend fun getBookExtras(title: String, author: String): NetworkGoogleBookPage {
        val queryMap = mutableMapOf(
            "q" to "$title+inauthor:$author",
            "startIndex" to "0",
            "maxResults" to "1",
            "key" to BuildConfig.GOOGLE_BOOKS_API_KEY
        )

        return googleBooksService.getBookExtras(queryMap)
    }
}