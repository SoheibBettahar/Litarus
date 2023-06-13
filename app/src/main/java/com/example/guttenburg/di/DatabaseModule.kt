package com.example.guttenburg.di

import android.content.Context
import androidx.room.Room
import com.example.guttenburg.data.database.GuttenburgDatabase
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
}