package com.soheibbettahar.litarus.navigation

import androidx.navigation.NavHostController

 fun NavHostController.navigateSingleTopTo(route: String) = navigate(route) {
    saveState()
    restoreState = true
    launchSingleTop = true
}