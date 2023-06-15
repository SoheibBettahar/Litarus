package com.example.guttenburg.data.database.paging

import androidx.paging.PagingSource
import androidx.room.*

@Dao
interface BookDao {
    @Query("SELECT * FROM book WHERE (title LIKE :searchText OR authors LIKE :searchText) " +
            "AND subjects LIKE :category AND languages LIKE :languages ORDER BY page")
    fun getBySearchAndCategoryAndLanguages(searchText: String, category: String, languages: String): PagingSource<Int, DatabaseBook>

    @Query("SELECT * FROM book WHERE (title LIKE :searchText OR authors LIKE :searchText) " +
            "AND subjects LIKE :category ORDER BY page")
    fun getBySearchAndCategory(searchText: String, category: String): PagingSource<Int, DatabaseBook>

    @Query("SELECT * FROM book WHERE (title LIKE :searchText OR authors LIKE :searchText) " +
            "AND languages LIKE :languages ORDER BY page")
    fun getBySearchAndLanguages(searchText: String, languages: String): PagingSource<Int, DatabaseBook>

    @Query("SELECT * FROM book WHERE subjects LIKE :category " +
            "AND languages LIKE :languages ORDER BY page")
    fun getByCategoryAndLanguages(category: String, languages: String): PagingSource<Int, DatabaseBook>

    @Query("SELECT * FROM book WHERE (title LIKE :searchText OR authors LIKE :searchText) " +
            "ORDER BY page")
    fun getBySearch(searchText: String): PagingSource<Int, DatabaseBook>

    @Query("SELECT * FROM book WHERE subjects LIKE :category ORDER BY page" )
    fun getByCategory(category: String): PagingSource<Int, DatabaseBook>

    @Query("SELECT * FROM book WHERE languages LIKE :languages ORDER BY page")
    fun getByLanguages(languages: String): PagingSource<Int, DatabaseBook>

    @Query("SELECT * FROM book ORDER BY page")
    fun getAll(): PagingSource<Int, DatabaseBook>


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