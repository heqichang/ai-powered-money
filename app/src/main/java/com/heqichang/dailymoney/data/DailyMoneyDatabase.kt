package com.heqichang.dailymoney.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.heqichang.dailymoney.data.dao.AccountBookDao
import com.heqichang.dailymoney.data.dao.CategoryDao
import com.heqichang.dailymoney.data.dao.TransactionDao
import com.heqichang.dailymoney.data.entity.AccountBook
import com.heqichang.dailymoney.data.entity.Category
import com.heqichang.dailymoney.data.entity.Transaction
import com.heqichang.dailymoney.data.util.LocalDateTimeConverter

@Database(
    entities = [
        AccountBook::class,
        Category::class,
        Transaction::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(LocalDateTimeConverter::class)
abstract class DailyMoneyDatabase : RoomDatabase() {
    
    abstract fun accountBookDao(): AccountBookDao
    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao
    
    companion object {
        @Volatile
        private var INSTANCE: DailyMoneyDatabase? = null
        
        fun getDatabase(context: Context): DailyMoneyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DailyMoneyDatabase::class.java,
                    "daily_money_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}