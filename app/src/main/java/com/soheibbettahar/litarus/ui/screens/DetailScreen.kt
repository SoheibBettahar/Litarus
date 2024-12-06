package com.soheibbettahar.litarus.ui.screens

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.soheibbettahar.litarus.R
import com.soheibbettahar.litarus.data.Result
import com.soheibbettahar.litarus.data.repository.model.BookWithExtras
import com.soheibbettahar.litarus.download.DownloadError
import com.soheibbettahar.litarus.download.DownloadStatus
import com.soheibbettahar.litarus.ui.TrackScreenViewEvent
import com.soheibbettahar.litarus.ui.components.AppBar
import com.soheibbettahar.litarus.ui.components.BookCover
import com.soheibbettahar.litarus.ui.components.ErrorLayout
import com.soheibbettahar.litarus.ui.shareBook
import com.soheibbettahar.litarus.ui.theme.LitarusTheme
import com.soheibbettahar.litarus.ui.util.withFadingEdgeEffect
import com.soheibbettahar.litarus.ui.viewmodels.BookDetailUiState
import com.soheibbettahar.litarus.ui.viewmodels.DownloadState
import com.soheibbettahar.litarus.util.DEFAULT_BOOK_WITH_EXTRAS
import com.soheibbettahar.litarus.util.analytics.LocalAnalyticsHelper

private const val TAG = "DetailScreen"

@Composable
fun DetailScreen(
    bookUiState: BookDetailUiState,
    downloadState: DownloadState,
    bookId: Long,
    bookTitle: String,
    bookAuthor: String,
    isOnline: Boolean,
    onDownloadClick: (BookWithExtras) -> Unit,
    onCancelDownloadClick: (BookWithExtras) -> Unit,
    getBookDetails: () -> Unit,
    onBackPress: () -> Unit = {},
    onShowSnackbar: (String) -> Unit = {},
    onShowSnackbarWithSettingsAction: (text: String, action: String) -> Unit = { _, _ -> },
    onSharePress: (Long) -> Unit = {},
    onRequestAppReview: () -> Unit = { //TODO: call it somewhere when you figure out the logic
    }
) {

    val loadResult = bookUiState.loadResult

    val downloadSuccess = stringResource(R.string.check_mark_download_success)
    val insufficientSpace = stringResource(R.string.close_mark_insufficient_space)
    val internetUnavailable = stringResource(R.string.warning_mark_internet_unavailable)
    val unknownError = stringResource(R.string.close_mark_unknown_error)

    LaunchedEffect(key1 = downloadState.status) {
        val downloadStatus = downloadState.status
        if (downloadStatus is DownloadStatus.Successful || downloadStatus is DownloadStatus.Failed) {
            val message = if (downloadStatus is DownloadStatus.Successful) downloadSuccess
            else when ((downloadStatus as DownloadStatus.Failed).error) {
                DownloadError.InsufficientSpaceError -> insufficientSpace
                DownloadError.HttpError -> internetUnavailable
                DownloadError.UnknownError -> unknownError
            }

            onShowSnackbar(message)
        }
    }

    val analyticsHelper = LocalAnalyticsHelper.current


    Column(modifier = Modifier.fillMaxSize()) {

        AppBar(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(R.string.details),
            onBackPress = onBackPress,
            onSharePress = {
                analyticsHelper.shareBook(bookId.toString(), bookTitle, bookAuthor)
                onSharePress(bookId)
            }
        )


        Box(modifier = Modifier.fillMaxSize()) {
            if (loadResult is Result.Success) {
                BookDetails(
                    modifier = Modifier.fillMaxSize(),
                    book = loadResult.data,
                    downloadStatus = downloadState.status,
                    downloadProgress = downloadState.progress,
                    onDownloadClick = {
                        if (isOnline) onDownloadClick(it)
                        else onShowSnackbar(internetUnavailable)
                    },
                    onCancelDownload = onCancelDownloadClick,
                    onShowSnackbarWithSettingsAction = onShowSnackbarWithSettingsAction
                )
            }

            if (loadResult is Result.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(
                        Alignment.Center
                    )
                )
            }

            if (loadResult is Result.Error) {
                ErrorLayout(
                    modifier = Modifier.align(Alignment.Center),
                    error = loadResult.exception,
                    onRetryClick = getBookDetails
                )
            }

        }
    }

    TrackScreenViewEvent(screenName = "Detail")
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BookDetails(
    modifier: Modifier = Modifier,
    book: BookWithExtras,
    downloadStatus: DownloadStatus = DownloadStatus.NotDownloading,
    downloadProgress: Float = 0f,
    onDownloadClick: (BookWithExtras) -> Unit = {},
    onCancelDownload: (BookWithExtras) -> Unit = {},
    onShowSnackbarWithSettingsAction: (String, String) -> Unit = { _, _ -> }
) {
    val isDownloadProgressVisible =
        downloadStatus is DownloadStatus.Pending || downloadStatus is DownloadStatus.Running || downloadStatus is DownloadStatus.Paused
    val isDownloadSuccessIndicatorVisible = downloadStatus is DownloadStatus.Successful

    val storagePermissionState =
        rememberPermissionState(permission = Manifest.permission.WRITE_EXTERNAL_STORAGE)

    val storagePermissionResultLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted -> if (isGranted) onDownloadClick(book) })

    val storagePermissionRequired = stringResource(R.string.storage_permission_required)
    val goToSettings = stringResource(R.string.go_to_settings)

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

        AnimatedVisibility(visible = isDownloadProgressVisible) {
            DownloadProgressIndicator(
                downloadStatus = downloadStatus, downloadProgress = downloadProgress
            )
        }


        if (book.downloadUrl != null) {

            Spacer(modifier = Modifier.height(14.dp))

            DownloadButton(
                modifier = Modifier.padding(horizontal = 16.dp), book = book, onDownloadClick = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q || storagePermissionState.status == PermissionStatus.Granted) {
                        onDownloadClick(it)
                    } else if (storagePermissionState.status.shouldShowRationale) {
                        onShowSnackbarWithSettingsAction(storagePermissionRequired, goToSettings)
                    } else {
                        storagePermissionResultLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    }

                }, onCancelDownloadClick = onCancelDownload
            )
        }

        Spacer(Modifier.height(2.dp))

        DownloadSuccessIndicator(isVisible = isDownloadSuccessIndicatorVisible)



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



@Preview
@Composable
fun BookDetailsPreview() {
        BookDetails(
            modifier = Modifier.fillMaxSize(),
            book = DEFAULT_BOOK_WITH_EXTRAS.copy(downloadId = 5),
            downloadProgress = 0.5f,
            downloadStatus = DownloadStatus.Pending
        )
}

@Composable
fun DownloadProgressIndicator(
    modifier: Modifier = Modifier, downloadStatus: DownloadStatus, downloadProgress: Float
) {
    val localModifier = modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
    val color = MaterialTheme.colors.secondary
    val strokeCap = StrokeCap.Round

    val progress: Float by animateFloatAsState(targetValue = downloadProgress)

    if (downloadStatus == DownloadStatus.Pending) LinearProgressIndicator(
        modifier = localModifier, color = color, strokeCap = strokeCap
    )
    else LinearProgressIndicator(
        progress = progress, modifier = localModifier, color = color, strokeCap = strokeCap
    )
}

@Preview
@Composable
fun DownloadProgressIndicatorPreview() {
    DownloadProgressIndicator(
        downloadStatus = DownloadStatus.Pending, downloadProgress = 0f
    )

}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DownloadButton(
    modifier: Modifier = Modifier,
    book: BookWithExtras,
    onDownloadClick: (BookWithExtras) -> Unit = {},
    onCancelDownloadClick: (BookWithExtras) -> Unit = {}
) {
    val buttonLabel = when (book.downloadId) {
        null -> stringResource(id = R.string.download)
        else -> stringResource(id = R.string.cancel)
    }
    val onClick: () -> Unit = {
        when (book.downloadId) {
            null -> onDownloadClick(book)
            else -> onCancelDownloadClick(book)
        }
    }


    Button(
        modifier = modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.secondary
        ), onClick = onClick, shape = RoundedCornerShape(12.dp)
    ) {
        AnimatedContent(targetState = buttonLabel) { label ->
            Text(text = label, style = MaterialTheme.typography.body1)
        }

    }
}

@Preview
@Composable
private fun DownloadButtonPreview() {
    DownloadButton(book = DEFAULT_BOOK_WITH_EXTRAS)
}

@Composable
fun DownloadSuccessIndicator(modifier: Modifier = Modifier, isVisible: Boolean = false) {
    val alpha: Float by animateFloatAsState(targetValue = if (isVisible) 1f else 0f)

    Row(
        modifier = modifier.graphicsLayer(alpha = alpha),
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
    DownloadSuccessIndicator(isVisible = true)
}


@Composable
fun BookInfo(modifier: Modifier = Modifier, book: BookWithExtras) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        if (book.languages.isNotEmpty()) {
            InfoCell(
                text = book.languages,
                icon = ImageVector.vectorResource(id = R.drawable.ic_language)
            )
            Spacer(modifier = Modifier.width(17.dp))
        }

        if (book.pageCount > 0) {
            InfoCell(
                text = book.pageCount.toString(),
                icon = ImageVector.vectorResource(id = R.drawable.ic_page_count)
            )
            Spacer(modifier = Modifier.width(17.dp))
        }

        if (book.downloadCount > -1) {
            InfoCell(
                text = book.downloadCount.toString(),
                icon = ImageVector.vectorResource(id = R.drawable.ic_download_count)
            )
        }
    }
}

@Preview
@Composable
fun BookInfoPreview() {
    LitarusTheme() {
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









