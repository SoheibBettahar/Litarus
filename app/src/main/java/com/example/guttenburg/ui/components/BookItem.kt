package com.example.guttenburg.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.guttenburg.data.repository.model.Book

private const val TAG = "BookItem"

private val DEFAULT_BOOK = Book(
    id = 0,
    title = "Moby Dick",
    authors = listOf("Herman Meville"),
    imageUrl = "https://www.gutenberg.org/cache/epub/2641/pg2641.cover.medium.jpg"
)

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


@Preview
@Composable
private fun BookItemPreview() {
    BookItem(
        book = DEFAULT_BOOK
    )
}