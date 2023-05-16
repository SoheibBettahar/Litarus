package com.example.guttenburg.data.network

import retrofit2.http.GET
import retrofit2.http.QueryMap

interface GoogleBooksService {

    @GET("volumes")
    suspend fun getBookExtras(@QueryMap queryMap: Map<String, String>): NetworkGoogleBookPage


}