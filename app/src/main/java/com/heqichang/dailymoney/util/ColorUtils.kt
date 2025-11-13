package com.heqichang.dailymoney.util

import android.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.Color as ComposeColor

/**
 * 颜色工具类，提供颜色生成、解析和处理功能
 */
object ColorUtils {

    // 默认的分类颜色列表
    val defaultCategoryColors = listOf(
        ComposeColor(0xFFE53935), // 红色
        ComposeColor(0xFF1E88E5), // 蓝色
        ComposeColor(0xFF43A047), // 绿色
        ComposeColor(0xFFFB8C00), // 橙色
        ComposeColor(0xFF8E24AA), // 紫色
        ComposeColor(0xFF039BE5), // 浅蓝色
        ComposeColor(0xFF7CB342), // 浅绿色
        ComposeColor(0xFFF57C00), // 深橙色
        ComposeColor(0xFF5E35B1), // 深紫色
        ComposeColor(0xFF00ACC1), // 青色
        ComposeColor(0xFF66BB6A), // 淡绿色
        ComposeColor(0xFFFFA726), // 琥珀色
        ComposeColor(0xFFEC407A), // 粉红色
        ComposeColor(0xFF26A69A), // 蓝绿色
        ComposeColor(0xFFAED581), // 浅绿色
        ComposeColor(0xFFFFD54F)  // 黄色
    )

    /**
     * 从Int颜色值获取Compose Color
     */
    fun intToComposeColor(colorInt: Int): ComposeColor {
        return ComposeColor(colorInt)
    }

    /**
     * 从Compose Color获取Int颜色值
     */
    fun composeColorToInt(color: ComposeColor): Int {
        return color.toArgb()
    }

    /**
     * 从十六进制字符串解析颜色
     */
    fun parseColor(hexString: String): ComposeColor {
        return ComposeColor(Color.parseColor(hexString))
    }

    /**
     * 将颜色转换为十六进制字符串
     */
    fun colorToHex(color: ComposeColor): String {
        val colorInt = composeColorToInt(color)
        return String.format("#%06X", 0xFFFFFF and colorInt)
    }

    /**
     * 获取随机分类颜色
     */
    fun getRandomCategoryColor(): ComposeColor {
        return defaultCategoryColors.random()
    }

    /**
     * 获取指定索引的分类颜色
     */
    fun getCategoryColorByIndex(index: Int): ComposeColor {
        return defaultCategoryColors[index % defaultCategoryColors.size]
    }

    /**
     * 调整颜色亮度
     */
    fun adjustBrightness(color: ComposeColor, factor: Float): ComposeColor {
        val colorInt = composeColorToInt(color)
        val hsv = FloatArray(3)
        Color.colorToHSV(colorInt, hsv)
        hsv[2] = Math.min(1f, hsv[2] * factor)
        return intToComposeColor(Color.HSVToColor(hsv))
    }

    /**
     * 获取颜色的互补色
     */
    fun getComplementaryColor(color: ComposeColor): ComposeColor {
        val colorInt = composeColorToInt(color)
        val r = Color.red(colorInt)
        val g = Color.green(colorInt)
        val b = Color.blue(colorInt)
        // 计算互补色：255-原颜色值
        return ComposeColor(
            red = (255 - r) / 255f,
            green = (255 - g) / 255f,
            blue = (255 - b) / 255f
        )
    }

    /**
     * 判断颜色是否为亮色（适合使用黑色文字）
     */
    fun isLightColor(color: ComposeColor): Boolean {
        val colorInt = composeColorToInt(color)
        val r = Color.red(colorInt) / 255f
        val g = Color.green(colorInt) / 255f
        val b = Color.blue(colorInt) / 255f
        // 计算相对亮度
        val luminance = 0.299 * r + 0.587 * g + 0.114 * b
        return luminance > 0.5
    }

    /**
     * 获取适合文本的颜色（根据背景色自动选择黑或白）
     */
    fun getTextColorForBackground(backgroundColor: ComposeColor): ComposeColor {
        return if (isLightColor(backgroundColor)) ComposeColor.Black else ComposeColor.White
    }
}