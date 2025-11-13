package com.heqichang.dailymoney.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.heqichang.dailymoney.data.entity.Category

@Composable
fun CategoryList(
    categories: List<Category>,
    onSelectCategory: (Category) -> Unit,
    onEditCategory: (Category) -> Unit,
    onDeleteCategory: (Category) -> Unit
) {
    if (categories.isEmpty()) {
        EmptyCategoryState()
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.fillMaxSize(),
            contentPadding = 16.dp
        ) {
            items(categories) {
                CategoryItem(
                    category = it,
                    onSelect = onSelectCategory,
                    onEdit = onEditCategory,
                    onDelete = onDeleteCategory
                )
            }
        }
    }
}

@Composable
fun EmptyCategoryState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        androidx.compose.material3.Icon(
            imageVector = androidx.compose.material.icons.Icons.Default.Category,
            contentDescription = "空状态",
            tint = Color.Gray,
            modifier = Modifier.size(64.dp)
        )
        Text(
            text = "还没有分类",
            style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "点击右下角按钮添加分类",
            style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}