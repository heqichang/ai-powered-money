package com.heqichang.dailymoney.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heqichang.dailymoney.data.entity.AccountBook
import com.heqichang.dailymoney.data.repository.AccountBookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
class AccountBookViewModel(
    private val repository: AccountBookRepository
) : ViewModel() {
    
    private val _accountBooks = repository.getAllAccountBooks()
    val accountBooks = _accountBooks
    
    private val _selectedAccountBook = MutableStateFlow<AccountBook?>(null)
    val selectedAccountBook: StateFlow<AccountBook?> = _selectedAccountBook.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
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
            _isLoading.value = true
            _error.value = null
            try {
                // 从仓库获取第一个账本
                val firstBook = repository.getFirstAccountBook()
                if (firstBook != null) {
                    _selectedAccountBook.value = firstBook
                }
            } catch (e: Exception) {
                _error.value = "加载默认账本失败"
            } finally {
                _isLoading.value = false
            }
        }
    }
}