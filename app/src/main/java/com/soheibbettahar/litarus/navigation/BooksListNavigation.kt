package com.soheibbettahar.litarus.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import com.soheibbettahar.litarus.ui.screens.ListScreen
import com.soheibbettahar.litarus.ui.viewmodels.BooksViewModel

const val BooksListRoute = "BooksList"

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.booksListScreen(
    ontNavigateToBookDetailScreen: (id: Long, title: String, author: String?) -> Unit,
    onShowSnackbar: (String) -> Unit
) {
    composable(BooksListRoute) {
        val viewModel: BooksViewModel = hiltViewModel()
        val searchTerm = viewModel.searchTerm
        val selectedCategory = viewModel.category
        val language = viewModel.language
        val lazyPagedItems = viewModel.searchedBooksPagingDataFlow.collectAsLazyPagingItems()

        ListScreen(
            searchTerm,
            selectedCategory,
            language,
            lazyPagedItems,
            viewModel::updateSearchTerm,
            viewModel::updateCategory,
            viewModel::updateLanguage,
            onBookItemClick = ontNavigateToBookDetailScreen,
            onShowSnackbar = onShowSnackbar
        )
    }
}