package com.example.guttenburg.di

import com.example.guttenburg.data.network.BookService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideBooksRetrofitService(): BookService {
        return Retrofit.Builder()
            .baseUrl("https://gutendex.com")
            .addConverterFactory(MoshiConverterFactory.create())
            .build().create(BookService::class.java)
    }


}