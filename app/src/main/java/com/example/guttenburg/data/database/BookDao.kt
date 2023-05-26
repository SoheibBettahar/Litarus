package com.example.guttenburg.data.database

import androidx.paging.PagingSource
import androidx.room.*

@Dao
interface BookDao {

    @Query("SELECT * FROM book ORDER BY page")
    fun getAll(): PagingSource<Int, DatabaseBook>

    @Query(
        "SELECT * FROM book WHERE (title LIKE :searchText OR authors LIKE :searchText) " +
                "AND subjects LIKE :category ORDER BY page"
    )
    fun booksByNameOrAuthorAndCategory(
        searchText: String,
        category: String
    ): PagingSource<Int, DatabaseBook>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(book: DatabaseBook)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(book: List<DatabaseBook>)

    @Delete
    suspend fun delete(book: DatabaseBook)

    @Query("DELETE FROM book")
    suspend fun clear()

    @Query("SELECT COUNT(id) FROM book")
    suspend fun size(): Int

}