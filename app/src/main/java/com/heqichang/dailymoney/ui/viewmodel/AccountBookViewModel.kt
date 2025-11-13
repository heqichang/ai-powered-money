package com.heqichang.dailymoney.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heqichang.dailymoney.data.entity.AccountBook
import com.heqichang.dailymoney.data.repository.AccountBookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AccountBookViewModel @Inject constructor(
    private val repository: AccountBookRepository
) : ViewModel() {
    
    private val _accountBooks = repository.getAllAccountBooks()
    val accountBooks = _accountBooks
    
    private val _selectedAccountBook = MutableStateFlow\u003cAccountBook?\u003e(null)
    val selectedAccountBook: StateFlow\u003cAccountBook?\u003e = _selectedAccountBook.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow\u003cBoolean\u003e = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow\u003cString?\u003e(null)
    val error: StateFlow\u003cString?\u003e = _error.asStateFlow()
    
    // 选择账本
    fun selectAccountBook(accountBook: AccountBook) {
        _selectedAccountBook.value = accountBook
    }
    
    // 创建新账本
    fun createAccountBook(name: String, description: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val newBook = AccountBook(
                    name = name,
                    description = description
                )
                repository.insertAccountBook(newBook)
            } catch (e: Exception) {
                _error.value = "创建账本失败: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    // 更新账本
    fun updateAccountBook(accountBook: AccountBook) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                repository.updateAccountBook(accountBook)
            } catch (e: Exception) {
                _error.value = "更新账本失败: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    // 删除账本
    fun deleteAccountBook(accountBook: AccountBook) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                repository.deleteAccountBook(accountBook)
                // 如果删除的是当前选中的账本，清除选中状态
                if (_selectedAccountBook.value?.id == accountBook.id) {
                    _selectedAccountBook.value = null
                }
            } catch (e: Exception) {
                _error.value = "删除账本失败: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    // 加载默认账本
    fun loadDefaultAccountBook() {
        viewModelScope.launch {
            try {
                val books = _accountBooks.replayCache.firstOrNull() ?: emptyList()
                if (books.isNotEmpty()) {
                    _selectedAccountBook.value = books.first()
                }
            } catch (e: Exception) {
                _error.value = "加载默认账本失败"
            }
        }
    }
}