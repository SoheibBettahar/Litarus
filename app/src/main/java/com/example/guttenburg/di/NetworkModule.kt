package com.example.guttenburg.di

import com.example.guttenburg.data.network.BooksService
import com.example.guttenburg.data.network.GoogleBooksService
import com.example.guttenburg.data.network.RemoteDataSource
import com.example.guttenburg.data.network.RemoteDataSourceImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {


    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
    }

    @Provides
    fun provideMoshiInstance(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }


    @Singleton
    @Provides
    fun provideGutendexBookService(okHttpClient: OkHttpClient, moshi: Moshi): BooksService {

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://gutendex.com")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(BooksService::class.java)
    }

    @Singleton
    @Provides
    fun provideGoogleBookService(okHttpClient: OkHttpClient, moshi: Moshi): GoogleBooksService {

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://www.googleapis.com/books/v1/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GoogleBooksService::class.java)
    }

    @Provides
    fun provideRemoteDataSource(
        booksService: BooksService,
        googleBooksService: GoogleBooksService
    ): RemoteDataSource {
        return RemoteDataSourceImpl(booksService, googleBooksService)
    }


}