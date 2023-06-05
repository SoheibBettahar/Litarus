package com.example.guttenburg.data.database.paging

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.guttenburg.data.repository.Book

private const val TAG = "DatabaseBook"
@Entity(tableName = "book")
data class DatabaseBook(
    @PrimaryKey val id: Long,
    val title: String,
    val authors: String,
    val imageUrl: String,
    val subjects: String,
    val page: Int,
)


fun DatabaseBook.asExternalModel(): Book {
    return Book(
        id = id,
        title = title,
        authors = authors.split('#'),
        imageUrl = imageUrl,
    )
}