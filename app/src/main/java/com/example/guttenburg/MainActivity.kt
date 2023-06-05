package com.example.guttenburg

import android.app.DownloadManager
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.guttenburg.Destination.*
import com.example.guttenburg.download.DownloadReceiver
import com.example.guttenburg.ui.screens.DetailScreen
import com.example.guttenburg.ui.screens.ListScreen
import com.example.guttenburg.ui.screens.ReaderScreen
import com.example.guttenburg.ui.screens.TrainingScreen
import com.example.guttenburg.ui.theme.GuttenburgTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MainActivity"


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var downloadReceiver: DownloadReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        registerReceiver(downloadReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        lifecycleScope

        setContent {
            GuttenburgApp()
        }
    }

    override fun onDestroy() {
        unregisterReceiver(downloadReceiver)
        super.onDestroy()
    }

    @Composable
    private fun GuttenburgApp() {
        GuttenburgTheme {
            // A surface container using
            // the 'background' color from the theme
            val snackbarHostState = remember { SnackbarHostState() }
            val coroutineScope = rememberCoroutineScope()


            Scaffold(
                modifier = Modifier
                    .fillMaxSize(),
                backgroundColor = MaterialTheme.colors.background,
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            ) { contentPadding ->
                val navController = rememberNavController()
                val systemUiController = rememberSystemUiController()
                val useDarkIcons = !isSystemInDarkTheme()


                LaunchedEffect(systemUiController, useDarkIcons) {
                    systemUiController.setSystemBarsColor(
                        Color.Transparent,
                        darkIcons = useDarkIcons
                    )
                }


                GuttenburgNavHost(
                    Modifier
                        .padding(contentPadding)
                        .safeDrawingPadding(), navController = navController,
                    onShowSnackBar = { message ->
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                message
                            )
                        }
                    })
            }
        }
    }


    @Composable
    fun GuttenburgNavHost(
        modifier: Modifier,
        navController: NavHostController,
        onShowSnackBar: (String) -> Unit = {}
    ) {
        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = BooksList.route
        ) {
            composable(Training.route) { TrainingScreen() }

            composable(BooksList.route) {
                ListScreen { id, title, author ->
                    val route = "${BookDetail.route}/$id/$title?author={$author}"
                    navController.navigateSingleTopTo(route)
                }
            }

            composable(
                route = "${BookDetail.route}/{id}/{title}?author={author}",
                arguments = listOf(navArgument("id") { type = NavType.LongType },
                    navArgument("title") { type = NavType.StringType },
                    navArgument("author") { type = NavType.StringType; nullable = true })
            ) {
                DetailScreen(
                    onBackPress = { navController.navigateUp() },
                    onStartReadingPress = {bookId ->
                        Log.d(TAG, "GuttenburgNavHost: bookId: $bookId")
                        val route = "${BookReader.route}/$bookId"
                        navController.navigateSingleTopTo(route)
                    },
                    onShowSnackbar = onShowSnackBar
                )
            }

            composable(
                route = "${BookReader.route}/{bookId}",
                arguments = listOf(navArgument("bookId") { type = NavType.LongType })
            ) {
                ReaderScreen(onBackPress = { navController.navigateUp()})
            }

        }
    }


    private fun NavHostController.navigateSingleTopTo(route: String) = navigate(route) {
        saveState()
        restoreState = true
        launchSingleTop = true
    }

}




