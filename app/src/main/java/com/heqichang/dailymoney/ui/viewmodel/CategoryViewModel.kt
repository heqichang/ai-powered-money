package com.heqichang.dailymoney.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heqichang.dailymoney.data.entity.Category
import com.heqichang.dailymoney.data.repository.CategoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CategoryViewModel @Inject constructor(
    private val repository: CategoryRepository
) : ViewModel() {
    
    private val _expenseCategories = repository.getExpenseCategories()
    val expenseCategories = _expenseCategories
    
    private val _incomeCategories = repository.getIncomeCategories()
    val incomeCategories = _incomeCategories
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow\u003cBoolean\u003e = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow\u003cString?\u003e(null)
    val error: StateFlow\u003cString?\u003e = _error.asStateFlow()
    
    // 创建新分类
    fun createCategory(name: String, color: Int, isExpense: Boolean) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                // 检查分类名称是否已存在
                if (repository.categoryExists(name, isExpense)) {
                    _error.value = "分类名称已存在"
                    return@launch
                }
                
                val newCategory = Category(
                    name = name,
                    color = color,
                    isExpense = isExpense
                )
                repository.insertCategory(newCategory)
            } catch (e: Exception) {
                _error.value = "创建分类失败: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    // 更新分类
    fun updateCategory(category: Category) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                repository.updateCategory(category)
            } catch (e: Exception) {
                _error.value = "更新分类失败: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    // 删除分类
    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                repository.deleteCategory(category)
            } catch (e: Exception) {
                _error.value = "删除分类失败: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    // 获取分类详情
    suspend fun getCategoryById(id: Int): Category? {
        return try {
            repository.getCategoryById(id)
        } catch (e: Exception) {
            _error.value = "获取分类失败"
            null
        }
    }
}