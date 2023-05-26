package com.example.guttenburg.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey
    val bookId: Long,
    val prevKey: Int?,
    val nextKey: Int?
)