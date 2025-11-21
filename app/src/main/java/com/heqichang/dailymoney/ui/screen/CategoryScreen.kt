package com.heqichang.dailymoney.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.heqichang.dailymoney.R
import com.heqichang.dailymoney.data.entity.Category
import com.heqichang.dailymoney.ui.component.CategoryDialog
import com.heqichang.dailymoney.ui.component.CategoryList
import com.heqichang.dailymoney.ui.component.SegmentedButtons
import com.heqichang.dailymoney.ui.viewmodel.CategoryViewModel

// 分类类型枚举
enum class CategoryType {
    EXPENSE, INCOME
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: CategoryViewModel = viewModel()
    var selectedType by remember { mutableStateOf(CategoryType.EXPENSE) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    
    // 收集ViewModel中的数据
    val expenseCategories by viewModel.expenseCategories.collectAsStateWithLifecycle()
    val incomeCategories by viewModel.incomeCategories.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    
    // 根据选中的类型获取分类
    val categories = if (selectedType == CategoryType.EXPENSE) {
        expenseCategories
    } else {
        incomeCategories
    }
    
    // 处理错误显示
    if (error != null) {
        androidx.compose.material3.SnackbarHostState().showSnackbar(
            message = error ?: "",
            withDismissAction = true
        )
    }
    
    // 添加分类
    fun handleAddCategory() {
        selectedCategory = null
        showDialog = true
    }
    
    // 编辑分类
    fun handleEditCategory(category: Category) {
        selectedCategory = category
        showDialog = true
    }
    
    // 删除分类
    fun handleDeleteCategory(category: Category) {
        viewModel.deleteCategory(category)
    }
    
    // 保存分类
    fun handleSaveCategory(category: Category) {
        if (selectedCategory == null) {
            // 添加新分类
            viewModel.createCategory(
                name = category.name,
                color = category.color,
                isExpense = selectedType == CategoryType.EXPENSE
            )
        } else {
            // 更新分类
            viewModel.updateCategory(category)
        }
        showDialog = false
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.category_management)) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { handleAddCategory() },
                containerColor = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.medium
            ) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.Add,
                    contentDescription = "添加分类"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 分类类型选择器
            SegmentedButtons(
                selectedType = selectedType,
                onSelectionChange = { selectedType = it }
            )
            
            // 分类列表
            CategoryList(
                categories = categories,
                onSelectCategory = { /* 选中分类的处理 */ },
                onEditCategory = { handleEditCategory(it) },
                onDeleteCategory = { handleDeleteCategory(it) }
            )
            
            // 加载指示器
            if (isLoading) {
                androidx.compose.material3.CircularProgressIndicator(
                    modifier = Modifier
                        .align(androidx.compose.ui.Alignment.CenterHorizontally)
                        .padding(16.dp)
                )
            }
        }
    }
    
    // 分类编辑对话框
    if (showDialog) {
        CategoryDialog(
            category = selectedCategory,
            onDismiss = { showDialog = false },
            onSave = { handleSaveCategory(it) }
        )
    }
}