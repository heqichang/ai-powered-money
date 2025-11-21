package com.heqichang.dailymoney.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heqichang.dailymoney.data.entity.Transaction
import com.heqichang.dailymoney.data.model.YearStatistics
import com.heqichang.dailymoney.data.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.Year
class TransactionViewModel(
    private val repository: TransactionRepository
) : ViewModel() {
    
    private val _transactions = MutableStateFlow(emptyList\u003cTransaction\u003e())
    val transactions: StateFlow\u003cList\u003eTransaction\u003e\u003e = _transactions.asStateFlow()
    
    private val _currentYear = MutableStateFlow(Year.now().value)
    val currentYear: StateFlow\u003cInt\u003e = _currentYear.asStateFlow()
    
    private val _availableYears = MutableStateFlow(emptyList\u003cInt\u003e())
    val availableYears: StateFlow\u003cList\u003eInt\u003e\u003e = _availableYears.asStateFlow()
    
    private val _yearStatistics = MutableStateFlow\u003cYearStatistics?\u003e(null)
    val yearStatistics: StateFlow\u003cYearStatistics?\u003e = _yearStatistics.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow\u003cBoolean\u003e = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow\u003cString?\u003e(null)
    val error: StateFlow\u003cString?\u003e = _error.asStateFlow()
    
    // 设置当前账本ID并加载数据
    fun setAccountBookId(accountBookId: Int) {
        loadTransactions(accountBookId)
        loadAvailableYears(accountBookId)
        loadYearStatistics(accountBookId, _currentYear.value)
    }
    
    // 加载交易记录
    private fun loadTransactions(accountBookId: Int) {
        viewModelScope.launch {
            repository.getTransactionsByAccountBook(accountBookId)
                .collectLatest {
                    _transactions.value = it
                }
        }
    }
    
    // 加载可用年份
    private fun loadAvailableYears(accountBookId: Int) {
        viewModelScope.launch {
            repository.getAvailableYears(accountBookId)
                .collectLatest {
                    _availableYears.value = it
                }
        }
    }
    
    // 添加交易记录
    fun addTransaction(amount: Double, accountBookId: Int, categoryId: Int, remark: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val transaction = Transaction(
                    amount = amount,
                    accountBookId = accountBookId,
                    categoryId = categoryId,
                    remark = remark,
                    transactionDate = LocalDateTime.now()
                )
                repository.insertTransaction(transaction)
                // 重新加载统计数据
                loadYearStatistics(accountBookId, _currentYear.value)
            } catch (e: Exception) {
                _error.value = "添加交易记录失败: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    // 更新交易记录
    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                repository.updateTransaction(transaction)
                // 重新加载统计数据
                loadYearStatistics(transaction.accountBookId, _currentYear.value)
            } catch (e: Exception) {
                _error.value = "更新交易记录失败: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    // 删除交易记录
    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                repository.deleteTransaction(transaction)
                // 重新加载统计数据
                loadYearStatistics(transaction.accountBookId, _currentYear.value)
            } catch (e: Exception) {
                _error.value = "删除交易记录失败: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    // 切换年份并加载统计数据
    fun selectYear(year: Int, accountBookId: Int) {
        _currentYear.value = year
        loadYearStatistics(accountBookId, year)
    }
    
    // 加载年度统计数据
    private fun loadYearStatistics(accountBookId: Int, year: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val stats = repository.getYearStatistics(accountBookId, year)
                _yearStatistics.value = stats
            } catch (e: Exception) {
                _error.value = "加载统计数据失败: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    // 获取月度统计数据
    fun loadMonthStatistics(accountBookId: Int, year: Int, month: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val stats = repository.getMonthStatistics(accountBookId, year, month)
                _yearStatistics.value = stats
            } catch (e: Exception) {
                _error.value = "加载月度统计数据失败: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}