package com.heqichang.dailymoney.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.heqichang.dailymoney.data.entity.Category

@Composable
fun CategorySelector(
    categories: List<Category>,
    selectedCategory: Category? = null,
    onCategorySelected: (Category) -> Unit,
    onAddNew: () -> Unit
) {
    var showCategoryList by remember { mutableStateOf(false) }
    
    Column(modifier = Modifier.fillMaxWidth()) {
        // 已选择的分类显示
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    showCategoryList = !showCategoryList
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (selectedCategory != null) {
                    CategoryIcon(
                        category = selectedCategory,
                        size = 40.dp
                    )
                    Text(
                        text = selectedCategory.name,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                } else {
                    Text(
                        text = "选择分类",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.ArrowDropDown,
                    contentDescription = "展开",
                    modifier = Modifier.weight(1f),
                    tint = Color.Gray
                )
            }
        }
        
        // 分类列表弹窗
        if (showCategoryList) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .clip(RoundedCornerShape(bottomEnd = 12.dp, bottomStart = 12.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // 分类网格
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        modifier = Modifier.height(240.dp)
                    ) {
                        items(categories) {
                            CategoryItem(
                                category = it,
                                isSelected = it.id == selectedCategory?.id,
                                onSelect = { category ->
                                    onCategorySelected(category)
                                    showCategoryList = false
                                }
                            )
                        }
                    }
                    
                    // 添加新分类按钮
                    TextButton(
                        onClick = {
                            showCategoryList = false
                            onAddNew()
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = androidx.compose.material.icons.Icons.Default.Add,
                                contentDescription = "添加",
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "添加新分类",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryItem(
    category: Category,
    isSelected: Boolean,
    onSelect: (Category) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onSelect(category) },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CategoryIcon(
            category = category,
            size = 48.dp,
            isSelected = isSelected
        )
        Text(
            text = category.name,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp),
            maxLines = 1
        )
    }
}

@Composable
fun CategoryIcon(
    category: Category,
    size: androidx.compose.ui.geometry.Size,
    isSelected: Boolean = false
) {
    val backgroundColor = Color(category.color ?: 0xFFE0E0E0)
    
    Box(
        modifier = Modifier
            .size(size)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(8.dp)
            )
            .then(
                if (isSelected) {
                    Modifier.border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(8.dp)
                    )
                } else {
                    Modifier
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = androidx.compose.material.icons.Icons.Default.Category,
            contentDescription = category.name,
            tint = Color.White,
            modifier = Modifier.size(size * 0.5f)
        )
    }
}