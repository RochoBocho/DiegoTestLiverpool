package com.example.diegorochintest.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.diegorochintest.ui.screens.SearchScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.SEARCH_SCREEN,
    ) {
        composable(route = NavigationRoutes.SEARCH_SCREEN) {
            SearchScreen()
        }
    }
}
