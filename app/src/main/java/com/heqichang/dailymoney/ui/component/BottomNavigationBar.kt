package com.heqichang.dailymoney.ui.component

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.heqichang.dailymoney.R
import com.heqichang.dailymoney.ui.navigation.Screen

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        NavigationItem(
            screen = Screen.Home,
            label = "首页",
            icon = R.drawable.ic_home // 稍后添加这些图标
        ),
        NavigationItem(
            screen = Screen.Statistics,
            label = "统计",
            icon = R.drawable.ic_statistics
        ),
        NavigationItem(
            screen = Screen.Category,
            label = "分类",
            icon = R.drawable.ic_category
        ),
        NavigationItem(
            screen = Screen.AccountBook,
            label = "账本",
            icon = R.drawable.ic_account_book
        )
    )
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    BottomAppBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = { 
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Default.Home, // 临时使用默认图标
                        contentDescription = item.label
                    ) 
                },
                label = { Text(text = item.label) },
                selected = currentRoute == item.screen.route,
                onClick = {
                    navController.navigate(item.screen.route) {
                        // 避免在栈顶重复添加相同的目的地
                        launchSingleTop = true
                        // 清除导航栈中当前目的地之后的所有目的地
                        popUpTo(Screen.Home.route) {
                            saveState = true
                        }
                        // 恢复保存的状态（如果有）
                        restoreState = true
                    }
                }
            )
        }
    }
}

// 导航项数据类
data class NavigationItem(
    val screen: Screen,
    val label: String,
    val icon: Int
)