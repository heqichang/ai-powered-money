package com.heqichang.dailymoney.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.heqichang.dailymoney.data.AppContainer
import com.heqichang.dailymoney.data.entity.AccountBook
import com.heqichang.dailymoney.ui.component.AccountBookDialog
import com.heqichang.dailymoney.ui.component.AccountBookList
import com.heqichang.dailymoney.ui.navigation.Screen
import com.heqichang.dailymoney.ui.viewmodel.AccountBookViewModel

@Composable
fun AccountBookScreen(navController: NavController) {
    val context = LocalContext.current
    val appContainer = remember { AppContainer(context) }
    val viewModel: AccountBookViewModel = viewModel(
        factory = AccountBookViewModel.Factory(appContainer.accountBookRepository)
    )
    
    val accountBooks by viewModel.allAccountBooks.collectAsState(emptyList())
    val selectedBook by viewModel.selectedAccountBook.collectAsState()
    
    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var editingBook by remember { mutableStateOf<AccountBook?>(null) }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    var bookToDelete by remember { mutableStateOf<AccountBook?>(null) }
    
    // 处理账本选择
    fun handleBookSelect(book: AccountBook) {
        viewModel.selectAccountBook(book)
        // 选择后返回首页
        navController.popBackStack()
    }
    
    // 处理添加账本
    fun handleAddBook() {
        showAddDialog = true
    }
    
    // 处理编辑账本
    fun handleEditBook(book: AccountBook) {
        editingBook = book
        showEditDialog = true
    }
    
    // 处理删除账本确认
    fun handleDeleteBook(book: AccountBook) {
        bookToDelete = book
        showDeleteConfirm = true
    }
    
    // 确认删除账本
    fun confirmDeleteBook() {
        bookToDelete?.let {
            viewModel.deleteAccountBook(it)
        }
        showDeleteConfirm = false
        bookToDelete = null
    }
    
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { handleAddBook() }) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.Add,
                    contentDescription = "添加账本"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // 标题
            Text(
                text = "账本管理",
                style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )
            
            // 账本列表
            AccountBookList(
                accountBooks = accountBooks,
                selectedBookId = selectedBook?.id,
                onSelectBook = ::handleBookSelect,
                onEditBook = ::handleEditBook,
                onDeleteBook = ::handleDeleteBook
            )
        }
    }
    
    // 添加账本对话框
    if (showAddDialog) {
        AccountBookDialog(
            onDismiss = { showAddDialog = false },
            onSave = { book -> viewModel.insertAccountBook(book) }
        )
    }
    
    // 编辑账本对话框
    if (showEditDialog && editingBook != null) {
        AccountBookDialog(
            accountBook = editingBook,
            onDismiss = { 
                showEditDialog = false 
                editingBook = null
            },
            onSave = { book -> viewModel.updateAccountBook(book) }
        )
    }
    
    // 删除确认对话框
    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text(text = "删除账本") },
            text = { Text(text = "确定要删除此账本吗？删除后相关的记账记录也会被删除。") },
            confirmButton = {
                TextButton(onClick = ::confirmDeleteBook) {
                    Text(text = "删除")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text(text = "取消")
                }
            }
        )
    }
}