package com.example.guttenburg.di

import android.content.Context
import androidx.room.Room
import com.example.guttenburg.data.database.GuttenburgDatabase
import com.example.guttenburg.data.database.LocalDataSource
import com.example.guttenburg.data.database.LocalDataSourceImpl
import com.example.guttenburg.data.network.RemoteDataSource
import com.example.guttenburg.data.network.RemoteDataSourceImpl
import com.example.guttenburg.data.network.googleBooksApi.GoogleBooksService
import com.example.guttenburg.data.network.guttendexApi.BooksService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {


    @Singleton
    @Provides
    fun provideGuttenburgDatabase(@ApplicationContext context: Context): GuttenburgDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            GuttenburgDatabase::class.java, "Guttenburg.db"
        ).build()

    }

    @Provides
    fun provideLocalDataSource(
        guttenburgDatabase: GuttenburgDatabase
    ): LocalDataSource {
        return LocalDataSourceImpl(guttenburgDatabase)
    }

}