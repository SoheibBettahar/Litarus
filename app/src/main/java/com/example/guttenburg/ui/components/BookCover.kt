package com.example.guttenburg.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.guttenburg.R


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



fun Modifier.withGradientLayer() {
    drawWithCache {
        val colorStops = arrayOf(
            0.0f to Color(0X00FFFFFF),
            0.1f to Color(0XFFFFFFFF),
            0.02f to Color(0X00FFFFFF),
            0.04f to Color(0X00FFFFFF),
            0.05f to Color(0X00000000),
            0.05f to Color(0X51000000),
            0.05f to Color(0XBC000000),
            0.06f to Color(0XFF000000),
            0.06f to Color(0XFFFFFFFF),
            0.06f to Color(0XDDFFFFFF),
            0.06f to Color(0X77FFFFFF),
            0.07f to Color(0X00FFFFFF)
        )

        val bookCornerBrush =
            Brush.horizontalGradient(colorStops = colorStops, tileMode = TileMode.Decal)
        onDrawWithContent {

            drawContent()
            drawRect(bookCornerBrush, alpha = 0.2f)

        }
    }
}