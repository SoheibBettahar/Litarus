package com.soheibbettahar.litarus.di

import android.content.Context
import androidx.room.Room
import com.soheibbettahar.litarus.data.database.LitarusDatabase
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
    fun provideLitarusDatabase(@ApplicationContext context: Context): LitarusDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            LitarusDatabase::class.java, "Litarus.db"
        ).build()

    }
}