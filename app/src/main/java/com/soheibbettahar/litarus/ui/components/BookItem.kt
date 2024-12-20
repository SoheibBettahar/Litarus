package com.soheibbettahar.litarus.ui.components

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
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.material.placeholder
import com.soheibbettahar.litarus.data.repository.model.Book
import com.soheibbettahar.litarus.util.DEFAULT_BOOK

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


@Preview
@Composable
private fun BookItemPreview() {
    BookItem(
        book = DEFAULT_BOOK
    )
}


@Composable
fun BookItemPlaceHolder(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .wrapContentSize()
    ) {

        Box(
            modifier = Modifier
                .placeholder(true, highlight = PlaceholderHighlight.fade())
                .size(height = 235.dp, width = 160.dp)
                .clip(MaterialTheme.shapes.medium),
        )

        Spacer(modifier = Modifier.height(14.dp))

        Box(
            modifier = Modifier
                .size(160.dp, height = 20.dp)
                .placeholder(true, highlight = PlaceholderHighlight.fade())
        )

        Spacer(modifier = Modifier.height(2.dp))

        Box(
            modifier = Modifier
                .size(100.dp, height = 18.dp)
                .placeholder(true, highlight = PlaceholderHighlight.fade())
        )
    }
}

@Preview
@Composable
fun BookItemPlaceHolderPreview() {
    BookItemPlaceHolder()
}