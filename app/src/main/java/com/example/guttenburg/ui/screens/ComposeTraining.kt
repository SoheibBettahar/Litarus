package com.example.guttenburg.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.*
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.guttenburg.ui.BooksViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlin.math.ceil


private const val TAG = "TrainingScreen"

@Composable
fun TrainingScreen(viewModel: BooksViewModel = hiltViewModel()) {

}
