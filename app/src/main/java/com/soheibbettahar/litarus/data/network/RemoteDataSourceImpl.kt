package com.soheibbettahar.litarus.data.network

import com.soheibbettahar.litarus.BuildConfig
import com.soheibbettahar.litarus.data.network.googleBooksApi.GoogleBooksService
import com.soheibbettahar.litarus.data.network.googleBooksApi.NetworkGoogleBookPage
import com.soheibbettahar.litarus.data.network.guttendexApi.BooksService
import com.soheibbettahar.litarus.data.network.guttendexApi.NetworkBook
import com.soheibbettahar.litarus.data.network.guttendexApi.NetworkBooksPage

class RemoteDataSourceImpl(
    private val booksService: BooksService,
    private val googleBooksService: GoogleBooksService
) : RemoteDataSource {

    override suspend fun searchBooks(
        page: Int,
        searchText: String,
        category: String,
        languages: String
    ): NetworkBooksPage {
        val queryMap = mutableMapOf("page" to "$page")

        if (searchText.isNotEmpty()) queryMap["search"] = searchText
        if (category.isNotEmpty()) queryMap["topic"] = category
        if (languages.isNotEmpty()) queryMap["languages"] = languages

        return booksService.searchBooks(queryMap)
    }

    override suspend fun getBooks(page: Int): NetworkBooksPage {
        return booksService.getBooks(page)
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