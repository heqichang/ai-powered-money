package com.heqichang.dailymoney.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.heqichang.dailymoney.data.entity.Transaction
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions WHERE account_book_id = :accountBookId ORDER BY transaction_date DESC")
    fun getTransactionsByAccountBook(accountBookId: Int): Flow<List<Transaction>>
    
    @Query("SELECT * FROM transactions WHERE account_book_id = :accountBookId AND 
            transaction_date BETWEEN :startDate AND :endDate ORDER BY transaction_date DESC")
    fun getTransactionsByDateRange(accountBookId: Int, startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<Transaction>>
    
    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: Int): Transaction?
    
    @Insert
    suspend fun insertTransaction(transaction: Transaction): Long
    
    @Update
    suspend fun updateTransaction(transaction: Transaction)
    
    @Delete
    suspend fun deleteTransaction(transaction: Transaction)
    
    // 按年份统计
    @Query("SELECT strftime('%Y', transaction_date) as year, 
            SUM(CASE WHEN (SELECT is_expense FROM categories WHERE id = category_id) = 0 THEN amount ELSE 0 END) as income,
            SUM(CASE WHEN (SELECT is_expense FROM categories WHERE id = category_id) = 1 THEN amount ELSE 0 END) as expense
            FROM transactions WHERE account_book_id = :accountBookId
            GROUP BY year ORDER BY year DESC")
    fun getYearlyStatistics(accountBookId: Int): Flow<List<YearlyStatResult>>
    
    // 按分类统计
    @Query("SELECT c.id as categoryId, c.name as categoryName, c.color as categoryColor, 
            SUM(t.amount) as totalAmount, COUNT(t.id) as count
            FROM transactions t
            JOIN categories c ON t.category_id = c.id
            WHERE t.account_book_id = :accountBookId 
            AND t.transaction_date BETWEEN :startDate AND :endDate
            AND c.is_expense = :isExpense
            GROUP BY c.id ORDER BY totalAmount DESC")
    suspend fun getCategoryStatistics(
        accountBookId: Int,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        isExpense: Boolean
    ): List<CategoryStatResult>
    
    // 统计结果模型
    data class YearlyStatResult(
        val year: String,
        val income: Double,
        val expense: Double
    )
    
    data class CategoryStatResult(
        val categoryId: Int,
        val categoryName: String,
        val categoryColor: Int,
        val totalAmount: Double,
        val count: Int
    )
}