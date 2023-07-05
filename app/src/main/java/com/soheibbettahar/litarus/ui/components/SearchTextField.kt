package com.soheibbettahar.litarus.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.soheibbettahar.litarus.R
import com.soheibbettahar.litarus.ui.theme.LitarusTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchTextField(
    modifier: Modifier = Modifier,
    searchText: String,
    onTextChanged: (String) -> Unit = {},
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val isFocused = remember { mutableStateOf(false) }
    val borderColorAlpha: Float by animateFloatAsState(if (isFocused.value) 1.0f else 0.6f)
    val leadingIconTint =
        if (isFocused.value) MaterialTheme.colors.primary else MaterialTheme.colors.primary.copy(
            alpha = 0.5f
        )
    val isTrailingIconVisible = searchText.isNotBlank()


    OutlinedTextField(
        modifier = modifier
            .border(
                width = 2.dp,
                color = MaterialTheme.colors.primary.copy(alpha = borderColorAlpha),
                shape = RoundedCornerShape(24.dp)
            )
            .onFocusChanged { focusState -> isFocused.value = focusState.isFocused },
        value = searchText,
        onValueChange = onTextChanged,
        placeholder = {
            Text(
                text = "Search by Title, Author...", style = MaterialTheme.typography.body2,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.search),
                tint = leadingIconTint
            )
        },
        trailingIcon = {
            if (isTrailingIconVisible)
                Icon(
                    modifier = Modifier.clickable { onTextChanged("") },
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.clear_search_text_icon)
                )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Transparent, unfocusedBorderColor = Color.Transparent
        ),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions {
            keyboardController?.hide()
            focusManager.clearFocus()
        },
    )
}


@Preview
@Composable
private fun SearchTextFieldPreview() {
    LitarusTheme {
        SearchTextField(searchText = "")
    }
}

@Composable
fun ListAppbar(
    modifier: Modifier = Modifier,
    searchText: String,
    selectedLanguage: Language = Language("All", ""),
    onTextChanged: (String) -> Unit = {},
    onLanguageButtonClick: () -> Unit = {}
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        SearchTextField(
            modifier = Modifier.weight(1f),
            searchText = searchText,
            onTextChanged = onTextChanged
        )
        LanguageButton(
            isBadgeVisible = selectedLanguage != NoLanguageFilter,
            onClick = onLanguageButtonClick
        )
    }
}


@Preview
@Composable
private fun ListAppbarPreview() {
    LitarusTheme {
        ListAppbar(modifier = Modifier.fillMaxWidth(), searchText = "", onLanguageButtonClick = {})
    }
}

@Composable
fun LanguageButton(isBadgeVisible: Boolean = false, onClick: () -> Unit = {}) {
    IconButton(onClick = onClick) {
        Box {
            Icon(
                modifier = Modifier
                    .size(42.dp)
                    .padding(bottom = 6.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_language_letters),
                contentDescription = "Choose Language Button"
            )

            AnimatedVisibility(
                modifier = Modifier.align(Alignment.BottomEnd),
                visible = isBadgeVisible
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colors.secondary)
                )
            }


        }
    }
}

@Preview
@Composable
private fun LanguageButtonPreview() {
    LanguageButton()
}