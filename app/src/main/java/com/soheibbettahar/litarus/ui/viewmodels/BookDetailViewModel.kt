package com.soheibbettahar.litarus.ui.viewmodels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soheibbettahar.litarus.data.Result
import com.soheibbettahar.litarus.data.repository.model.BookWithExtras
import com.soheibbettahar.litarus.data.repository.BooksRepository
import com.soheibbettahar.litarus.download.DownloadStatus
import com.soheibbettahar.litarus.util.connectivity.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


private const val TAG = "BookDetailViewModel"

data class BookDetailUiState(
    val loadResult: Result<BookWithExtras> = Result.Loading,
)

data class DownloadState(
    val status: DownloadStatus = DownloadStatus.NotDownloading,
    val progress: Float = 0f
)


@HiltViewModel
class BookDetailViewModel @Inject constructor(
    private val booksRepository: BooksRepository,
    networkMonitor: NetworkMonitor,
    savedStateHandle: SavedStateHandle
) :
    ViewModel() {

    val id: Long = savedStateHandle["id"] ?: -1
    val title: String = savedStateHandle["title"] ?: ""
    val author: String = savedStateHandle["author"] ?: ""

    private val _bookUiState = MutableStateFlow(BookDetailUiState())
    val bookUiState: StateFlow<BookDetailUiState>
        get() = _bookUiState


    val downloadState: StateFlow<DownloadState> = downloadUiState().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DownloadState()
    )

    val isOnline = networkMonitor.isOnline.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false
    )


    init {
        getBook()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun downloadUiState(): Flow<DownloadState> {
        val downloadId = bookUiState.map { it.loadResult }
            .filter { it is Result.Success<BookWithExtras> }
            .map { (it as Result.Success).data.downloadId }
            .distinctUntilChanged()

        val downloadProgress = downloadId.filterNotNull()
            .flatMapLatest { booksRepository.getDownloadProgress(it) }
            .distinctUntilChanged()

        val downloadStatus = downloadId.filterNotNull()
            .flatMapLatest { booksRepository.getDownloadStatus(it) }
            .distinctUntilChanged()


        return downloadStatus.combine(downloadProgress) { status, progress ->
            DownloadState(status, progress)
        }
    }

    fun getBook() {

        viewModelScope.launch {
            try {
                if (!booksRepository.containsBookWithExtras(id)) {
                    _bookUiState.value = BookDetailUiState(Result.Loading)
                    booksRepository.fetchBookWithExtras(id, title, author)
                }

                booksRepository.getBookWithExtras(id)
                    .map { Result.Success(it) }
                    .onEach {
                        _bookUiState.value = BookDetailUiState(it)
                    }
                    .collect()

            } catch (exception: Exception) {
                _bookUiState.value = BookDetailUiState(Result.Error(exception))
            }
        }

    }

    fun downloadBook(book: BookWithExtras) {
        viewModelScope.launch {
            booksRepository.downloadBook(book)
        }
    }

    fun cancelDownload(book: BookWithExtras) {
        viewModelScope.launch {
            book.downloadId?.let {
                booksRepository.cancelDownload(book)
            }

        }
    }

}