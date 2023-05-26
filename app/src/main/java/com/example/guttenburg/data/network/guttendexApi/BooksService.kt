package com.example.guttenburg.data.network.guttendexApi

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import retrofit2.http.QueryName


interface BooksService {

    @GET("books")
    suspend fun searchBooks(@QueryMap queryMap: Map<String, String>): NetworkBooksPage

    @GET("books")
    suspend fun getBooks(@Query("page") page: Int): NetworkBooksPage

    @GET("books/{id}")
    suspend fun getBook(@Path("id") id: Long): NetworkBook
}


