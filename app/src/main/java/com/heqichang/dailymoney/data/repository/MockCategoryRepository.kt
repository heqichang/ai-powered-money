package com.heqichang.dailymoney.data.repository

import com.heqichang.dailymoney.data.entity.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDateTime

class MockCategoryRepository : CategoryRepository {
    // 模拟数据列表
    private val categories = mutableListOf(
        // 支出分类
        Category(id = 1, name = "餐饮", iconRes = null, color = 0xFF4CAF50, isExpense = true),
        Category(id = 2, name = "购物", iconRes = null, color = 0xFF2196F3, isExpense = true),
        Category(id = 3, name = "住房", iconRes = null, color = 0xFFF44336, isExpense = true),
        Category(id = 4, name = "交通", iconRes = null, color = 0xFFFF9800, isExpense = true),
        Category(id = 5, name = "教育", iconRes = null, color = 0xFF9C27B0, isExpense = true),
        Category(id = 6, name = "医疗", iconRes = null, color = 0xFF00BCD4, isExpense = true),
        Category(id = 7, name = "娱乐", iconRes = null, color = 0xFFE91E63, isExpense = true),
        Category(id = 8, name = "其他", iconRes = null, color = 0xFF795548, isExpense = true),
        
        // 收入分类
        Category(id = 9, name = "工资", iconRes = null, color = 0xFF4CAF50, isExpense = false),
        Category(id = 10, name = "奖金", iconRes = null, color = 0xFF2196F3, isExpense = false),
        Category(id = 11, name = "投资", iconRes = null, color = 0xFFFF9800, isExpense = false),
        Category(id = 12, name = "兼职", iconRes = null, color = 0xFF9C27B0, isExpense = false),
        Category(id = 13, name = "其他收入", iconRes = null, color = 0xFF795548, isExpense = false)
    )
    
    // 状态流
    private val categoriesFlow = MutableStateFlow(categories.toList())
    
    // 获取所有支出分类
    override fun getExpenseCategories(): Flow<List<Category>> {
        return categoriesFlow.map { list -> list.filter { it.isExpense } }
    }
    
    // 获取所有收入分类
    override fun getIncomeCategories(): Flow<List<Category>> {
        return categoriesFlow.map { list -> list.filter { !it.isExpense } }
    }
    
    // 根据ID获取分类
    override suspend fun getCategoryById(id: Int): Category? {
        return categories.find { it.id == id }
    }
    
    // 检查分类名称是否已存在
    override suspend fun categoryExists(name: String, isExpense: Boolean): Boolean {
        return categories.any { it.name == name && it.isExpense == isExpense }
    }
    
    // 插入分类
    override suspend fun insertCategory(category: Category) {
        val newId = if (categories.isEmpty()) 1 else categories.maxByOrNull { it.id }?.id?.plus(1) ?: 1
        val newCategory = category.copy(
            id = newId,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        categories.add(newCategory)
        updateFlow()
    }
    
    // 更新分类
    override suspend fun updateCategory(category: Category) {
        val index = categories.indexOfFirst { it.id == category.id }
        if (index != -1) {
            categories[index] = category.copy(updatedAt = LocalDateTime.now())
            updateFlow()
        }
    }
    
    // 删除分类
    override suspend fun deleteCategory(category: Category) {
        categories.removeIf { it.id == category.id }
        updateFlow()
    }
    
    // 获取所有分类
    override fun getAllCategories(): Flow<List<Category>> {
        return categoriesFlow
    }
    
    // 更新流
    private fun updateFlow() {
        categoriesFlow.value = categories.toList()
    }
    
    // 扩展函数: Flow映射
    private fun <T, R> MutableStateFlow<List<T>>.map(transform: (List<T>) -> List<R>): Flow<List<R>> {
        return this.map(transform) as Flow<List<R>>
    }
}