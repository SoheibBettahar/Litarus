package com.example.guttenburg.di

import com.example.guttenburg.data.database.BooksLocalDataSource
import com.example.guttenburg.data.database.BooksLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class LocalDataSourceModule {

    @Binds
    abstract fun bindBooksLocalDataSource(datasource: BooksLocalDataSourceImpl): BooksLocalDataSource
}