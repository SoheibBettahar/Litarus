package com.example.guttenburg.ui.components

import android.accounts.NetworkErrorException
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


@Composable
fun ErrorLayout(modifier: Modifier = Modifier, error: Throwable?, onRetryClick: () -> Unit) {

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(modifier = modifier, text = "$error", textAlign = TextAlign.Center)

        Button(
            modifier = modifier,
            onClick = onRetryClick,
        ) { Text(text = stringResource(R.string.retry)) }
    }

}

@Preview
@Composable
fun ErrorLayoutPreview() {
    ErrorLayout(error = NetworkErrorException("Please check your internet")) {

    }
}