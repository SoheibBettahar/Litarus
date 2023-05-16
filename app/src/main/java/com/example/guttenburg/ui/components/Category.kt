package com.example.guttenburg.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


val DEFAULT_CATEGORIES = mapOf(
    "All" to "",
    "Adventure" to "Adventure",
    "Biography" to "Biography",
    "Children" to "Children",
    "Detective" to "Detective",
    "Drama" to "Drama",
    "Fantasy" to "Fantasy",
    "Fiction" to "Fiction",
    "History" to "History",
    "Horror" to "Horror",
    "Humor" to "Humor",
    "Literature" to "Literature",
    "Mystery" to "Mystery",
    "Romance" to "Romance",
    "Science" to "Science",
    "Thriller" to "Thriller",
    "Poetry" to "Poetry",
)


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Category(
    modifier: Modifier = Modifier,
    label: String,
    isSelected: Boolean = false,
    onCategoryClick: (String) -> Unit = {}
) {

    val backgroundColor =
        if (isSelected) MaterialTheme.colors.onSurface else MaterialTheme.colors.surface
    val textColor = if (isSelected) MaterialTheme.colors.surface else MaterialTheme.colors.onSurface

    Chip(
        modifier = modifier,
        colors = ChipDefaults.chipColors(backgroundColor = backgroundColor),
        onClick = { onCategoryClick(label) }) {
        Text(
            modifier = Modifier.padding(vertical = 14.dp, horizontal = 12.dp),
            text = label,
            style = MaterialTheme.typography.body2,
            textAlign = TextAlign.Center,
            color = textColor
        )
    }
}


@Preview
@Composable
fun CategoryPreview() {
    Category(label = "Detective", isSelected = false)
}