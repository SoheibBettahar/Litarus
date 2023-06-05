package com.example.guttenburg.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.guttenburg.R
import com.example.guttenburg.data.Result
import com.example.guttenburg.data.repository.BookWithExtras
import com.example.guttenburg.download.DownloadStatus
import com.example.guttenburg.ui.components.BookCover
import com.example.guttenburg.ui.components.ErrorLayout
import com.example.guttenburg.ui.theme.GuttenburgTheme
import com.example.guttenburg.ui.util.withFadingEdgeEffect
import com.example.guttenburg.ui.viewmodels.BookDetailViewModel

private const val TAG = "DetailScreen"

private val DEFAULT_BOOK_WITH_EXTRAS = BookWithExtras(
    id = 145,
    title = "Middlemarch",
    description = "This is an analysis of the life of an English provincial town during the" +
            " time of social unrest prior to the first Reform Bill of 1832. It is told through" +
            " the lives of Dorothea Brooke and Dr Tertius Lydgate and includes a host of characters" +
            " who illuminate the condition of English life in the mid 19th century. This is an " +
            "analysis of the life of an English provincial town during the time of social unrest" +
            " prior to the first Reform Bill of 1832. It is told through the lives of Dorothea " +
            "<Brooke and Dr Tertius Lydgate and includes a host of characters who illuminate the " +
            "condition of English life in the mid 19th century. This is an analysis of the life " +
            "of an English provincial town during the time of social unrest prior to the first " +
            "Reform Bill of 1832. It is told through the lives of Dorothea Brooke and Dr Tertius" +
            " Lydgate and includes a host of characters who illuminate the condition of English " +
            "life in the mid 19th century. This is an analysis of the life of an English provincial" +
            " town during the time of social unrest prior to the first Reform Bill of 1832. It is " +
            "told through the lives of Dorothea Brooke and Dr Tertius Lydgate and includes a host" +
            " of characters who illuminate the condition of English life in the mid 19th century." +
            " This is an analysis of the life of an English provincial town during the time of " +
            "social unrest prior to the first Reform Bill of 1832. It is told through the lives of" +
            " Dorothea Brooke and Dr Tertius Lydgate and includes a host of characters who " +
            "illuminate the condition of English life in the mid 19th century. This is an analysis" +
            " of the life of an English provincial town during the time of social unrest prior to" +
            " the first Reform Bill of 1832. It is told through the lives of Dorothea Brooke and " +
            "Dr Tertius Lydgate and includes a host of characters who illuminate the condition of" +
            " English life in the mid 19th century. This is an analysis of the life of an English" +
            " provincial town during the time of social unrest prior to the first Reform Bill of " +
            "1832. It is told through the lives of Dorothea Brooke and Dr Tertius Lydgate and includes a host of characters who illuminate the condition of English life in the mid 19th century. This is an analysis of the life of an English provincial town during the time of social unrest prior to the first Reform Bill of 1832. It is told through the lives of Dorothea Brooke and Dr Tertius Lydgate and includes a host of characters who illuminate the condition of English life in the mid 19th century. This is an analysis of the life of an English provincial town during the time of social unrest prior to the first Reform Bill of 1832. It is told through the lives of Dorothea Brooke and Dr Tertius Lydgate and includes a host of characters who illuminate the condition of English life in the mid 19th century. This is an analysis of the life of an English provincial town during the time of social unrest prior to the first Reform Bill of 1832. It is told through the lives of Dorothea Brooke and Dr Tertius Lydgate and includes a host of characters who illuminate the condition of English life in the mid 19th century. This is an analysis of the life of an English provincial town during the time of social unrest prior to the first Reform Bill of 1832. It is told through the lives of Dorothea Brooke and Dr Tertius Lydgate and includes a host of characters who illuminate the condition of English life in the mid 19th century.",
    pageCount = 740,
    epubDownloadUrl = "https://www.gutenberg.org/ebooks/145.txt.utf-8",
    imageUrl = "https://www.gutenberg.org/cache/epub/145/pg145.cover.medium.jpg",
    downloadCount = 130471,
    language = "English",
    authors = "Eliot, George",
    downloadId = null,
    fileUri = null
)


@Composable
fun DetailScreen(
    viewModel: BookDetailViewModel = hiltViewModel(),
    onBackPress: () -> Unit = {},
    onStartReadingPress: (bookId: Long) -> Unit = {},
    onShowSnackbar: (String) -> Unit = {}
) {
    val book = viewModel.book.collectAsState()
    val result = book.value
    val downloadStatus =
        viewModel.downloadStatus.collectAsStateWithLifecycle(initialValue = DownloadStatus.NotDownloading)
    val downloadProgress = viewModel.downloadProgress.collectAsStateWithLifecycle(initialValue = 0f)
    val downloadError =
        viewModel.downloadErrorOrNull.collectAsStateWithLifecycle(initialValue = null)


    LaunchedEffect(key1 = downloadError.value) {
        downloadError.value?.let {
            onShowSnackbar(it.toString())
        }
    }


    Column(modifier = Modifier.fillMaxSize()) {

        AppBar(modifier = Modifier.fillMaxWidth(), onBackPress = onBackPress)

        Box(modifier = Modifier.fillMaxSize()) {

            if (result is Result.Success) {
                BookDetails(
                    modifier = Modifier.fillMaxSize(),
                    book = result.data,
                    downloadStatus = downloadStatus.value,
                    downloadProgress = downloadProgress.value,
                    onDownloadClick = viewModel::downloadBook,
                    onStartReadingClick = { onStartReadingPress(it.id) },
                    onCancelDownload = viewModel::cancelDownload
                )
            }


            if (result is Result.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(
                        Alignment.Center
                    )
                )
            }

            if (result is Result.Error)
                ErrorLayout(
                    modifier = Modifier.align(Alignment.Center),
                    error = result.exception,
                    onRetryClick = { viewModel.getBook() }
                )
        }

    }

}

@Composable
fun AppBar(modifier: Modifier = Modifier, onBackPress: () -> Unit = {}) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        AppbarButton(
            icon = Icons.Default.ArrowBack,
            onClick = onBackPress,
            contentDescription = stringResource(R.string.back_button)
        )

        Text(text = stringResource(R.string.details), style = MaterialTheme.typography.body1)


        AppbarButton(
            icon = Icons.Default.MoreVert,
            contentDescription = stringResource(R.string.more_button)
        )
    }
}

@Preview
@Composable
fun AppBarPreview() {
    GuttenburgTheme() {
        AppBar()
    }
}

@Composable
fun BookDetails(
    modifier: Modifier = Modifier,
    book: BookWithExtras,
    downloadStatus: DownloadStatus = DownloadStatus.NotDownloading,
    downloadProgress: Float = 0f,
    onDownloadClick: (BookWithExtras) -> Unit = {},
    onStartReadingClick: (BookWithExtras) -> Unit = {},
    onCancelDownload: (BookWithExtras) -> Unit = {}
) {
    Log.d(TAG, "BookDetails: $book")


    Column(
        modifier = modifier
            .fillMaxWidth()
            .withFadingEdgeEffect()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Spacer(modifier = Modifier.height(24.dp))
        //260 -> 320 , 180 -> 220
        BookCover(
            modifier = Modifier.size(
                height = if (book.description.isNotEmpty()) 260.dp else 320.dp,
                width = if (book.description.isNotEmpty()) 180.dp else 220.dp
            ), imageUrl = book.imageUrl
        )
        Spacer(modifier = Modifier.height(55.dp))


        if (book.areInfoAvailable()) {
            BookInfo(book = book)
            Spacer(modifier = Modifier.height(18.dp))
        }


        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = book.title,
            style = MaterialTheme.typography.subtitle1,
            textAlign = TextAlign.Center
        )

        if (book.authors.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = book.authors,
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (book.fileUri == null && book.downloadId != null) {
            DownloadProgressIndicator(
                downloadStatus = downloadStatus,
                downloadProgress = downloadProgress
            )
        } else {
            Spacer(modifier = Modifier.height(2.dp))
        }


        Spacer(modifier = Modifier.height(18.dp))

        StartReadingButton(
            modifier = Modifier.padding(horizontal = 16.dp),
            book = book,
            onDownloadClick = onDownloadClick,
            onStartReadingClick = onStartReadingClick,
            onCancelDownloadClick = onCancelDownload
        )

        Spacer(modifier = Modifier.height(18.dp))


        if (!book.description.isNullOrBlank()) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                text = book.description,
                style = MaterialTheme.typography.body2
            )

            Spacer(modifier = Modifier.height(16.dp))
        }


    }

}

@Composable
fun DownloadProgressIndicator(
    modifier: Modifier = Modifier,
    downloadStatus: DownloadStatus,
    downloadProgress: Float
) {
    val localModifier = modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
    val color = MaterialTheme.colors.secondary
    val strokeCap = StrokeCap.Round

    if (downloadStatus == DownloadStatus.NotDownloading || downloadStatus == DownloadStatus.Pending)
        LinearProgressIndicator(
            modifier = localModifier,
            color = color,
            strokeCap = strokeCap
        )
    else
        LinearProgressIndicator(
            progress = downloadProgress,
            modifier = localModifier,
            color = color,
            strokeCap = strokeCap
        )
}


@Composable
fun StartReadingButton(
    modifier: Modifier = Modifier,
    book: BookWithExtras,
    onStartReadingClick: (BookWithExtras) -> Unit = {},
    onDownloadClick: (BookWithExtras) -> Unit = {},
    onCancelDownloadClick: (BookWithExtras) -> Unit = {}
) {
    val buttonTextId = when {
        book.downloadId == null && book.fileUri == null -> R.string.download
        book.fileUri == null -> R.string.cancel
        else -> R.string.start_reading
    }
    val onClick: () -> Unit = {
        when {
            book.downloadId == null && book.fileUri == null -> onDownloadClick(book)
            book.fileUri == null -> onCancelDownloadClick(book)
            else -> onStartReadingClick(book)
        }
    }


    Button(
        modifier = modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.secondary
        ), onClick = onClick,
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(text = stringResource(buttonTextId), style = MaterialTheme.typography.body1)
    }
}

@Preview
@Composable
private fun StartReadingButtonPreview() {
    StartReadingButton(book = DEFAULT_BOOK_WITH_EXTRAS)
}

@Preview
@Composable
fun BookDetailsPreview() {
    GuttenburgTheme() {
        BookDetails(
            modifier = Modifier.fillMaxSize(),
            book = DEFAULT_BOOK_WITH_EXTRAS.copy(downloadId = 5),
            downloadProgress = 0.5f,
            downloadStatus = DownloadStatus.Pending
        )
    }
}

@Composable
fun AppbarButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    onClick: () -> Unit = {},
    contentDescription: String
) {
    IconButton(
        modifier = modifier
            .clip(CircleShape),
        onClick = onClick
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
        )
    }
}


@Preview
@Composable
fun CircularButtonPreview() {
    GuttenburgTheme() {
        AppbarButton(icon = Icons.Default.ArrowBack, contentDescription = "")
    }
}

@Composable
fun BookInfo(modifier: Modifier = Modifier, book: BookWithExtras) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        book.language?.let {
            InfoCell(text = it, icon = ImageVector.vectorResource(id = R.drawable.ic_language))
            Spacer(modifier = Modifier.width(17.dp))
        }

        book.pageCount?.let {
            InfoCell(
                text = it.toString(),
                icon = ImageVector.vectorResource(id = R.drawable.ic_page_count)
            )
            Spacer(modifier = Modifier.width(17.dp))
        }

        book.downloadCount?.let {
            InfoCell(
                text = it.toString(),
                icon = ImageVector.vectorResource(id = R.drawable.ic_download_count)
            )
        }

    }
}

@Preview
@Composable
fun BookInfoPreview() {
    GuttenburgTheme() {
        BookInfo(book = DEFAULT_BOOK_WITH_EXTRAS)
    }
}


@Composable
fun InfoCell(modifier: Modifier = Modifier, text: String, icon: ImageVector) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(
            modifier = Modifier.size(20.dp),
            imageVector = icon,
            contentDescription = "Book Info Icon",
            tint = MaterialTheme.colors.secondary
        )

        Spacer(modifier = Modifier.width(5.dp))

        Text(
            text = text, style = MaterialTheme.typography.subtitle2, textAlign = TextAlign.Center
        )
    }
}


@Preview
@Composable
fun InfoCellPreview() {
    InfoCell(text = "14", icon = Icons.Default.Star)
}





