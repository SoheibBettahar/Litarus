package com.soheibbettahar.litarus.data.database.paging

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeys>)

    @Query("SELECT * FROM remote_keys WHERE bookId = :bookId")
    suspend fun remoteKeysByBookId(bookId: Long): RemoteKeys?

    @Query("DELETE FROM remote_keys")
    suspend fun clear()

    @Query("SELECT COUNT(id) FROM book")
    suspend fun size(): Int
}