package com.example.guttenburg.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.example.guttenburg.data.repository.model.BookWithExtras
import com.example.guttenburg.download.DownloadError
import com.example.guttenburg.download.DownloadStatus
import com.example.guttenburg.ui.components.AppBar
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
    downloadUrl = "https://www.gutenberg.org/ebooks/145.txt.utf-8",
    fileExtension = ".epub",
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
    onShowSnackbar: (String) -> Unit = {}
) {
    val book = viewModel.book.collectAsState()
    val result = book.value
    val downloadStatus =
        viewModel.downloadStatus.collectAsStateWithLifecycle(initialValue = DownloadStatus.NotDownloading)
    val downloadProgress = viewModel.downloadProgress.collectAsStateWithLifecycle(initialValue = 0f)

    LaunchedEffect(key1 = downloadStatus.value) {
        val downloadState = downloadStatus.value

        if (downloadState is DownloadStatus.Successful || downloadState is DownloadStatus.Failed) {
            val message =
                if (downloadState is DownloadStatus.Successful) "Download Completed Successfully"
                else when ((downloadState as DownloadStatus.Failed).error) {
                    DownloadError.InsufficientSpaceError -> "Insufficient Space"
                    DownloadError.HttpError -> "Internet Unavailable"
                    DownloadError.UnknownError -> "An Error Happened"
                }

            onShowSnackbar(message)
        }


    }


    Column(modifier = Modifier.fillMaxSize()) {

        AppBar(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(R.string.details),
            onBackPress = onBackPress
        )

        Box(modifier = Modifier.fillMaxSize()) {

            if (result is Result.Success) {
                BookDetails(
                    modifier = Modifier.fillMaxSize(),
                    book = result.data,
                    downloadStatus = downloadStatus.value,
                    downloadProgress = downloadProgress.value,
                    onDownloadClick = viewModel::downloadBook,
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
    onCancelDownload: (BookWithExtras) -> Unit = {}
) {
    Log.d(TAG, "BookDetails: $book")
    val isDownloadProgressVisible =
        downloadStatus is DownloadStatus.Pending || downloadStatus is DownloadStatus.Running || downloadStatus is DownloadStatus.Paused
    val isDownloadSuccessIndicatorVisible = downloadStatus is DownloadStatus.Successful

    Column(
        modifier = modifier
            .fillMaxWidth()
            .withFadingEdgeEffect()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Spacer(modifier = Modifier.height(24.dp))
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

        if (isDownloadProgressVisible)
            DownloadProgressIndicator(
                downloadStatus = downloadStatus,
                downloadProgress = downloadProgress
            )
        else
            Spacer(modifier = Modifier.height(4.dp))
        


        Spacer(modifier = Modifier.height(14.dp))

        DownloadButton(
            modifier = Modifier.padding(horizontal = 16.dp),
            book = book,
            onDownloadClick = onDownloadClick,
            onCancelDownloadClick = onCancelDownload
        )

        Spacer(Modifier.height(2.dp))

        if (isDownloadSuccessIndicatorVisible)
            DownloadSuccessIndicator()
        else
            Spacer(Modifier.height(20.dp))


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

    if (downloadStatus == DownloadStatus.Pending)
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

@Preview
@Composable
fun DownloadProgressIndicatorPreview() {
    Row(Modifier.background(color = Color.Red)) {
        DownloadProgressIndicator(
            modifier = Modifier.weight(1f),
            downloadStatus = DownloadStatus.Pending,
            downloadProgress = 0f
        )
        Box(
            modifier = Modifier
                .size(4.dp)
                .background(Color.Black)
                .weight(1f)
        )
    }
}


@Composable
fun DownloadButton(
    modifier: Modifier = Modifier,
    book: BookWithExtras,
    onDownloadClick: (BookWithExtras) -> Unit = {},
    onCancelDownloadClick: (BookWithExtras) -> Unit = {}
) {
    val buttonLabelId = when (book.downloadId) {
        null -> R.string.download
        else -> R.string.cancel
    }
    val onClick: () -> Unit = {
        Log.d(TAG, "StartReadingButton: fileUri:${book.fileUri}, downloadId:${book.downloadId}")
        when (book.downloadId) {
            null -> onDownloadClick(book)
            else -> onCancelDownloadClick(book)
        }
    }


    Button(
        modifier = modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.secondary
        ), onClick = onClick,
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(text = stringResource(buttonLabelId), style = MaterialTheme.typography.body1)
    }
}

@Preview
@Composable
private fun DownloadButtonPreview() {
    DownloadButton(book = DEFAULT_BOOK_WITH_EXTRAS)
}

@Composable
fun DownloadSuccessIndicator(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = stringResource(R.string.download_successfully),
            style = MaterialTheme.typography.caption,
        )
        Spacer(modifier = Modifier.padding(horizontal = 4.dp))
        Icon(
            modifier = Modifier.size(16.dp),
            imageVector = Icons.Default.Check,
            contentDescription = stringResource(R.string.check_icon),
            tint = Color.Green
        )
    }
}

@Preview
@Composable
fun DownloadSuccessIndicatorPreview() {
    DownloadSuccessIndicator()
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









