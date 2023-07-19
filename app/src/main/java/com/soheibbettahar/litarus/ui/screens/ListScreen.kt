package com.soheibbettahar.litarus.ui.screens


import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.soheibbettahar.litarus.R
import com.soheibbettahar.litarus.data.repository.model.Book
import com.soheibbettahar.litarus.ui.TrackScreenViewEvent
import com.soheibbettahar.litarus.ui.components.*
import com.soheibbettahar.litarus.ui.openBook
import com.soheibbettahar.litarus.ui.theme.LitarusTheme
import com.soheibbettahar.litarus.ui.util.*
import com.soheibbettahar.litarus.util.DEFAULT_BOOK_LIST
import com.soheibbettahar.litarus.util.DEFAULT_CATEGORIES
import com.soheibbettahar.litarus.util.analytics.LocalAnalyticsHelper
import kotlinx.coroutines.flow.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException


private const val TAG = "ListScreen"


@Composable
fun ListScreen(
    searchTerm: String,
    selectedCategory: String,
    language: Language,
    lazyPagedItems: LazyPagingItems<Book>,
    onUpdateSearchTerm: (String) -> Unit,
    onUpdateCategory: (String) -> Unit,
    onUpdateLanguage: (Language) -> Unit,
    onBookItemClick: (id: Long, title: String, author: String?) -> Unit = { _, _, _ -> },
    onShowSnackbar: (String) -> Unit = {}
) {

    val scrollState = rememberLazyGridState()
    LaunchedEffect(lazyPagedItems) {
        snapshotFlow { lazyPagedItems.loadState }
            .asRemotePresentationState()
            .map { it == RemotePresentationState.PRESENTED }
            .distinctUntilChanged()
            .collect { hasFinishedLoading ->
                if (hasFinishedLoading) scrollState.animateScrollToItem(
                    0
                )
            }
    }


    val internetUnavailable = stringResource(R.string.warning_mark_internet_unavailable)
    val internetSlow = stringResource(R.string.warning_mark_internet_slow)
    val unknownError = stringResource(R.string.close_mark_unknown_error)
    LaunchedEffect(lazyPagedItems.isAppendError()) {
        if (lazyPagedItems.isAppendError()) {
            val message = when (lazyPagedItems.appendError().error) {
                is UnknownHostException -> internetUnavailable
                is SocketTimeoutException -> internetSlow
                else -> unknownError
            }
            onShowSnackbar(message)
        }
    }

    var isLanguageDialogVisible: Boolean by rememberSaveable { mutableStateOf(false) }

    val analyticsHelper = LocalAnalyticsHelper.current

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        ListAppbar(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
            searchText = searchTerm,
            selectedLanguage = language,
            onTextChanged = onUpdateSearchTerm,
            onLanguageButtonClick = { isLanguageDialogVisible = true }
        )


        Categories(
            modifier = Modifier.padding(top = 16.dp),
            categories = DEFAULT_CATEGORIES.keys.toList(),
            selectedCategory = selectedCategory
        ) { category -> onUpdateCategory(DEFAULT_CATEGORIES[category]!!) }

        Box(modifier = Modifier.fillMaxSize()) {

            if (lazyPagedItems.isRefreshSuccess()) {
                BooksGrid(
                    books = lazyPagedItems,
                    gridState = scrollState,
                    isAppendLoading = lazyPagedItems.isAppendLoading(),
                    isAppendError = lazyPagedItems.isAppendError(),
                    onBookItemClick = { id, title, author ->
                        analyticsHelper.openBook(id.toString(), title, author.orEmpty())
                        onBookItemClick(id, title, author)
                    },
                    retry = { lazyPagedItems.retry() })
            }

            if (lazyPagedItems.isRefreshLoading()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            if (lazyPagedItems.isRefreshError()) {
                ErrorLayout(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 16.dp),
                    onRetryClick = { lazyPagedItems.refresh() },
                    error = lazyPagedItems.refreshError().error
                )
            }


            if (isLanguageDialogVisible) {
                LanguageDialog(
                    selectedLanguage = language,
                    onItemClick = onUpdateLanguage,
                    dismiss = { isLanguageDialogVisible = false }
                )
            }
        }
    }

    TrackScreenViewEvent(screenName = "List")

}


@Composable
fun Categories(
    modifier: Modifier = Modifier,
    categories: List<String>,
    selectedCategory: String,
    onCategoryClick: (String) -> Unit = {}
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        itemsIndexed(categories) { index, category ->
            Category(
                label = category,
                isSelected = DEFAULT_CATEGORIES[category] == selectedCategory,
                onCategoryClick = onCategoryClick
            )

            if (index < categories.lastIndex) Spacer(modifier = Modifier.width(8.dp))
        }
    }
}


@Preview
@Composable
fun CategoriesPreview() {
    Categories(
        categories = DEFAULT_CATEGORIES.keys.toList(),
        selectedCategory = DEFAULT_CATEGORIES["All"]!!
    )
}

const val BOOK_ITEM_CONTENT_TYPE = 0
const val FOOTER_CONTENT_TYPE = 1

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BooksGrid(
    modifier: Modifier = Modifier,
    books: LazyPagingItems<Book>,
    gridState: LazyGridState = rememberLazyGridState(),
    isAppendLoading: Boolean,
    isAppendError: Boolean,
    onBookItemClick: (id: Long, title: String, author: String?) -> Unit = { _, _, _ -> },
    retry: () -> Unit = {},
) {

    LazyVerticalGrid(
        state = gridState,
        modifier = modifier.withFadingEdgeEffect(),
        columns = GridCells.Adaptive(minSize = 132.dp),
        contentPadding = PaddingValues(top = 24.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        items(
            count = books.itemCount,
            key = { index -> books[index]?.id ?: -1 },
            contentType = { BOOK_ITEM_CONTENT_TYPE }) { index ->
            val book = books[index]

            if (book != null)
                BookItem(
                    modifier = Modifier.animateItemPlacement(tween(500)),
                    book = book,
                    onItemClick = onBookItemClick
                )
            else
                BookItemPlaceHolder()


        }

        if (isAppendLoading || isAppendError) {
            item(span = { GridItemSpan(maxLineSpan) }, contentType = { FOOTER_CONTENT_TYPE }) {
                AppendStateLayout(
                    isLoading = isAppendLoading, isError = isAppendError, retry = retry
                )
            }
        }


    }
}


@Preview
@Composable
private fun BooksGridPreview() {
    LitarusTheme() {
        BooksGrid(
            modifier = Modifier.fillMaxSize(),
            books = flowOf(PagingData.from(DEFAULT_BOOK_LIST)).collectAsLazyPagingItems(),
            isAppendLoading = false,
            isAppendError = false
        )
    }

}


@Composable
fun AppendStateLayout(
    modifier: Modifier = Modifier, isLoading: Boolean, isError: Boolean, retry: () -> Unit = {}
) {
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        if (isLoading) CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(vertical = 12.dp)
                .size(24.dp),
            strokeWidth = 2.dp,

            )

        if (isError) {
            IconButton(modifier = Modifier.align(Alignment.Center), onClick = retry) {
                Icon(
                    modifier = Modifier.size(28.dp),
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh"
                )
            }
        }

    }
}


@Preview
@Composable
fun AppendStateLayoutPreview() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        AppendStateLayout(
            modifier = Modifier
                .background(Color.Green)
                .weight(1f),
            isLoading = true,
            isError = false
        )


        AppendStateLayout(
            modifier = Modifier
                .background(Color.Red)
                .weight(1f), isLoading = false, isError = true
        )
    }

}