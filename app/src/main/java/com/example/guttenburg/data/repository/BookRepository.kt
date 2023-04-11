package com.example.guttenburg.data.repository

interface BookRepository {
    fun getBooks(): List<Book>

    fun getBook(id: Long): Book

}