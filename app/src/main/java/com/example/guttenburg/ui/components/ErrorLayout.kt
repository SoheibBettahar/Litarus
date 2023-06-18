package com.example.guttenburg.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.guttenburg.R
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

private const val TAG = "ErrorLayout"

@Composable
fun ErrorLayout(modifier: Modifier = Modifier, error: Throwable?, onRetryClick: () -> Unit = {}) {
    val message = when (error) {
        is SocketTimeoutException, is UnknownHostException -> stringResource(id = R.string.internet_unavailable)
        is HttpException -> stringResource(id = R.string.server_error)
        else -> stringResource(id = R.string.unknown_error)
    }

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(modifier = modifier, text = message, textAlign = TextAlign.Center)

        Button(
            modifier = modifier,
            onClick = onRetryClick,
        ) { Text(text = stringResource(R.string.retry)) }
    }

}

@Preview
@Composable
fun ErrorLayoutPreview() {
    ErrorLayout(error = SocketTimeoutException()) {

    }
}