package com.example.guttenburg.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.guttenburg.data.Result
import com.example.guttenburg.data.Result.Loading.asResult
import com.example.guttenburg.data.repository.Book
import com.example.guttenburg.data.repository.BookWithExtras
import com.example.guttenburg.data.repository.BooksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject


private const val TAG = "BooksViewModel"

@HiltViewModel
class BooksViewModel @Inject constructor(private val booksRepository: BooksRepository) :
    ViewModel() {


    val category = MutableStateFlow(value = "")
    private val searchText = MutableStateFlow(value = "")


    val pagingDataFlow: Flow<PagingData<Book>> =
        combine(category, searchText, ::Pair)
            .flatMapLatest { (category, searchText) -> searchBooks(category, searchText) }
            .distinctUntilChanged()
            .cachedIn(viewModelScope)


    private fun searchBooks(category: String, searchText: String): Flow<PagingData<Book>> =
        booksRepository.getBooks(category, searchText)

    fun selectCategory(selected: String) {
        category.value = selected
    }

    fun setSearchText(text: String) {
        searchText.value = text
    }


    private val _book: MutableStateFlow<Result<BookWithExtras>> = MutableStateFlow(Result.Loading)
    val book: StateFlow<Result<BookWithExtras>>
        get() = _book

    fun getBook(id: Long, title: String, author: String) {
        booksRepository.getBook(id, title, author)
            .asResult()
            .onEach { _book.value = it }
            .launchIn(viewModelScope)
    }


}