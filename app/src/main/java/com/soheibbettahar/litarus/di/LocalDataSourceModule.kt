package com.soheibbettahar.litarus.di

import com.soheibbettahar.litarus.data.database.BooksLocalDataSource
import com.soheibbettahar.litarus.data.database.BooksLocalDataSourceImpl
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