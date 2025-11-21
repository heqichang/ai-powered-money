package com.heqichang.dailymoney.data.repository

import com.heqichang.dailymoney.data.dao.CategoryDao
import com.heqichang.dailymoney.data.entity.Category
import kotlinx.coroutines.flow.Flow
class CategoryRepository(
    private val categoryDao: CategoryDao
) {
    
    fun getExpenseCategories(): Flow<List<Category>> {
        return categoryDao.getCategoriesByType(isExpense = true)
    }
    
    fun getIncomeCategories(): Flow<List<Category>> {
        return categoryDao.getCategoriesByType(isExpense = false)
    }
    
    fun getAllCategories(): Flow<List<Category>> {
        return categoryDao.getAllCategories()
    }
    
    suspend fun getCategoryById(id: Int): Category? {
        return categoryDao.getCategoryById(id)
    }
    
    suspend fun insertCategory(category: Category): Long {
        return categoryDao.insertCategory(category)
    }
    
    suspend fun updateCategory(category: Category) {
        categoryDao.updateCategory(category)
    }
    
    suspend fun deleteCategory(category: Category) {
        categoryDao.deleteCategory(category)
    }
    
    suspend fun categoryExists(name: String, isExpense: Boolean): Boolean {
        return categoryDao.categoryExists(name, isExpense)
    }
    
    suspend fun createDefaultCategories() {
        // 默认支出分类
        val expenseCategories = listOf(
            Category(name = "餐饮", color = 0xFFF44336),
            Category(name = "交通", color = 0xFF2196F3),
            Category(name = "购物", color = 0xFF9C27B0),
            Category(name = "娱乐", color = 0xFFFF9800),
            Category(name = "医疗", color = 0xFF4CAF50),
            Category(name = "教育", color = 0xFF00BCD4),
            Category(name = "住房", color = 0xFF795548),
            Category(name = "其他", color = 0xFF607D8B)
        )
        
        // 默认收入分类
        val incomeCategories = listOf(
            Category(name = "工资", color = 0xFF4CAF50, isExpense = false),
            Category(name = "奖金", color = 0xFF8BC34A, isExpense = false),
            Category(name = "投资", color = 0xFF009688, isExpense = false),
            Category(name = "其他收入", color = 0xFF607D8B, isExpense = false)
        )
        
        // 插入默认分类
        (expenseCategories + incomeCategories).forEach { category ->
            if (!categoryExists(category.name, category.isExpense)) {
                insertCategory(category)
            }
        }
    }
}