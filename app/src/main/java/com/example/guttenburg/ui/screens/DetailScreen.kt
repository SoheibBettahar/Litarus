package com.example.guttenburg.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.guttenburg.R
import com.example.guttenburg.data.Result
import com.example.guttenburg.data.repository.BookWithExtras
import com.example.guttenburg.ui.BooksViewModel
import com.example.guttenburg.ui.components.BookCover
import com.example.guttenburg.ui.theme.GuttenburgTheme
import com.example.guttenburg.ui.util.withFadingEdgeEffect

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
    texPlain = "https://www.gutenberg.org/ebooks/145.txt.utf-8",
    imageUrl = "https://www.gutenberg.org/cache/epub/145/pg145.cover.medium.jpg",
    downloadCount = 130471,
    language = "English",
    authors = "Eliot, George"
)


@Composable
fun DetailScreen(
    viewModel: BooksViewModel = hiltViewModel(), id: Long, title: String, author: String
) {
    val book = viewModel.book.collectAsState()
    val result = book.value
    LaunchedEffect(key1 = Unit) {
        viewModel.getBook(id, title, author)
    }

    Column(modifier = Modifier.fillMaxSize()) {

        AppBar(modifier = Modifier.fillMaxWidth())

        Box(modifier = Modifier.fillMaxSize()) {

            if (result is Result.Success) {
                BookDetails(modifier = Modifier.fillMaxSize(), result.data)
            }


            if (result is Result.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(
                        Alignment.Center
                    )
                )
            }

            if (result is Result.Error) Text(
                modifier = Modifier.align(Alignment.Center),
                text = "Error ${result.exception}",
                textAlign = TextAlign.Center
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

        CircularButton(
            icon = Icons.Default.ArrowBack,
            onClick = onBackPress,
            contentDescription = "Back Button"
        )

        Text(text = "Details", style = MaterialTheme.typography.body1)


        CircularButton(icon = Icons.Default.MoreVert, contentDescription = "More Button")
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
fun BookDetails(modifier: Modifier = Modifier, book: BookWithExtras) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .withFadingEdgeEffect()
            .verticalScroll(rememberScrollState(), enabled = true),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(24.dp))

        BookCover(
            modifier = Modifier.size(height = 260.dp, width = 180.dp), imageUrl = book.imageUrl
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

        Spacer(modifier = Modifier.height(8.dp))


        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = book.authors,
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(18.dp))

        StartReadingButton(modifier = Modifier.padding(horizontal = 16.dp))

        Spacer(modifier = Modifier.height(18.dp))







        if (!book.description.isNullOrBlank()) {
            Synopsis(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                description = book.description
            )
        }

    }

}

@Composable
fun Synopsis(modifier: Modifier = Modifier, description: String) {
    Text(modifier = modifier, text = description, style = MaterialTheme.typography.body2)
}

@Composable
private fun StartReadingButton(modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Button(
        modifier = modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.secondary
        ), onClick = onClick,
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(text = "Start Reading", style = MaterialTheme.typography.body1)
    }
}

@Preview
@Composable
fun BookDetailsPreview() {
    GuttenburgTheme() {
        BookDetails(modifier = Modifier.fillMaxSize(), book = DEFAULT_BOOK_WITH_EXTRAS)
    }
}

@Composable
fun CircularButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    onClick: () -> Unit = {},
    contentDescription: String
) {
    IconButton(
        modifier = modifier
            .clip(CircleShape)
            .background(MaterialTheme.colors.primary),
        onClick = onClick
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = MaterialTheme.colors.onPrimary
        )
    }
}


@Preview
@Composable
fun CircularButtonPreview() {
    GuttenburgTheme() {
        CircularButton(icon = Icons.Default.ArrowBack, contentDescription = "")
    }
}

@Composable
fun BookInfo(modifier: Modifier = Modifier, book: BookWithExtras) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        //language, downloadCount, pageCount, avrage rating
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
    GuttenburgTheme() {
        InfoCell(text = "English", icon = Icons.Default.Star)
    }
}





