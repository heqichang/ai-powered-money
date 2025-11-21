package com.heqichang.dailymoney.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.heqichang.dailymoney.data.entity.Category

@Composable
fun CategoryDialog(
    category: Category? = null,
    onDismiss: () -> Unit,
    onSave: (Category) -> Unit
) {
    // 预设的分类颜色选项
    val colorOptions = listOf(
        Color(0xFF4CAF50), // 绿色
        Color(0xFF2196F3), // 蓝色
        Color(0xFFF44336), // 红色
        Color(0xFFFF9800), // 橙色
        Color(0xFF9C27B0), // 紫色
        Color(0xFF00BCD4), // 青色
        Color(0xFFE91E63), // 粉红色
        Color(0xFF795548)  // 棕色
    )
    
    var name by remember { mutableStateOf(category?.name ?: "") }
    var isExpense by remember { mutableStateOf(category?.isExpense ?: true) }
    var selectedColor by remember { mutableStateOf(category?.color ?: colorOptions[0].value.toLong()) }
    var nameError by remember { mutableStateOf(false) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = if (category == null) "新建分类" else "编辑分类")
        },
        text = {
            Column {
                // 分类名称输入
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        nameError = false
                    },
                    label = { Text(text = "分类名称") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = nameError,
                )
                
                // 分类类型选择
                Text(
                    text = "分类类型",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                        RadioButton(
                            selected = isExpense,
                            onClick = { isExpense = true },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = MaterialTheme.colorScheme.primary
                            )
                        )
                        Text(text = "支出")
                    }
                    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                        RadioButton(
                            selected = !isExpense,
                            onClick = { isExpense = false },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = MaterialTheme.colorScheme.primary
                            )
                        )
                        Text(text = "收入")
                    }
                }
                
                // 颜色选择器
                Text(
                    text = "选择颜色",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    colorOptions.forEach { color ->
                        ColorOption(
                            color = color,
                            isSelected = color.value.toLong() == selectedColor,
                            onClick = { selectedColor = color.value.toLong() }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isBlank()) {
                        nameError = true
                        return@Button
                    }
                    
                    val newCategory = category?.copy(
                        name = name,
                        isExpense = isExpense,
                        color = selectedColor
                    ) ?: Category(
                        name = name,
                        isExpense = isExpense,
                        color = selectedColor
                    )
                    
                    onSave(newCategory)
                    onDismiss()
                }
            ) {
                Text(text = "保存")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "取消")
            }
        }
    )
}