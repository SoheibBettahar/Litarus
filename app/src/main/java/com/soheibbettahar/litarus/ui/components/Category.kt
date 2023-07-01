package com.soheibbettahar.litarus.ui.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Category(
    modifier: Modifier = Modifier,
    label: String,
    isSelected: Boolean = false,
    onCategoryClick: (String) -> Unit = {}
) {
    val transition =
        updateTransition(targetState = isSelected, label = "Category Selection Transition")

    val backgroundColor by transition.animateColor(label = "Background Color Animation") { isCurrentlySelected ->
        if (isCurrentlySelected) MaterialTheme.colors.onSurface else MaterialTheme.colors.surface
    }

    val textColor by transition.animateColor(label = "Text Color Animation") { isCurrentlySelected ->
        if (isCurrentlySelected) MaterialTheme.colors.surface else MaterialTheme.colors.onSurface
    }

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