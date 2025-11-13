package com.heqichang.dailymoney.data.repository

import com.heqichang.dailymoney.data.dao.TransactionDao
import com.heqichang.dailymoney.data.entity.Category
import com.heqichang.dailymoney.data.entity.Transaction
import com.heqichang.dailymoney.data.model.CategoryStatistics
import com.heqichang.dailymoney.data.model.YearStatistics
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

class TransactionRepository @Inject constructor(
    private val transactionDao: TransactionDao,
    private val categoryRepository: CategoryRepository
) {
    
    fun getTransactionsByAccountBook(accountBookId: Int): Flow\u003cList\u003cTransaction\u003e\u003e {
        return transactionDao.getTransactionsByAccountBook(accountBookId)
    }
    
    fun getTransactionsByDateRange(accountBookId: Int, startDate: LocalDateTime, endDate: LocalDateTime): Flow\u003cList\u003cTransaction\u003e\u003e {
        return transactionDao.getTransactionsByDateRange(accountBookId, startDate, endDate)
    }
    
    suspend fun getTransactionById(id: Int): Transaction? {
        return transactionDao.getTransactionById(id)
    }
    
    suspend fun insertTransaction(transaction: Transaction): Long {
        return transactionDao.insertTransaction(transaction)
    }
    
    suspend fun updateTransaction(transaction: Transaction) {
        transactionDao.updateTransaction(transaction)
    }
    
    suspend fun deleteTransaction(transaction: Transaction) {
        transactionDao.deleteTransaction(transaction)
    }
    
    // 按年份获取统计数据
    suspend fun getYearStatistics(accountBookId: Int, year: Int): YearStatistics {
        val startDate = LocalDateTime.of(year, 1, 1, 0, 0)
        val endDate = LocalDateTime.of(year, 12, 31, 23, 59, 59)
        
        // 获取支出统计
        val expenseStats = getCategoryStatistics(accountBookId, startDate, endDate, true)
        
        // 获取收入统计
        val incomeStats = getCategoryStatistics(accountBookId, startDate, endDate, false)
        
        val totalExpense = expenseStats.sumOf {\ it.totalAmount }
        val totalIncome = incomeStats.sumOf {\ it.totalAmount }
        
        // 合并统计数据
        val allStats = expenseStats + incomeStats
        
        return YearStatistics(
            year = year,
            totalIncome = totalIncome,
            totalExpense = totalExpense,
            categoryStats = allStats
        )
    }
    
    // 按年月获取统计数据
    suspend fun getMonthStatistics(accountBookId: Int, year: Int, month: Int): YearStatistics {
        val startDate = LocalDateTime.of(year, month, 1, 0, 0)
        val endDate = startDate.with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59).withSecond(59)
        
        // 获取支出统计
        val expenseStats = getCategoryStatistics(accountBookId, startDate, endDate, true)
        
        // 获取收入统计
        val incomeStats = getCategoryStatistics(accountBookId, startDate, endDate, false)
        
        val totalExpense = expenseStats.sumOf {\ it.totalAmount }
        val totalIncome = incomeStats.sumOf {\ it.totalAmount }
        
        // 合并统计数据
        val allStats = expenseStats + incomeStats
        
        return YearStatistics(
            year = year,
            totalIncome = totalIncome,
            totalExpense = totalExpense,
            categoryStats = allStats
        )
    }
    
    // 获取分类统计数据
    private suspend fun getCategoryStatistics(
        accountBookId: Int,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        isExpense: Boolean
    ): List\u003cCategoryStatistics\u003e {
        val rawStats = transactionDao.getCategoryStatistics(accountBookId, startDate, endDate, isExpense)
        val totalAmount = rawStats.sumOf {\ it.totalAmount }
        
        return rawStats.map {\ stat ->
            val category = Category(
                id = stat.categoryId,
                name = stat.categoryName,
                color = stat.categoryColor,
                isExpense = isExpense
            )
            
            CategoryStatistics(
                category = category,
                totalAmount = stat.totalAmount,
                percentage = if (totalAmount > 0) (stat.totalAmount / totalAmount) * 100 else 0.0,
                count = stat.count
            )
        }
    }
    
    // 获取所有有记录的年份
    fun getAvailableYears(accountBookId: Int): Flow\u003cList\u003cInt\u003e\u003e {
        return transactionDao.getYearlyStatistics(accountBookId).map {\ stats ->
            stats.map {\ it.year.toInt() }
        }
    }
}