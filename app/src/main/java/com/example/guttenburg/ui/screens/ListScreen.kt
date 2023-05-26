package com.example.guttenburg.ui.screens


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.guttenburg.R
import com.example.guttenburg.data.repository.Book
import com.example.guttenburg.ui.components.BookItem
import com.example.guttenburg.ui.components.Category
import com.example.guttenburg.ui.components.DEFAULT_CATEGORIES
import com.example.guttenburg.ui.components.ErrorLayout
import com.example.guttenburg.ui.theme.GuttenburgTheme
import com.example.guttenburg.ui.util.*
import com.example.guttenburg.ui.viewmodels.BooksViewModel
import kotlinx.coroutines.flow.flowOf


private const val TAG = "ListScreen"


@Composable
fun ListScreen(
    viewModel: BooksViewModel = hiltViewModel(),
    onBookItemClick: (id: Long, title: String, author: String) -> Unit = { _, _, _ -> }
) {

    val selectedCategory = viewModel.category.collectAsStateWithLifecycle()
    val lazyPagedItems = viewModel.searchedBooksPagingDataFlow.collectAsLazyPagingItems()
    val searchText = remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        SearchTextField(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
            searchText = searchText.value,
            onTextChanged = { searchText.value = it },
            submitSearch = { viewModel.setSearchText(it) })

        Categories(
            modifier = Modifier.padding(top = 16.dp),
            categories = DEFAULT_CATEGORIES.keys.toList(),
            selectedCategory = selectedCategory.value
        ) { category ->
            viewModel.selectCategory(DEFAULT_CATEGORIES[category]!!)
        }

        Box(modifier = Modifier.fillMaxSize()) {
            if (lazyPagedItems.isRefreshSuccess()) {
                BooksGrid(
                    books = lazyPagedItems,
                    isAppendLoading = lazyPagedItems.isAppendLoading(),
                    isAppendError = lazyPagedItems.isAppendError(),
                    onBookItemClick = onBookItemClick,
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
                    error = (lazyPagedItems.loadState.refresh as LoadState.Error).error
                )
            }

            if (lazyPagedItems.isRefreshEmpty()) {
                RefreshEmptyLayout(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 16.dp)
                )
            }

        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SearchTextField(
    modifier: Modifier = Modifier,
    searchText: String,
    onTextChanged: (String) -> Unit = {},
    submitSearch: (String) -> Unit = {}
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val isFocused = remember { mutableStateOf(false) }
    val borderColorAlpha: Float by animateFloatAsState(if (isFocused.value) 1.0f else 0.6f)
    val leadingIconTint =
        if (isFocused.value) MaterialTheme.colors.primary else MaterialTheme.colors.primary.copy(
            alpha = 0.5f
        )

    OutlinedTextField(
        modifier = modifier
            .border(
                width = 2.dp,
                color = MaterialTheme.colors.primary.copy(alpha = borderColorAlpha),
                shape = RoundedCornerShape(24.dp)
            )
            .onFocusChanged { focusState -> isFocused.value = focusState.isFocused }
            .fillMaxWidth(),
        value = searchText,
        onValueChange = onTextChanged,
        placeholder = {
            Text(
                text = "Search by Title, Author...", style = MaterialTheme.typography.body2
            )
        },
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.search),
                tint = leadingIconTint
            )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Transparent, unfocusedBorderColor = Color.Transparent
        ),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions {
            submitSearch(searchText)
            keyboardController?.hide()
            focusManager.clearFocus()
        },
    )
}


@Preview
@Composable
fun SearchTextFieldPreview() {
    GuttenburgTheme() {
        SearchTextField(searchText = "")
    }
}


@Composable
fun RefreshEmptyLayout(modifier: Modifier = Modifier) {
    Text(modifier = modifier, text = stringResource(R.string.no_books_found))
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
                Icon(modifier = Modifier.size(28.dp),imageVector = Icons.Default.Refresh, contentDescription = "Refresh")
            }
        }

    }
}


@Preview
@Composable
fun AppStateLayoutPreview() {
    Row(
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start
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

@Composable
fun BooksGrid(
    modifier: Modifier = Modifier,
    books: LazyPagingItems<Book>,
    isAppendLoading: Boolean,
    isAppendError: Boolean,
    onBookItemClick: (id: Long, title: String, author: String) -> Unit = { _, _, _ -> },
    retry: () -> Unit = {}
) {

    val scrollState = rememberLazyGridState()

    LazyVerticalGrid(
        state = scrollState,
        modifier = modifier.withFadingEdgeEffect(),
        columns = GridCells.Adaptive(minSize = 168.dp),
        contentPadding = PaddingValues(top = 24.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        items(count = books.itemCount, key = { index -> books[index]?.id ?: -1 }) { index ->
            val book = books[index]
            book?.let { BookItem(book = book, onItemClick = onBookItemClick) }
        }

        if (isAppendLoading || isAppendError) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                AppendStateLayout(
                    isLoading = isAppendLoading, isError = isAppendError, retry = retry
                )
            }
        }


    }
}


private val DEFAULT_BOOK_LIST = listOf(
    Book(
        id = 0, title = "Moby Dick", authors = listOf("Herman Meville"), imageUrl = ""
    ), Book(
        id = 0,
        title = "Authority",
        authors = listOf("Jeff Vandermer"),
        imageUrl = "https://www.gutenberg.org/cache/epub/2641/pg2641.cover.medium.jpg"
    ), Book(
        id = 1,
        title = "The Adventures Of A dying Man",
        authors = listOf("William Shakespeare"),
        imageUrl = "https://www.gutenberg.org/cache/epub/2641/pg2641.cover.medium.jpg"
    ), Book(
        id = 2,
        title = "The Adventures Of A dying Man",
        authors = listOf("William Shakespeare"),
        imageUrl = "https://www.gutenberg.org/cache/epub/2641/pg2641.cover.medium.jpg"
    ), Book(
        id = 3,
        title = "The Adventures Of A dying Man",
        authors = listOf("William Shakespeare"),
        imageUrl = "https://www.gutenberg.org/cache/epub/2641/pg2641.cover.medium.jpg"
    ), Book(
        id = 4,
        title = "The Adventures Of A dying Man",
        authors = listOf("William Shakespeare"),
        imageUrl = "https://www.gutenberg.org/cache/epub/2641/pg2641.cover.medium.jpg"
    ), Book(
        id = 4,
        title = "The Adventures Of A dying Man",
        authors = listOf("William Shakespeare"),
        imageUrl = "https://www.gutenberg.org/cache/epub/2641/pg2641.cover.medium.jpg"
    ), Book(
        id = 5,
        title = "The Adventures Of A dying Man",
        authors = listOf("William Shakespeare"),
        imageUrl = "https://www.gutenberg.org/cache/epub/2641/pg2641.cover.medium.jpg"
    ), Book(
        id = 6,
        title = "The Adventures Of A dying Man",
        authors = listOf("William Shakespeare"),
        imageUrl = "https://www.gutenberg.org/cache/epub/2641/pg2641.cover.medium.jpg"
    ), Book(
        id = 7,
        title = "The Adventures Of A dying Man",
        authors = listOf("William Shakespeare"),
        imageUrl = "https://www.gutenberg.org/cache/epub/2641/pg2641.cover.medium.jpg"
    ), Book(
        id = 8,
        title = "The Adventures Of A dying Man",
        authors = listOf("William Shakespeare"),
        imageUrl = "https://www.gutenberg.org/cache/epub/2641/pg2641.cover.medium.jpg"
    ), Book(
        id = 9,
        title = "The Adventures Of A dying Man",
        authors = listOf("William Shakespeare"),
        imageUrl = "https://www.gutenberg.org/cache/epub/2641/pg2641.cover.medium.jpg"
    )
)

@Preview
@Composable
private fun BooksGridPreview() {
    BooksGrid(
        modifier = Modifier.fillMaxSize(),
        books = flowOf(PagingData.from(DEFAULT_BOOK_LIST)).collectAsLazyPagingItems(),
        isAppendLoading = false,
        isAppendError = false
    )
}