package com.example.guttenburg.data.repository

import com.example.guttenburg.data.network.BookService

class BookRepositoryImpl(private val bookService: BookService) : BookRepository {

    override fun getBooks(): List<Book> {
        TODO("Not yet implemented")
    }

    override fun getBook(id: Long): Book {
        TODO("Not yet implemented")
    }

}