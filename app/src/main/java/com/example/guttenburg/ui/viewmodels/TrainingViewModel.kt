package com.example.guttenburg.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.guttenburg.data.repository.BooksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

private const val TAG = "TrainingViewModel"

@HiltViewModel
class TrainingViewModel @Inject constructor(
    private val booksRepository: BooksRepository, savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val id: Long = 37106
    private val _bookData = MutableStateFlow<String>("")
    val bookData: StateFlow<String>
        get() = _bookData


}