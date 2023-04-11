package com.example.guttenburg.di

import com.example.guttenburg.data.repository.BookRepository
import com.example.guttenburg.data.repository.BookRepositoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun provideBookRepository(bookRepository: BookRepositoryImpl): BookRepository
}