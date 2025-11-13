package com.heqichang.dailymoney.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * 日期时间工具类，提供日期格式化、解析等功能
 */
object DateUtils {

    // 日期格式化器
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val monthFormatter = DateTimeFormatter.ofPattern("MM月")
    private val yearMonthFormatter = DateTimeFormatter.ofPattern("yyyy-MM")
    private val fullDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    /**
     * 将LocalDate格式化为字符串
     */
    fun formatDate(date: LocalDate): String {
        return dateFormatter.format(date)
    }

    /**
     * 将LocalDateTime格式化为完整日期时间字符串
     */
    fun formatDateTime(dateTime: LocalDateTime): String {
        return fullDateTimeFormatter.format(dateTime)
    }

    /**
     * 格式化月份为MM月格式
     */
    fun formatMonth(date: LocalDate): String {
        return monthFormatter.format(date)
    }

    /**
     * 格式化年月为yyyy-MM格式
     */
    fun formatYearMonth(date: LocalDate): String {
        return yearMonthFormatter.format(date)
    }

    /**
     * 解析日期字符串
     */
    fun parseDate(dateString: String): LocalDate {
        return LocalDate.parse(dateString, dateFormatter)
    }

    /**
     * 获取当前日期
     */
    fun getCurrentDate(): LocalDate {
        return LocalDate.now()
    }

    /**
     * 获取当前日期时间
     */
    fun getCurrentDateTime(): LocalDateTime {
        return LocalDateTime.now()
    }

    /**
     * 获取指定年份的所有月份
     */
    fun getMonthsOfYear(year: Int): List<Pair<String, LocalDate>> {
        return (1..12).map { month ->
            val date = LocalDate.of(year, month, 1)
            Pair(formatMonth(date), date)
        }
    }

    /**
     * 获取最近几年的列表
     */
    fun getRecentYears(years: Int): List<Int> {
        val currentYear = getCurrentDate().year
        return (currentYear downTo currentYear - years + 1).toList()
    }

    /**
     * 获取某个月的第一天
     */
    fun getFirstDayOfMonth(year: Int, month: Int): LocalDate {
        return LocalDate.of(year, month, 1)
    }

    /**
     * 获取某个月的最后一天
     */
    fun getLastDayOfMonth(year: Int, month: Int): LocalDate {
        return LocalDate.of(year, month, 1).withDayOfMonth(LocalDate.of(year, month, 1).lengthOfMonth())
    }
}