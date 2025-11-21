package com.heqichang.dailymoney.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heqichang.dailymoney.data.entity.Category
import com.heqichang.dailymoney.data.entity.Transaction
import com.heqichang.dailymoney.data.repository.TransactionRepository
import com.heqichang.dailymoney.util.DateUtils
import com.heqichang.dailymoney.util.AmountUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Year

class StatisticsViewModel : ViewModel() {
    // 使用MockTransactionRepository提供模拟数据
    private val transactionRepository: TransactionRepository = MockTransactionRepository()
    
    // 当前选中的年份
    private val _selectedYear = MutableStateFlow(Year.now().value)
    val selectedYear: StateFlow<Int> = _selectedYear.asStateFlow()
    
    // 月度支出统计数据
    private val _monthlyExpenses = MutableStateFlow<List<Pair<String, Double>>>(emptyList())
    val monthlyExpenses: StateFlow<List<Pair<String, Double>>> = _monthlyExpenses.asStateFlow()
    
    // 月度收入统计数据
    private val _monthlyIncomes = MutableStateFlow<List<Pair<String, Double>>>(emptyList())
    val monthlyIncomes: StateFlow<List<Pair<String, Double>>> = _monthlyIncomes.asStateFlow()
    
    // 分类支出统计数据
    private val _categoryExpenses = MutableStateFlow<List<Pair<Category, Double>>>(emptyList())
    val categoryExpenses: StateFlow<List<Pair<Category, Double>>> = _categoryExpenses.asStateFlow()
    
    // 分类收入统计数据
    private val _categoryIncomes = MutableStateFlow<List<Pair<Category, Double>>>(emptyList())
    val categoryIncomes: StateFlow<List<Pair<Category, Double>>> = _categoryIncomes.asStateFlow()
    
    // 总支出
    private val _totalExpense = MutableStateFlow(0.0)
    val totalExpense: StateFlow<Double> = _totalExpense.asStateFlow()
    
    // 总收入
    private val _totalIncome = MutableStateFlow(0.0)
    val totalIncome: StateFlow<Double> = _totalIncome.asStateFlow()
    
    // 加载状态
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    // 错误信息
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    // 选择年份
    fun selectYear(year: Int) {
        _selectedYear.value = year
        loadStatisticsData(year)
    }
    
    // 加载统计数据
    private fun loadStatisticsData(year: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                // 获取指定年份的所有交易记录（处理Flow数据）
                transactionRepository.getTransactionsByYear(year).collect { transactions ->
                    // 计算月度统计
                    calculateMonthlyStatistics(transactions)
                    
                    // 计算分类统计
                    calculateCategoryStatistics(transactions)
                    
                    // 计算总计
                    _totalExpense.value = transactions.filter { it.amount < 0 }.sumOf { -it.amount }
                    _totalIncome.value = transactions.filter { it.amount > 0 }.sumOf { it.amount }
                }
                
            } catch (e: Exception) {
                _error.value = "加载统计数据失败: \${e.message}"
                // 加载失败时使用模拟数据
                loadMockData()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    // 计算月度统计
    private fun calculateMonthlyStatistics(transactions: List<Transaction>) {
        val monthlyExpensesMap = mutableMapOf<String, Double>()
        val monthlyIncomesMap = mutableMapOf<String, Double>()
        
        // 初始化12个月的数据
        for (month in 1..12) {
            val monthKey = DateUtils.getMonthFormat(month)
            monthlyExpensesMap[monthKey] = 0.0
            monthlyIncomesMap[monthKey] = 0.0
        }
        
        // 按月份汇总数据
        transactions.forEach { transaction ->
            val month = transaction.transactionDate.month.value // 使用transactionDate字段
            val monthKey = DateUtils.getMonthFormat(month)
            
            if (transaction.amount < 0) {
                // 支出
                monthlyExpensesMap[monthKey] = monthlyExpensesMap[monthKey]!! + (-transaction.amount)
            } else {
                // 收入
                monthlyIncomesMap[monthKey] = monthlyIncomesMap[monthKey]!! + transaction.amount
            }
        }
        
        // 转换为列表并排序
        _monthlyExpenses.value = monthlyExpensesMap.toList().sortedBy { it.first }
        _monthlyIncomes.value = monthlyIncomesMap.toList().sortedBy { it.first }
    }
    
    // 计算分类统计
    private fun calculateCategoryStatistics(transactions: List<Transaction>) {
        val categoryExpensesMap = mutableMapOf<Category, Double>()
        val categoryIncomesMap = mutableMapOf<Category, Double>()
        
        // 按分类汇总数据
        transactions.forEach { transaction ->
            transaction.category?.let { category ->
                if (transaction.amount < 0) {
                    // 支出
                    categoryExpensesMap[category] = categoryExpensesMap.getOrDefault(category, 0.0) + (-transaction.amount)
                } else {
                    // 收入
                    categoryIncomesMap[category] = categoryIncomesMap.getOrDefault(category, 0.0) + transaction.amount
                }
            }
        }
        
        // 转换为列表并按金额降序排序
        _categoryExpenses.value = categoryExpensesMap.toList()
            .sortedByDescending { it.second }
        
        _categoryIncomes.value = categoryIncomesMap.toList()
            .sortedByDescending { it.second }
    }
    
    // 获取可用年份列表
    fun getAvailableYears(): List<Int> {
        // 从DateUtils获取可用年份列表
        return DateUtils.getAvailableYears()
    }
    
    // 加载模拟数据（当真实数据加载失败时）
    private fun loadMockData() {
        // 模拟月度数据
        val mockMonthlyExpenses = listOf(
            DateUtils.getMonthFormat(1) to 1500.0,
            DateUtils.getMonthFormat(2) to 1800.0,
            DateUtils.getMonthFormat(3) to 2200.0,
            DateUtils.getMonthFormat(4) to 1900.0,
            DateUtils.getMonthFormat(5) to 2500.0,
            DateUtils.getMonthFormat(6) to 2800.0,
            DateUtils.getMonthFormat(7) to 3000.0,
            DateUtils.getMonthFormat(8) to 2700.0,
            DateUtils.getMonthFormat(9) to 2400.0,
            DateUtils.getMonthFormat(10) to 2600.0,
            DateUtils.getMonthFormat(11) to 2900.0,
            DateUtils.getMonthFormat(12) to 3200.0
        )
        
        val mockMonthlyIncomes = listOf(
            DateUtils.getMonthFormat(1) to 8000.0,
            DateUtils.getMonthFormat(2) to 9000.0,
            DateUtils.getMonthFormat(3) to 8500.0,
            DateUtils.getMonthFormat(4) to 9200.0,
            DateUtils.getMonthFormat(5) to 8800.0,
            DateUtils.getMonthFormat(6) to 9500.0,
            DateUtils.getMonthFormat(7) to 9800.0,
            DateUtils.getMonthFormat(8) to 10000.0,
            DateUtils.getMonthFormat(9) to 9600.0,
            DateUtils.getMonthFormat(10) to 10200.0,
            DateUtils.getMonthFormat(11) to 9900.0,
            DateUtils.getMonthFormat(12) to 11000.0
        )
        
        _monthlyExpenses.value = mockMonthlyExpenses
        _monthlyIncomes.value = mockMonthlyIncomes
        
        // 计算总计
        _totalIncome.value = mockMonthlyIncomes.sumOf { it.second }
        _totalExpense.value = mockMonthlyExpenses.sumOf { it.second }
        
        // 模拟分类数据可以暂时为空，因为没有Category对象
    
    // 初始化加载数据
    init {
        loadStatisticsData(_selectedYear.value)
    }
}