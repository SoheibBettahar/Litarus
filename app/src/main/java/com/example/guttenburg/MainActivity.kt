package com.example.guttenburg

import android.app.DownloadManager
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
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
import com.example.guttenburg.ui.screens.TrainingScreen
import com.example.guttenburg.ui.theme.GuttenburgTheme
import com.example.guttenburg.util.GUTTENBURG_URL
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

            Box(
                modifier = Modifier
                    .background(MaterialTheme.colors.background)
                    .fillMaxSize()
            ) {
                // A surface container using
                // the 'background' color from the theme
                val snackbarHostState = remember { SnackbarHostState() }
                val coroutineScope = rememberCoroutineScope()
                val context = LocalContext.current

                Scaffold(
                    modifier = Modifier.fillMaxSize().safeDrawingPadding(),
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
                            .padding(contentPadding), navController = navController,
                        onShowSnackBar = { message ->
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    message
                                )
                            }
                        }, onShowSnackbarWithSettingsAction = { message, action ->
                            coroutineScope.launch {
                                val result = snackbarHostState.showSnackbar(message, action)
                                Log.d(TAG, "GuttenburgApp: $result")

                                if (result == SnackbarResult.ActionPerformed) {
                                    Log.d(TAG, "GuttenburgApp: ActionPerformed")
                                    val intent = Intent(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.fromParts("package", context.packageName, null)
                                    )
                                    startActivity(intent)
                                }
                            }
                        })
                }
            }
        }
    }


    @Composable
    fun GuttenburgNavHost(
        modifier: Modifier,
        navController: NavHostController,
        onShowSnackBar: (String) -> Unit = {},
        onShowSnackbarWithSettingsAction: (String, String) -> Unit
    ) {
        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = BooksList.route
        ) {
            composable(Training.route) { TrainingScreen() }

            composable(BooksList.route) {
                ListScreen(onBookItemClick = { id, title, author ->
                    val route = "${BookDetail.route}/$id/$title?author={$author}"
                    navController.navigateSingleTopTo(route)
                }, onShowSnackbar = onShowSnackBar)
            }

            composable(
                route = "${BookDetail.route}/{id}/{title}?author={author}",
                arguments = listOf(navArgument("id") { type = NavType.LongType },
                    navArgument("title") { type = NavType.StringType },
                    navArgument("author") { type = NavType.StringType; nullable = true })
            ) {
                val context = LocalContext.current
                DetailScreen(
                    onBackPress = { navController.navigateUp() },
                    onShowSnackbar = onShowSnackBar,
                    onShowSnackbarWithSettingsAction = onShowSnackbarWithSettingsAction,
                    onSharePress = { id ->
                        val intent = Intent(Intent.ACTION_SEND)
                        intent.type = "text/plain"
                        intent.putExtra(
                            Intent.EXTRA_TEXT, "$GUTTENBURG_URL${id}"
                        )
                        val chooser = Intent.createChooser(
                            intent, context.getString(com.example.guttenburg.R.string.share_book)
                        )
                        context.startActivity(chooser)

                    }
                )
            }
        }
    }


    private fun NavHostController.navigateSingleTopTo(route: String) = navigate(route) {
        saveState()
        restoreState = true
        launchSingleTop = true
    }

}




