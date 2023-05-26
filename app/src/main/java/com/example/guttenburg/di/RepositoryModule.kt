package com.example.guttenburg.di

import com.example.guttenburg.data.database.GuttenburgDatabase
import com.example.guttenburg.data.database.LocalDataSource
import com.example.guttenburg.data.network.RemoteDataSource
import com.example.guttenburg.data.repository.BooksRepository
import com.example.guttenburg.data.repository.BooksRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {

    @Provides
    fun provideIODispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Singleton
    @Provides
    fun bindBookRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource,
        ioDispatcher: CoroutineDispatcher
    ): BooksRepository {
        return BooksRepositoryImpl(remoteDataSource, localDataSource, ioDispatcher)
    }

}