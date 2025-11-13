package com.heqichang.dailymoney.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.heqichang.dailymoney.data.entity.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories WHERE is_expense = :isExpense ORDER BY name ASC")
    fun getCategoriesByType(isExpense: Boolean): Flow<List<Category>>
    
    @Query("SELECT * FROM categories ORDER BY is_expense ASC, name ASC")
    fun getAllCategories(): Flow<List<Category>>
    
    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getCategoryById(id: Int): Category?
    
    @Insert
    suspend fun insertCategory(category: Category): Long
    
    @Update
    suspend fun updateCategory(category: Category)
    
    @Delete
    suspend fun deleteCategory(category: Category)
    
    @Query("SELECT EXISTS(SELECT 1 FROM categories WHERE name = :name AND is_expense = :isExpense)")
    suspend fun categoryExists(name: String, isExpense: Boolean): Boolean
}