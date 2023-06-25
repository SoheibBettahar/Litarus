package com.example.guttenburg.ui.components

import android.os.Parcelable
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.guttenburg.R
import com.example.guttenburg.ui.theme.GuttenburgTheme
import kotlinx.parcelize.Parcelize

val languages = mapOf(
    "All" to "",
    //Languages that have more than 50 books
    "Chinese" to "zh",
    "Danish" to "da",
    "Dutch" to "nl",
    "English" to "en",
    "Esperanto" to "eo",
    "Finnish" to "fi",
    "French" to "fr",
    "German" to "de",
    "Greek" to "el",
    "Hungarian" to "hu",
    "Italian" to "it",
    "Latin" to "la",
    "Portuguese" to "pt",
    "Spanish" to "es",
    "Swedish" to "sv",
    "Tagalog" to "tl",
    //Languages that have up to 50 books
    "Afrikaans" to "af",
    "Aleut" to "ale",
    "Arabic" to "ar",
    "Arapaho" to "arp",
    "Bodo" to "brx",
    "Breton" to "br",
    "Bulgarian" to "bg",
    "Caló" to "rmr",
    "Catalan" to "ca",
    "Cebuano" to "ceb",
    "Czech" to "cs",
    "Estonian" to "et",
    "Farsi" to "fa",
    "Frisian" to "fy",
    "Friulian" to "fur",
    "Gaelic, Scottish" to "gd",
    "Galician" to "gl",
    "Gamilaraay" to "kld",
    "Greek Ancient" to "grc",
    "Hebrew" to "he",
    "Icelandic" to "is",
    "Iloko" to "ilo",
    "Interlingua" to "ia",
    "Inuktitut" to "iu",
    "Irish" to "ga",
    "Japanese" to "ja",
    "Kashubian" to "csb",
    "Khasi" to "kha",
    "Korean" to "ko",
    "Lithuanian" to "lt",
    "Maori" to "mi",
    "Mayan" to "myn",
    "Middle English" to "enm",
    "Nahuatl" to "nah",
    "Napoletano-Calabrese" to "nap",
    "Navajo" to "nav",
    "North American Indian" to "nai",
    "Norwegian" to "no",
    "Occitan" to "oc",
    "Ojibwa" to "oji",
    "Old English" to "ang",
    "Polish" to "pl",
    "Romanian" to "ro",
    "Russian" to "ru",
    "Sanskrit" to "sa",
    "Serbian" to "sr",
    "Slovenian" to "sl",
    "Tagabawa" to "bgs",
    "Telugu" to "te",
    "Tibetan" to "bo",
    "Welsh" to "cy",
    "Yiddish" to "yi",
)
val NoLanguageFilter = Language("All", "")

@Parcelize
data class Language(val label: String, val code: String) : Parcelable


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LanguageDialog(
    modifier: Modifier = Modifier,
    selectedLanguage: Language = NoLanguageFilter,
    onItemClick: (Language) -> Unit = {},
    dismiss: () -> Unit = {}
) {

    val keys = remember { languages.keys.toList() }

    Dialog(onDismissRequest = dismiss) {

        Box(
            modifier = modifier
                .clip(MaterialTheme.shapes.medium)
                .fillMaxWidth()
                .height(400.dp)
                .background(MaterialTheme.colors.surface)
        ) {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(minSize = 140.dp),
                contentPadding = PaddingValues(16.dp),
            ) {

                item(span = StaggeredGridItemSpan.FullLine) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.select_language),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.subtitle1,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                }

                items(count = languages.size) {
                    val label: String = keys[it]
                    val code: String = languages[label]!!
                    val data = Language(label, code)
                    LanguageItem(
                        language = data,
                        isSelected = selectedLanguage.label == label,
                        onClick = { language ->
                            onItemClick(language)
                            dismiss()
                        }
                    )
                }

                item(span = StaggeredGridItemSpan.FullLine) { Spacer(modifier = Modifier.size(12.dp)) }
            }
        }

    }

}

@Composable
fun LanguageItem(
    modifier: Modifier = Modifier,
    language: Language,
    isSelected: Boolean,
    onClick: (Language) -> Unit
) {
    val backgroundColor = if (isSelected) MaterialTheme.colors.secondary.copy(alpha = 0.1f)
    else Color.Transparent

    TextButton(
        modifier = modifier,
        border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colors.secondary) else null,
        contentPadding = PaddingValues(vertical = 10.dp, horizontal = 4.dp),
        onClick = { onClick(language) },
        colors = ButtonDefaults.textButtonColors(backgroundColor = backgroundColor)
    ) {
        Text(
            text = language.label,
            fontStyle = MaterialTheme.typography.body1.fontStyle,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
        )
    }
}


@Preview
@Composable
private fun LanguageDialogPreview() {
    GuttenburgTheme() {
        LanguageDialog()
    }
}