package com.example.guttenburg.di

import com.example.guttenburg.data.repository.BooksRepository
import com.example.guttenburg.data.repository.BooksRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindBookRepository(repository: BooksRepositoryImpl): BooksRepository

}