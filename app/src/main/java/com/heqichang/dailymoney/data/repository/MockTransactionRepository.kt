package com.heqichang.dailymoney.data.repository

import com.heqichang.dailymoney.data.entity.Category
import com.heqichang.dailymoney.data.entity.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDateTime
import java.time.Month
import java.util.Random

class MockTransactionRepository : TransactionRepository {
    private val transactions = mutableListOf<Transaction>()
    private val categories = mutableListOf<Category>()
    private val random = Random()
    
    // 状态流
    private val transactionsFlow = MutableStateFlow(transactions.toList())
    
    init {
        // 初始化模拟分类数据
        initCategories()
        // 初始化模拟交易数据
        initMockTransactions()
    }
    
    // 初始化分类数据
    private fun initCategories() {
        categories.addAll(
            listOf(
                // 支出分类
                Category(id = 1, name = "餐饮", iconRes = null, color = 0xFF4CAF50, isExpense = true),
                Category(id = 2, name = "购物", iconRes = null, color = 0xFF2196F3, isExpense = true),
                Category(id = 3, name = "住房", iconRes = null, color = 0xFFF44336, isExpense = true),
                Category(id = 4, name = "交通", iconRes = null, color = 0xFFFF9800, isExpense = true),
                Category(id = 5, name = "教育", iconRes = null, color = 0xFF9C27B0, isExpense = true),
                
                // 收入分类
                Category(id = 9, name = "工资", iconRes = null, color = 0xFF4CAF50, isExpense = false),
                Category(id = 10, name = "奖金", iconRes = null, color = 0xFF2196F3, isExpense = false),
                Category(id = 11, name = "投资", iconRes = null, color = 0xFFFF9800, isExpense = false)
            )
        )
    }
    
    // 初始化模拟交易数据
    private fun initMockTransactions() {
        val currentYear = LocalDateTime.now().year
        
        // 为最近两年生成模拟数据
        for (year in currentYear - 1..currentYear) {
            for (month in 1..12) {
                // 每月生成10-20条交易记录
                val transactionCount = random.nextInt(10) + 10
                
                for (i in 1..transactionCount) {
                    val day = random.nextInt(28) + 1
                    val isExpense = random.nextBoolean() || random.nextBoolean() // 75%概率是支出
                    
                    val categoryList = if (isExpense) {
                        categories.filter { it.isExpense }
                    } else {
                        categories.filter { !it.isExpense }
                    }
                    
                    val category = categoryList.random()
                    
                    val amount = if (isExpense) {
                        // 支出金额：50-1000之间的随机数
                        - (random.nextDouble() * 950 + 50)
                    } else {
                        // 收入金额：1000-5000之间的随机数
                        random.nextDouble() * 4000 + 1000
                    }
                    
                    val transaction = Transaction(
                        id = transactions.size + 1,
                        amount = amount,
                        accountBookId = 1, // 默认账本
                        categoryId = category.id,
                        remark = "模拟交易记录",
                        transactionDate = LocalDateTime.of(year, month, day, random.nextInt(24), random.nextInt(60))
                    )
                    
                    // 设置分类信息
                    transaction.category = category
                    
                    transactions.add(transaction)
                }
            }
        }
        
        updateFlow()
    }
    
    // 获取指定年份的交易记录
    override fun getTransactionsByYear(year: Int): Flow<List<Transaction>> {
        val filtered = transactions.filter {
            it.transactionDate.year == year
        }
        return MutableStateFlow(filtered)
    }
    
    // 获取指定年月的交易记录
    override fun getTransactionsByMonth(year: Int, month: Int): Flow<List<Transaction>> {
        val filtered = transactions.filter {
            it.transactionDate.year == year && it.transactionDate.monthValue == month
        }
        return MutableStateFlow(filtered)
    }
    
    // 获取指定日期范围的交易记录
    override fun getTransactionsByDateRange(startDate: String, endDate: String): Flow<List<Transaction>> {
        // 简化实现，实际应该解析日期字符串
        return MutableStateFlow(emptyList())
    }
    
    // 根据账本ID获取交易记录
    override fun getTransactionsByAccountBook(accountBookId: Int): Flow<List<Transaction>> {
        val filtered = transactions.filter { it.accountBookId == accountBookId }
        return MutableStateFlow(filtered)
    }
    
    // 根据分类ID获取交易记录
    override fun getTransactionsByCategory(categoryId: Int): Flow<List<Transaction>> {
        val filtered = transactions.filter { it.categoryId == categoryId }
        return MutableStateFlow(filtered)
    }
    
    // 插入交易记录
    override suspend fun insertTransaction(transaction: Transaction) {
        transactions.add(transaction.copy(id = transactions.size + 1))
        updateFlow()
    }
    
    // 更新交易记录
    override suspend fun updateTransaction(transaction: Transaction) {
        val index = transactions.indexOfFirst { it.id == transaction.id }
        if (index != -1) {
            transactions[index] = transaction
            updateFlow()
        }
    }
    
    // 删除交易记录
    override suspend fun deleteTransaction(transaction: Transaction) {
        transactions.removeIf { it.id == transaction.id }
        updateFlow()
    }
    
    // 根据ID获取交易记录
    override suspend fun getTransactionById(id: Int): Transaction? {
        return transactions.find { it.id == id }
    }
    
    // 获取所有交易记录
    override fun getAllTransactions(): Flow<List<Transaction>> {
        return transactionsFlow
    }
    
    // 更新流
    private fun updateFlow() {
        transactionsFlow.value = transactions.toList()
    }
}