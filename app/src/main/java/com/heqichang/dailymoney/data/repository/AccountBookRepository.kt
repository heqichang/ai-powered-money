package com.heqichang.dailymoney.data.repository

import com.heqichang.dailymoney.data.dao.AccountBookDao
import com.heqichang.dailymoney.data.entity.AccountBook
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AccountBookRepository @Inject constructor(
    private val accountBookDao: AccountBookDao
) {
    
    fun getAllAccountBooks(): Flow<List<AccountBook>> {
        return accountBookDao.getAllAccountBooks()
    }
    
    suspend fun getAccountBookById(id: Int): AccountBook? {
        return accountBookDao.getAccountBookById(id)
    }
    
    suspend fun insertAccountBook(accountBook: AccountBook): Long {
        return accountBookDao.insertAccountBook(accountBook)
    }
    
    suspend fun updateAccountBook(accountBook: AccountBook) {
        accountBookDao.updateAccountBook(accountBook)
    }
    
    suspend fun deleteAccountBook(accountBook: AccountBook) {
        accountBookDao.deleteAccountBook(accountBook)
    }
    
    suspend fun hasAccountBooks(): Boolean {
        return accountBookDao.getAccountBookCount() > 0
    }
    
    suspend fun createDefaultAccountBook(): Long {
        val defaultBook = AccountBook(
            name = "默认账本",
            description = "系统默认账本"
        )
        return insertAccountBook(defaultBook)
    }
}