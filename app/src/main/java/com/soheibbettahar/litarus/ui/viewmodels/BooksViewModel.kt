package com.soheibbettahar.litarus.ui.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.soheibbettahar.litarus.data.repository.BooksRepository
import com.soheibbettahar.litarus.data.repository.model.Book
import com.soheibbettahar.litarus.ui.components.Language
import com.soheibbettahar.litarus.ui.components.NoLanguageFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class BooksViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val booksRepository: BooksRepository
) :
    ViewModel() {


    @OptIn(SavedStateHandleSaveableApi::class)
    var searchTerm by savedStateHandle.saveable() {
        mutableStateOf("")
    }
        private set

    @OptIn(FlowPreview::class)
    private val searchTermFlow: Flow<String> = snapshotFlow { searchTerm }
        .debounce(200)
        .conflate()

    @OptIn(SavedStateHandleSaveableApi::class)
    var category by savedStateHandle.saveable() {
        mutableStateOf(value = "")
    }
        private set

    private val categoryFlow = snapshotFlow { category }

    @OptIn(SavedStateHandleSaveableApi::class)
    var language by savedStateHandle.saveable {
        mutableStateOf(NoLanguageFilter)
    }
        private set

    private val languageFlow = snapshotFlow { language }
        .onEach { delay(500) }


    @OptIn(ExperimentalCoroutinesApi::class)
    val searchedBooksPagingDataFlow: Flow<PagingData<Book>> =
        combine(searchTermFlow, categoryFlow, languageFlow, ::Triple)

            .flatMapLatest { (searchText, category, language) ->
                searchBooks(searchText, category, language)
            }.distinctUntilChanged()
            .cachedIn(viewModelScope)


    private fun searchBooks(
        searchText: String,
        category: String,
        language: Language
    ): Flow<PagingData<Book>> =
        booksRepository.searchBooks(searchText, category, listOf(language.code))

    fun updateCategory(selected: String) {
        category = selected
    }

    fun updateSearchTerm(text: String) {
        searchTerm = text
    }

    fun updateLanguage(language: Language) {
        this.language = language
    }


}