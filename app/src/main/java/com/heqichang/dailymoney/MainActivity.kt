package com.heqichang.dailymoney

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.heqichang.dailymoney.ui.component.MainContent
import com.heqichang.dailymoney.ui.theme.DailyMoneyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DailyMoneyTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    MainContent(navController = navController)
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    DailyMoneyTheme {
        MainScreen()
    }
}