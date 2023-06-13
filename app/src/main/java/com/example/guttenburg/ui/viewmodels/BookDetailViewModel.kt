package com.example.guttenburg.ui.viewmodels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.guttenburg.data.Result
import com.example.guttenburg.data.repository.model.BookWithExtras
import com.example.guttenburg.data.repository.BooksRepository
import com.example.guttenburg.download.DownloadStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


private const val TAG = "BookDetailViewModel"

@HiltViewModel
class BookDetailViewModel @Inject constructor(
    private val booksRepository: BooksRepository,
    savedStateHandle: SavedStateHandle
) :
    ViewModel() {

    private val id: Long = savedStateHandle["id"] ?: -1
    private val title: String = savedStateHandle["title"] ?: ""
    private val author: String = savedStateHandle["author"] ?: ""


    private val _book: MutableStateFlow<Result<BookWithExtras>> = MutableStateFlow(Result.Loading)
    val book: StateFlow<Result<BookWithExtras>>
        get() = _book


    private val downloadId = book
        .filter { it is Result.Success<BookWithExtras> }
        .map { (it as Result.Success).data.downloadId }
        .distinctUntilChanged()

    val downloadProgress = downloadId.filterNotNull()
        .flatMapLatest { booksRepository.getDownloadProgress(it) }
        .distinctUntilChanged()

    val downloadStatus = downloadId.filterNotNull()
        .flatMapLatest { booksRepository.getDownloadStatus(it) }
        .onEach { Log.d(TAG, "downloadStatus: $it") }
        .onEach { if (it is DownloadStatus.Paused) Log.d(TAG, "Paused Reason: ${it.reason}") }
        .distinctUntilChanged()


    init {
        getBook()
    }

    fun getBook() {

        viewModelScope.launch {
            try {
                if (!booksRepository.containsBookWithExtras(id)) {
                    _book.value = Result.Loading
                    booksRepository.fetchBookWithExtras(id, title, author)
                }
                booksRepository.getBookWithExtras(id)
                    .map { Result.Success(it) }
                    .onEach { _book.value = it }
                    .collect()
            } catch (exception: Exception) {
                _book.value = Result.Error(exception)
            }
        }

    }

    fun downloadBook(book: BookWithExtras) {
        Log.d(TAG, "downloadBook: $book")
        viewModelScope.launch {
            booksRepository.downloadBook(book)
        }
    }

    fun cancelDownload(book: BookWithExtras) {
        viewModelScope.launch {
            book.downloadId?.let {
                booksRepository.cancelDownload(downloadId = it)
            }

        }
    }
}