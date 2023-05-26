package com.example.guttenburg.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [DatabaseBook::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class GuttenburgDatabase : RoomDatabase() {

    abstract fun booksDao(): BookDao
    abstract fun remoteKeysDao(): RemoteKeysDao

}