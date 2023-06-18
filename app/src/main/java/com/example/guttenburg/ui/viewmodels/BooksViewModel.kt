package com.example.guttenburg.ui.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.guttenburg.data.repository.BooksRepository
import com.example.guttenburg.data.repository.model.Book
import com.example.guttenburg.ui.components.Language
import com.example.guttenburg.ui.components.NoLanguageFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class BooksViewModel @Inject constructor(private val booksRepository: BooksRepository) :
    ViewModel() {


    var searchTerm by mutableStateOf("")
        private set

    @OptIn(FlowPreview::class)
    private val searchTermFlow: Flow<String> = snapshotFlow { searchTerm }
        .debounce(200)
        .conflate()

    val category = MutableStateFlow(value = "")

    private val _language: MutableStateFlow<Language> = MutableStateFlow(NoLanguageFilter)
    val language: StateFlow<Language>
        get() = _language


    @OptIn(ExperimentalCoroutinesApi::class)
    val searchedBooksPagingDataFlow: Flow<PagingData<Book>> =
        combine(searchTermFlow, category, language, ::Triple)

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
        category.value = selected
    }

    fun updateSearchTerm(text: String) {
        searchTerm = text
    }

    fun updateLanguage(language: Language){
        _language.value = language
    }


}