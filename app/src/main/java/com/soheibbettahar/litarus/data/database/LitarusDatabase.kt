package com.soheibbettahar.litarus.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.soheibbettahar.litarus.data.database.detail.BookWithExtrasDao
import com.soheibbettahar.litarus.data.database.detail.DatabaseBookWithExtras
import com.soheibbettahar.litarus.data.database.paging.BookDao
import com.soheibbettahar.litarus.data.database.paging.DatabaseBook
import com.soheibbettahar.litarus.data.database.paging.RemoteKeys
import com.soheibbettahar.litarus.data.database.paging.RemoteKeysDao

@Database(
    entities = [DatabaseBook::class, RemoteKeys::class,
        DatabaseBookWithExtras::class],
    version = 1,
    exportSchema = false
)
abstract class LitarusDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao
    abstract fun remoteKeysDao(): RemoteKeysDao
    abstract fun bookWithExtrasDao(): BookWithExtrasDao

}