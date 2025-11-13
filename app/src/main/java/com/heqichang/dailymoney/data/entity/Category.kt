package com.heqichang.dailymoney.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val iconRes: Int? = null, // 图标资源ID
    val color: Long = 0xFF1976D2, // 默认颜色
    val isExpense: Boolean = true, // true: 支出分类, false: 收入分类
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)