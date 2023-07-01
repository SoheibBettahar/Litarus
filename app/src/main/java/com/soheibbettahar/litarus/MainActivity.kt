package com.soheibbettahar.litarus

import android.app.DownloadManager
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.soheibbettahar.litarus.Destination.*
import com.soheibbettahar.litarus.download.DownloadReceiver
import com.soheibbettahar.litarus.ui.screens.DetailScreen
import com.soheibbettahar.litarus.ui.screens.ListScreen
import com.soheibbettahar.litarus.ui.theme.LitarusTheme
import com.soheibbettahar.litarus.util.GUTTENBURG_URL
import com.soheibbettahar.litarus.util.analytics.AnalyticsHelper
import com.soheibbettahar.litarus.util.analytics.LocalAnalyticsHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MainActivity"


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var downloadReceiver: DownloadReceiver

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        registerReceiver(downloadReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        setContent {
            CompositionLocalProvider(LocalAnalyticsHelper provides analyticsHelper) {
                LitarusApp()
            }

        }
    }

    override fun onDestroy() {
        unregisterReceiver(downloadReceiver)
        super.onDestroy()
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    private fun LitarusApp() {
        LitarusTheme {
            val snackbarHostState = remember { SnackbarHostState() }
            val coroutineScope = rememberCoroutineScope()
            val context = LocalContext.current

            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .safeDrawingPadding(),
                backgroundColor = MaterialTheme.colors.background,
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            ) { contentPadding ->
                val navController = rememberAnimatedNavController()

                //TODO: Bug, when oppening the app, systemBarColor is always light
                val systemUiController = rememberSystemUiController()
                val useDarkIcons = !isSystemInDarkTheme()

                LaunchedEffect(systemUiController, useDarkIcons) {
                    systemUiController.setSystemBarsColor(
                        Color.Transparent,
                        darkIcons = useDarkIcons
                    )
                }


                LitarusNavHost(
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

                            if (result == SnackbarResult.ActionPerformed) {
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


    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun LitarusNavHost(
        modifier: Modifier,
        navController: NavHostController,
        onShowSnackBar: (String) -> Unit = {},
        onShowSnackbarWithSettingsAction: (String, String) -> Unit
    ) {
        AnimatedNavHost(
            modifier = modifier,
            navController = navController,
            startDestination = BooksList.route
        ) {
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
                            intent,
                            context.getString(com.soheibbettahar.litarus.R.string.share_book)
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




