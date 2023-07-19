package com.soheibbettahar.litarus.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.soheibbettahar.litarus.R
import com.soheibbettahar.litarus.ui.theme.LitarusTheme

@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    title: String = "Title",
    onBackPress: () -> Unit = {},
    onSharePress: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                vertical = 20.dp,
                horizontal = 16.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        AppbarButton(
            icon = Icons.Default.ArrowBack,
            onClick = onBackPress,
            contentDescription = stringResource(R.string.back_button)
        )

        Text(text = title, style = MaterialTheme.typography.body1)


        AppbarButton(
            icon = Icons.Default.Share,
            contentDescription = stringResource(R.string.share_button),
            onClick = onSharePress
        )
    }
}

@Preview
@Composable
fun AppBarPreview(){
    AppBar()
}


@Composable
fun AppbarButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    onClick: () -> Unit = {},
    contentDescription: String
) {
    IconButton(
        modifier = modifier
            .clip(CircleShape),
        onClick = onClick
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
        )
    }
}


@Preview
@Composable
fun AppbarButtonPreview() {
    LitarusTheme() {
        AppbarButton(icon = Icons.Default.ArrowBack, contentDescription = "")
    }
}