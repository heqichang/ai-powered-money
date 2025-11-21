package com.heqichang.dailymoney.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.heqichang.dailymoney.data.AppContainer
import com.heqichang.dailymoney.ui.component.AccountBookSelector
import com.heqichang.dailymoney.ui.viewmodel.AccountBookViewModel

@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val appContainer = remember { AppContainer(context) }
    val viewModel = viewModel(
        factory = AccountBookViewModel.Factory(appContainer.accountBookRepository)
    )
    
    val accountBooks by viewModel.allAccountBooks.collectAsState(emptyList())
    val selectedBook by viewModel.selectedAccountBook.collectAsState()
    
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { /* 打开添加交易记录页面 */ }) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.Add,
                    contentDescription = "添加记录"
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            // 顶部账本选择栏
            AccountBookSelector(
                navController = navController,
                accountBooks = accountBooks,
                selectedBook = selectedBook,
                onBookSelected = { viewModel.selectAccountBook(it) }
            )
            
            // 日期筛选栏
            DateFilterBar()
            
            // 交易记录列表
            TransactionList()
        }
    }
}

@Composable
fun DateFilterBar() {
    // 这里将实现日期筛选栏，暂时显示占位符
    Text(text = "日期筛选: 全部时间")
}

@Composable
fun TransactionList() {
    // 这里将实现交易记录列表，暂时显示占位符
    Text(text = "交易记录列表")
}