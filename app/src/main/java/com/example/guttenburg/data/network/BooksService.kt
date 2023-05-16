package com.example.guttenburg.data.network

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap


interface BooksService {

    @GET("books")
    suspend fun getBooks(@QueryMap queryMap: Map<String, String>): NetworkBooksPage

    @GET("books")
    suspend fun getBooksAsText(): ResponseBody

    @GET("books/{id}")
    suspend fun getBook(@Path("id") id: Long): NetworkBook
}


