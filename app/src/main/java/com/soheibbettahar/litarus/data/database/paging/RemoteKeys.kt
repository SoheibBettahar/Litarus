package com.soheibbettahar.litarus.data.database.paging

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey
    val bookId: Long,
    val prevKey: Int?,
    val nextKey: Int?
)