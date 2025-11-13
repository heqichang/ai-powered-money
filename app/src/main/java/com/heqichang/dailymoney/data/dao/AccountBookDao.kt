package com.heqichang.dailymoney.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.heqichang.dailymoney.data.entity.AccountBook
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountBookDao {
    @Query("SELECT * FROM account_books ORDER BY created_at DESC")
    fun getAllAccountBooks(): Flow<List<AccountBook>>
    
    @Query("SELECT * FROM account_books WHERE id = :id")
    suspend fun getAccountBookById(id: Int): AccountBook?
    
    @Insert
    suspend fun insertAccountBook(accountBook: AccountBook): Long
    
    @Update
    suspend fun updateAccountBook(accountBook: AccountBook)
    
    @Delete
    suspend fun deleteAccountBook(accountBook: AccountBook)
    
    @Query("SELECT COUNT(*) FROM account_books")
    suspend fun getAccountBookCount(): Int
}