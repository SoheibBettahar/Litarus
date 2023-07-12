package com.soheibbettahar.litarus.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.composable
import com.soheibbettahar.litarus.ui.screens.DetailScreen
import com.soheibbettahar.litarus.ui.viewmodels.BookDetailUiState
import com.soheibbettahar.litarus.ui.viewmodels.BookDetailViewModel
import com.soheibbettahar.litarus.ui.viewmodels.DownloadState

const val BookDetailRoute = "BooksDetail"

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.bookDetailScreen(
    onBackPress: () -> Unit,
    onShowSnackbar: (String) -> Unit,
    onShowSnackbarWithSettingsAction: (String, String) -> Unit,
    onShareBookClick: (Long) -> Unit,
    onRequestAppReview: () -> Unit
) {
    composable(
        route = "$BookDetailRoute/{id}/{title}?author={author}",
        arguments = listOf(navArgument("id") { type = NavType.LongType },
            navArgument("title") { type = NavType.StringType },
            navArgument("author") { type = NavType.StringType; nullable = true })
    ) {
        val viewModel: BookDetailViewModel = hiltViewModel()
        val book: BookDetailUiState by viewModel.bookUiState.collectAsState()
        val downloadState: DownloadState by viewModel.downloadState.collectAsStateWithLifecycle()
        val bookId = viewModel.id
        val bookTitle = viewModel.title
        val bookAuthor = viewModel.author
        val isOnline: Boolean by viewModel.isOnline.collectAsStateWithLifecycle()

        DetailScreen(
            book,
            downloadState,
            bookId,
            bookTitle,
            bookAuthor,
            isOnline,
            viewModel::downloadBook,
            viewModel::cancelDownload,
            viewModel::getBook,
            onBackPress = onBackPress,
            onShowSnackbar = onShowSnackbar,
            onShowSnackbarWithSettingsAction = onShowSnackbarWithSettingsAction,
            onSharePress = onShareBookClick,
            onRequestAppReview = onRequestAppReview
        )
    }
}


fun NavHostController.navigateToBookDetail(id: Long, title: String, author: String?) {
    val route = "$BookDetailRoute/$id/$title?author={$author}"
    navigateSingleTopTo(route)
}


private const val idArg = "id"
private const val titleArg = "title"
private const val authorArg = "author"

internal class BookDetailArgs private constructor(
    val id: Long,
    val title: String,
    val author: String
) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(
                checkNotNull(savedStateHandle[idArg]) as Long,
                checkNotNull(savedStateHandle[titleArg]) as String,
                savedStateHandle.get<String>(authorArg).orEmpty()
            )
}
