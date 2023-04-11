package com.example.guttenburg.data.network

import retrofit2.http.GET
import retrofit2.http.Path


interface BookService {

    @GET("books")
    suspend fun getBooks(): List<NetworkBook>

    @GET("books/{id}")
    suspend fun getBook(@Path("id") id: Long): NetworkBook
}


