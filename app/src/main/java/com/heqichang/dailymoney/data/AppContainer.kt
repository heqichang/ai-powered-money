package com.heqichang.dailymoney.data

import android.content.Context
import androidx.room.Room
import com.heqichang.dailymoney.data.dao.AccountBookDao
import com.heqichang.dailymoney.data.dao.CategoryDao
import com.heqichang.dailymoney.data.dao.TransactionDao
import com.heqichang.dailymoney.data.repository.AccountBookRepository
import com.heqichang.dailymoney.data.repository.CategoryRepository
import com.heqichang.dailymoney.data.repository.TransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class AppContainer(context: Context) {
    
    // 数据库实例
    private val database: DailyMoneyDatabase = Room.databaseBuilder(
        context.applicationContext,
        DailyMoneyDatabase::class.java,
        "daily_money_database"
    ).build()
    
    // DAO实例
    private val accountBookDao: AccountBookDao = database.accountBookDao()
    private val categoryDao: CategoryDao = database.categoryDao()
    private val transactionDao: TransactionDao = database.transactionDao()
    
    // Repository实例
    val accountBookRepository: AccountBookRepository = AccountBookRepository(accountBookDao)
    val categoryRepository: CategoryRepository = CategoryRepository(categoryDao)
    val transactionRepository: TransactionRepository = TransactionRepository(
        transactionDao,
        categoryRepository
    )
    
    // 初始化默认数据
    init {
        runBlocking {
            withContext(Dispatchers.IO) {
                // 初始化默认账本
                if (!accountBookRepository.hasAccountBooks()) {
                    accountBookRepository.createDefaultAccountBook()
                }
                
                // 初始化默认分类
                categoryRepository.createDefaultCategories()
            }
        }
    }
}