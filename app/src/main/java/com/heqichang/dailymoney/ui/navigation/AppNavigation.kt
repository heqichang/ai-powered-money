package com.heqichang.dailymoney.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.heqichang.dailymoney.ui.screen.HomeScreen
import com.heqichang.dailymoney.ui.screen.AccountBookScreen
import com.heqichang.dailymoney.ui.screen.CategoryScreen
import com.heqichang.dailymoney.ui.screen.StatisticsScreen

// 导航路由定义
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AccountBook : Screen("account_book")
    object Category : Screen("category")
    object Statistics : Screen("statistics")
}

@Composable
fun AppNavigation(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(Screen.AccountBook.route) {
            AccountBookScreen(navController = navController)
        }
        composable(Screen.Category.route) {
            CategoryScreen(navController = navController)
        }
        composable(Screen.Statistics.route) {
            StatisticsScreen(navController = navController)
        }
    }
}