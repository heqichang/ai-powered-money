package com.heqichang.dailymoney.util

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * 主题工具类，提供应用主题相关功能
 */
object ThemeUtils {

    // 应用主色调
    private val primaryColor = Color(0xFF1976D2)
    private val primaryDarkColor = Color(0xFF0D47A1)
    private val primaryLightColor = Color(0xFF64B5F6)
    private val accentColor = Color(0xFFFFA000)
    private val errorColor = Color(0xFFD32F2F)
    private val successColor = Color(0xFF388E3C)
    
    // 支出和收入相关颜色
    val expenseColor = Color(0xFFF44336)
    val incomeColor = Color(0xFF4CAF50)
    val balancePositiveColor = Color(0xFF4CAF50)
    val balanceNegativeColor = Color(0xFFF44336)

    // 浅色主题
    val lightColorScheme = lightColorScheme(
        primary = primaryColor,
        primaryContainer = primaryLightColor,
        secondary = accentColor,
        secondaryContainer = accentColor.copy(alpha = 0.1f),
        error = errorColor,
        background = Color(0xFFF5F5F5),
        surface = Color.White,
        onPrimary = Color.White,
        onSecondary = Color.Black,
        onBackground = Color.Black,
        onSurface = Color.Black,
        onError = Color.White
    )

    // 深色主题
    val darkColorScheme = darkColorScheme(
        primary = primaryLightColor,
        primaryContainer = primaryDarkColor,
        secondary = accentColor,
        secondaryContainer = accentColor.copy(alpha = 0.2f),
        error = errorColor,
        background = Color(0xFF121212),
        surface = Color(0xFF1E1E1E),
        onPrimary = Color.Black,
        onSecondary = Color.White,
        onBackground = Color.White,
        onSurface = Color.White,
        onError = Color.White
    )

    /**
     * 获取适合图表的颜色列表
     */
    fun getChartColors(): List<Color> {
        return listOf(
            primaryColor,
            accentColor,
            successColor,
            errorColor,
            Color(0xFF9C27B0),
            Color(0xFF00BCD4),
            Color(0xFFE91E63),
            Color(0xFF795548)
        )
    }

    /**
     * 获取支出或收入对应的颜色
     */
    fun getTypeColor(isExpense: Boolean): Color {
        return if (isExpense) expenseColor else incomeColor
    }

    /**
     * 获取余额对应的颜色
     */
    fun getBalanceColor(balance: Double): Color {
        return if (balance > 0) balancePositiveColor else balanceNegativeColor
    }

    /**
     * 获取主题色下的透明度变体
     */
    fun getColorWithAlpha(color: Color, alpha: Float): Color {
        return color.copy(alpha = alpha)
    }
}

/**
 * 扩展函数：获取主题中支出颜色
 */
val ColorScheme.expenseColor: Color
    @Composable
    get() = ThemeUtils.expenseColor

/**
 * 扩展函数：获取主题中收入颜色
 */
val ColorScheme.incomeColor: Color
    @Composable
    get() = ThemeUtils.incomeColor

/**
 * 扩展函数：获取基于交易类型的颜色
 */
fun ColorScheme.getTypeColor(isExpense: Boolean): Color {
    return if (isExpense) expenseColor else incomeColor
}