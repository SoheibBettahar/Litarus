package com.example.guttenburg

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.guttenburg.Destination.BookDetail
import com.example.guttenburg.Destination.BooksList
import com.example.guttenburg.Destination.Training
import com.example.guttenburg.ui.screens.DetailScreen
import com.example.guttenburg.ui.screens.ListScreen
import com.example.guttenburg.ui.screens.TrainingScreen
import com.example.guttenburg.ui.theme.GuttenburgTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            GuttenburgApp()
        }
    }

    @Composable
    private fun GuttenburgApp() {
        GuttenburgTheme {
            // A surface container using
            // the 'background' color from the theme
            Surface(
                modifier = Modifier
                    .fillMaxSize(),
                color = MaterialTheme.colors.background
            ) {
                val navController = rememberNavController()
                val systemUiController = rememberSystemUiController()
                val useDarkIcons = !isSystemInDarkTheme()


                LaunchedEffect(systemUiController, useDarkIcons) {
                    systemUiController.setSystemBarsColor(
                        Color.Transparent,
                        darkIcons = useDarkIcons
                    )
                }


                GuttenburgNavHost(Modifier.safeDrawingPadding(), navController = navController)
            }
        }
    }


    @Composable
    fun GuttenburgNavHost(modifier: Modifier, navController: NavHostController) {
        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = BooksList.route
        ) {

            composable(Training.route) { TrainingScreen() }

            composable(BooksList.route) {
                ListScreen { id, title, author ->
                    val route = "${BookDetail.route}/$id/$title/$author"
                    Log.d(TAG, "GuttenburgNavHost: $route")
                    navController.navigateSingleTopTo(route)
                }
            }

            composable(
                route = "${BookDetail.route}/{id}/{title}/{author}",
                arguments = listOf(navArgument("id") { type = NavType.LongType },
                    navArgument("title") { type = NavType.StringType },
                    navArgument("author") { type = NavType.StringType })
            ) { backStackEntry ->
                DetailScreen(
                    id = backStackEntry.arguments?.getLong("id") ?: -1,
                    title = backStackEntry.arguments?.getString("title") ?: "",
                    author = backStackEntry.arguments?.getString("author") ?: ""
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




