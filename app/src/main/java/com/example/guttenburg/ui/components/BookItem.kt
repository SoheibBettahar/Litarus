package com.example.guttenburg.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.guttenburg.R
import com.example.guttenburg.data.repository.Book

private const val TAG = "BookItem"

@Composable
fun BookItem(
    modifier: Modifier = Modifier,
    book: Book,
    onItemClick: (id: Long, title: String, author: String?) -> Unit = { _, _, _ -> }
) {
    val bottomRadius = 4.dp
    Column(modifier = modifier
        .wrapContentSize()
        .clip(
            MaterialTheme.shapes.medium.copy(
                bottomEnd = CornerSize(bottomRadius),
                bottomStart = CornerSize(bottomRadius)
            )

        )
        .clickable {
            onItemClick(
                book.id,
                book.title,
                book.authors.firstOrNull()
            )
        }

    ) {

        BookCover(
            modifier = Modifier.size(height = 235.dp, width = 160.dp),
            imageUrl = book.imageUrl
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            modifier = Modifier.width(160.dp),
            text = book.title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.subtitle1
        )

        Text(
            modifier = Modifier.width(160.dp),
            text = book.authors.firstOrNull() ?: "",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.body2,
        )
    }
}


private val DEFAULT_BOOK = Book(
    id = 0,
    title = "Moby Dick",
    authors = listOf("Herman Meville"),
    imageUrl = "https://www.gutenberg.org/cache/epub/2641/pg2641.cover.medium.jpg"
)

@Preview
@Composable
private fun BookItemPreview() {
    BookItem(
        book = DEFAULT_BOOK
    )
}


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

