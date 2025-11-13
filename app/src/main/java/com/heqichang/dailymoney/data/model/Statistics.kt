package com.heqichang.dailymoney.data.model

import com.heqichang.dailymoney.data.entity.Category

// 分类统计数据
data class CategoryStatistics(
    val category: Category,
    val totalAmount: Double,
    val percentage: Double,
    val count: Int
)

// 年份统计数据
data class YearStatistics(
    val year: Int,
    val totalIncome: Double,
    val totalExpense: Double,
    val categoryStats: List<CategoryStatistics>
)

// 月度统计数据
data class MonthStatistics(
    val year: Int,
    val month: Int,
    val totalIncome: Double,
    val totalExpense: Double,
    val categoryStats: List<CategoryStatistics>
)