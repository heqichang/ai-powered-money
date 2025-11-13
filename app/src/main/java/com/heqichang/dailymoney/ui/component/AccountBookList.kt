package com.heqichang.dailymoney.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.heqichang.dailymoney.R
import com.heqichang.dailymoney.data.entity.AccountBook

@Composable
fun AccountBookList(
    accountBooks: List<AccountBook>,
    selectedBookId: Long? = null,
    onSelectBook: (AccountBook) -> Unit,
    onEditBook: (AccountBook) -> Unit,
    onDeleteBook: (AccountBook) -> Unit
) {
    if (accountBooks.isEmpty()) {
        EmptyState()
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = 16.dp
        ) {
            items(accountBooks) {
                AccountBookItem(
                    accountBook = it,
                    isSelected = it.id == selectedBookId,
                    onSelect = onSelectBook,
                    onEdit = onEditBook,
                    onDelete = onDeleteBook
                )
            }
        }
    }
}

@Composable
fun AccountBookItem(
    accountBook: AccountBook,
    isSelected: Boolean,
    onSelect: (AccountBook) -> Unit,
    onEdit: (AccountBook) -> Unit,
    onDelete: (AccountBook) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .margin(bottom = 12.dp)
            .clickable { onSelect(accountBook) },
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 账本图标
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        color = Color(accountBook.color ?: 0xFF4CAF50),
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.Book,
                    contentDescription = accountBook.name,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
            
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = accountBook.name,
                    style = androidx.compose.material3.MaterialTheme.typography.titleMedium
                )
                if (!accountBook.description.isNullOrEmpty()) {
                    Text(
                        text = accountBook.description,
                        style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
            
            if (isSelected) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.CheckCircle,
                    contentDescription = "已选中",
                    tint = colorResource(id = R.color.primary)
                )
            }
        }
    }
}

@Composable
fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = androidx.compose.material.icons.Icons.Default.BookOpen,
            contentDescription = "空状态",
            tint = Color.Gray,
            modifier = Modifier.size(64.dp)
        )
        Text(
            text = "还没有账本",
            style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "点击右下角按钮添加您的第一个账本",
            style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}