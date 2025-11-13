package com.heqichang.dailymoney.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.heqichang.dailymoney.data.entity.AccountBook
import com.heqichang.dailymoney.ui.navigation.Screen

@Composable
fun AccountBookSelector(
    navController: NavController,
    accountBooks: List<AccountBook> = emptyList(),
    selectedBook: AccountBook? = null,
    onBookSelected: (AccountBook) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                // 打开账本列表页面而不是下拉菜单，以提供更好的用户体验
                navController.navigate(Screen.AccountBook.route)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = selectedBook?.name ?: "选择账本",
            style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = androidx.compose.material.icons.Icons.Default.ArrowDropDown,
            contentDescription = "展开"
        )
    }
}