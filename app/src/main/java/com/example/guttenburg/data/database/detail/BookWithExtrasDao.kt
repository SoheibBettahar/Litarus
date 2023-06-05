package com.example.guttenburg.data.database.detail

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BookWithExtrasDao {

    @Insert
    suspend fun insert(bookWithExtras: DatabaseBookWithExtras)

    @Update
    suspend fun update(bookWithExtras: DatabaseBookWithExtras)

    @Delete
    suspend fun delete(bookWithExtras: DatabaseBookWithExtras)

    @Query("SELECT * FROM book_with_extras WHERE :id = id")
    fun get(id: Long): Flow<DatabaseBookWithExtras>

    @Query("SELECT * FROM book_with_extras WHERE :downloadId = downloadId")
    suspend fun getByDownloadId(downloadId: Long): DatabaseBookWithExtras?

    @Query("SELECT * FROM book_with_extras WHERE :id = id")
    suspend fun getById(id: Long): DatabaseBookWithExtras?

    @Query("SELECT EXISTS(SELECT * FROM book_with_extras WHERE :id = id)")
    suspend fun contains(id: Long): Boolean

}