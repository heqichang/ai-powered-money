package com.heqichang.dailymoney.util

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

/**
 * 金额工具类，提供金额格式化、解析和验证功能
 */
object AmountUtils {

    // 金额格式化器
    private val decimalFormat = DecimalFormat("#,###.00")
    private val simpleDecimalFormat = DecimalFormat("#.##")

    /**
     * 格式化金额，保留两位小数
     */
    fun formatAmount(amount: Double): String {
        return "¥${decimalFormat.format(amount)}"
    }

    /**
     * 简洁格式化金额，自动去掉末尾的零
     */
    fun formatAmountCompact(amount: Double): String {
        val formatted = simpleDecimalFormat.format(amount)
        return "¥$formatted"
    }

    /**
     * 解析金额字符串为Double
     */
    fun parseAmount(amountString: String): Double? {
        return try {
            // 移除可能的货币符号和千分位
            val cleanString = amountString.replace("[¥,]", "").trim()
            cleanString.toDouble()
        } catch (e: NumberFormatException) {
            null
        }
    }

    /**
     * 验证金额字符串是否有效
     */
    fun isValidAmount(amountString: String): Boolean {
        // 检查是否为数字且大于0
        val cleanString = amountString.replace("[¥,]", "").trim()
        return try {
            val amount = cleanString.toDouble()
            amount > 0 && amount <= 9999999.99
        } catch (e: NumberFormatException) {
            false
        }
    }

    /**
     * 格式化百分比
     */
    fun formatPercentage(value: Double, total: Double): String {
        if (total == 0.0) return "0%"
        val percentage = (value / total) * 100
        return "${String.format("%.1f", percentage)}%"
    }

    /**
     * 限制小数位数
     */
    fun limitDecimalPlaces(amount: Double, places: Int = 2): Double {
        val factor = Math.pow(10.0, places.toDouble())
        return Math.round(amount * factor) / factor
    }
}