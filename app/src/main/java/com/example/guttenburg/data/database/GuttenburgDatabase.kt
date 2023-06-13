package com.example.guttenburg.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.guttenburg.data.database.detail.BookWithExtrasDao
import com.example.guttenburg.data.database.detail.DatabaseBookWithExtras
import com.example.guttenburg.data.database.paging.BookDao
import com.example.guttenburg.data.database.paging.DatabaseBook
import com.example.guttenburg.data.database.paging.RemoteKeys
import com.example.guttenburg.data.database.paging.RemoteKeysDao

@Database(
    entities = [DatabaseBook::class, RemoteKeys::class,
        DatabaseBookWithExtras::class],
    version = 1,
    exportSchema = false
)
abstract class GuttenburgDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao
    abstract fun remoteKeysDao(): RemoteKeysDao
    abstract fun bookWithExtrasDao(): BookWithExtrasDao

}