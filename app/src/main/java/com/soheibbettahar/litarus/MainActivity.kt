package com.soheibbettahar.litarus

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.android.play.core.review.ReviewManagerFactory
import com.soheibbettahar.litarus.download.DownloadReceiver
import com.soheibbettahar.litarus.navigation.BooksListRoute
import com.soheibbettahar.litarus.navigation.bookDetailScreen
import com.soheibbettahar.litarus.navigation.booksListScreen
import com.soheibbettahar.litarus.navigation.navigateToBookDetail
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

                LitarusNavHost(
                    Modifier
                        .padding(contentPadding), navController = navController,
                    onShowSnackBar = { message ->
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                message
                            )
                        }
                    },
                    onShowSnackbarWithSettingsAction = { message, action ->
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
                    },
                    onShareBookClick = { id -> shareBookDownloadLink(context, id) },
                    onRequestAppReview = ::showReviewDialog
                )
            }

        }
    }


    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun LitarusNavHost(
        modifier: Modifier,
        navController: NavHostController,
        onShowSnackBar: (String) -> Unit = {},
        onShowSnackbarWithSettingsAction: (String, String) -> Unit,
        onShareBookClick: (Long) -> Unit,
        onRequestAppReview: () -> Unit = {}
    ) {

        AnimatedNavHost(
            modifier = modifier,
            navController = navController,
            startDestination = BooksListRoute
        ) {

            booksListScreen(
                ontNavigateToBookDetailScreen = { id, title, author ->
                    navController.navigateToBookDetail(
                        id,
                        title,
                        author
                    )
                },
                onShowSnackbar = onShowSnackBar
            )

            bookDetailScreen(
                onBackPress = { navController.navigateUp() },
                onShowSnackbar = onShowSnackBar,
                onShowSnackbarWithSettingsAction = onShowSnackbarWithSettingsAction,
                onShareBookClick = onShareBookClick,
                onRequestAppReview = onRequestAppReview
            )

        }
    }



    private fun showReviewDialog() {
        val reviewManager = ReviewManagerFactory.create(this)
        reviewManager.requestReviewFlow().addOnCompleteListener {
            if (it.isSuccessful)
                reviewManager.launchReviewFlow(this, it.result)
        }

    }

    private fun shareBookDownloadLink(context: Context, id: Long) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(
            Intent.EXTRA_TEXT, "$GUTTENBURG_URL${id}"
        )
        val chooser = Intent.createChooser(
            intent,
            context.getString(R.string.share_book)
        )
        context.startActivity(chooser)
    }

}




