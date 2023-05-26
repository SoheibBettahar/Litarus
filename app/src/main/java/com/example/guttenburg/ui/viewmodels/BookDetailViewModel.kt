package com.example.guttenburg.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.guttenburg.data.Result
import com.example.guttenburg.data.Result.Loading.asResult
import com.example.guttenburg.data.repository.BookWithExtras
import com.example.guttenburg.data.repository.BooksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

//saved state handle
@HiltViewModel
class BookDetailViewModel @Inject constructor(private val booksRepository: BooksRepository, private val savedStateHandle: SavedStateHandle) :
    ViewModel() {

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