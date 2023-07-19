package com.soheibbettahar.litarus.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.soheibbettahar.litarus.R


@Composable
fun BookCover(modifier: Modifier = Modifier, imageUrl: String) {

    Card(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium),
        elevation = 4.dp
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize(),
            model = ImageRequest.Builder(LocalContext.current).data(imageUrl)
                .crossfade(true)
                .diskCacheKey(imageUrl)
                .build(),
            contentDescription = stringResource(R.string.book_cover),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.texture),
            error = painterResource(id = R.drawable.texture)
        )
    }

}

@Preview
@Composable
private fun BookCoverPreview() {
    BookCover(modifier = Modifier.size(height = 235.dp, width = 160.dp), imageUrl = "")
}

