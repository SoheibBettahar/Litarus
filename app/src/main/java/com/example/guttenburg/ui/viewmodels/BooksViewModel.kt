package com.example.guttenburg.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.guttenburg.data.repository.model.Book
import com.example.guttenburg.data.repository.BooksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class BooksViewModel @Inject constructor(private val booksRepository: BooksRepository) :
    ViewModel() {


    val category = MutableStateFlow(value = "")
    private val searchText = MutableStateFlow(value = "")


    val searchedBooksPagingDataFlow: Flow<PagingData<Book>> =
        combine(category, searchText, ::Pair)
            .flatMapLatest { (category, searchText) -> searchBooks(category, searchText) }
            .distinctUntilChanged()
            .cachedIn(viewModelScope)

    fun getAllBooks(): Flow<PagingData<Book>> {
        return booksRepository.getBooks()
    }

    private fun searchBooks(category: String, searchText: String): Flow<PagingData<Book>> =
        booksRepository.searchBooks(category, searchText)

    fun selectCategory(selected: String) {
        category.value = selected
    }

    fun setSearchText(text: String) {
        searchText.value = text
    }

}