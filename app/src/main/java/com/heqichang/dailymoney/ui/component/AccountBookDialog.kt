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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.heqichang.dailymoney.data.entity.AccountBook
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountBookDialog(
    accountBook: AccountBook? = null,
    onDismiss: () -> Unit,
    onSave: (AccountBook) -> Unit
) {
    // 预设的账本颜色选项
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
    
    var name by remember { mutableStateOf(accountBook?.name ?: "") }
    var description by remember { mutableStateOf(accountBook?.description ?: "") }
    var selectedColor by remember { mutableStateOf(accountBook?.color ?: colorOptions[0].value.toLong()) }
    var nameError by remember { mutableStateOf(false) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = if (accountBook == null) "新建账本" else "编辑账本")
        },
        text = {
            Column {
                // 账本名称输入
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        nameError = false
                    },
                    label = { Text(text = "账本名称") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = nameError,
                    supportingText = if (nameError) { Text("请输入账本名称") } else null
                )
                
                // 账本描述输入
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(text = "账本描述") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = false,
                    maxLines = 3
                )
                
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
                    
                    val book = accountBook?.copy(
                        name = name,
                        description = description,
                        color = selectedColor,
                        updatedAt = LocalDateTime.now()
                    ) ?: AccountBook(
                        name = name,
                        description = description,
                        color = selectedColor,
                        createdAt = LocalDateTime.now(),
                        updatedAt = LocalDateTime.now()
                    )
                    
                    onSave(book)
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

@Composable
fun ColorOption(color: Color, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .size(40.dp)
                .background(color = color, shape = RoundedCornerShape(8.dp))
                .clickable(onClick = onClick)
                .border(
                    width = 2.dp,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                    shape = RoundedCornerShape(8.dp)
                )
        )
    }
}