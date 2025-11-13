package com.heqichang.dailymoney.ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtons
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.heqichang.dailymoney.util.AmountUtils
import com.heqichang.dailymoney.util.ThemeUtils
import com.heqichang.dailymoney.ui.viewmodel.StatisticsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(navController: NavController) {
    // 使用ViewModel
    val viewModel: StatisticsViewModel = viewModel()
    
    // 从ViewModel获取状态
    val selectedYear by viewModel.selectedYear
    val monthlyExpenses by viewModel.monthlyExpenses
    val monthlyIncomes by viewModel.monthlyIncomes
    val totalExpense by viewModel.totalExpense
    val totalIncome by viewModel.totalIncome
    val isLoading by viewModel.isLoading
    val error by viewModel.error
    
    // UI状态
    var statisticsType by remember {
        mutableStateOf("支出") // 支出、收入、对比
    }
    var categoryType by remember {
        mutableStateOf("支出") // 支出、收入
    }
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("统计") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 年份选择器
            YearSelector(
                selectedYear = selectedYear,
                years = viewModel.getAvailableYears(),
                onYearChange = viewModel::selectYear
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 错误提示
            error?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            
            // 加载状态
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                // 收支概览卡片
                OverviewCard(totalIncome = totalIncome, totalExpense = totalExpense)
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // 统计类型选择
                SegmentedButtons(
                    modifier = Modifier.fillMaxWidth(),
                    options = listOf(
                        "支出",
                        "收入",
                        "对比"
                    ),
                    onOptionSelect = { statisticsType = it },
                    selectedOption = statisticsType
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // 根据选择的类型显示不同的统计图表
                when (statisticsType) {
                    "支出" -> BarChart("月度支出统计", monthlyExpenses, ThemeUtils.getExpenseColor())
                    "收入" -> BarChart("月度收入统计", monthlyIncomes, ThemeUtils.getIncomeColor())
                    "对比" -> ComparisonChart(monthlyExpenses, monthlyIncomes)
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // 分类统计区域
                Text(
                    text = "分类统计",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.align(Alignment.Start)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // 分类类型选择
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CategoryTypeButton(
                        text = "支出",
                        isSelected = categoryType == "支出",
                        onClick = { categoryType = "支出" },
                        color = ThemeUtils.getExpenseColor()
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    CategoryTypeButton(
                        text = "收入",
                        isSelected = categoryType == "收入",
                        onClick = { categoryType = "收入" },
                        color = ThemeUtils.getIncomeColor()
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // 分类统计图表（暂时显示占位信息）
                CategoryStatisticsCard(type = categoryType)
            }
        }
    }
}

@Composable
fun YearSelector(selectedYear: Int, years: List<Int>, onYearChange: (Int) -> Unit) {
    Text(
        text = "选择年份",
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.align(Alignment.Start).padding(bottom = 8.dp)
    )
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(years) { year ->
            YearButton(
                year = year,
                isSelected = year == selectedYear,
                onClick = { onYearChange(year) }
            )
        }
    }
}

@Composable
fun YearButton(year: Int, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = year.toString(),
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp),
            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun OverviewCard(totalIncome: Double, totalExpense: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "收支概览",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // 收入
                StatItem(
                    label = "总收入",
                    value = AmountUtils.formatCurrency(totalIncome),
                    color = ThemeUtils.getIncomeColor()
                )
                // 支出
                StatItem(
                    label = "总支出",
                    value = AmountUtils.formatCurrency(totalExpense),
                    color = ThemeUtils.getExpenseColor()
                )
                // 结余
                StatItem(
                    label = "结余",
                    value = AmountUtils.formatCurrency(totalIncome - totalExpense),
                    color = ThemeUtils.getBalanceColor(totalIncome - totalExpense)
                )
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
fun BarChart(title: String, data: List<Pair<String, Double>>, barColor: Color) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Start).padding(bottom = 16.dp)
            )
            
            if (data.isEmpty()) {
                Text(
                    text = "暂无数据",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .wrapContentHeight(Alignment.CenterVertically),
                    textAlign = TextAlign.Center
                )
            } else {
                // 计算最大值用于缩放
                val maxValue = data.maxOfOrNull { it.second } ?: 1.0
                val chartHeight = 200.dp
                
                Canvas(modifier = Modifier
                    .fillMaxWidth()
                    .height(chartHeight)
                ) {
                    val canvasWidth = size.width
                    val canvasHeight = size.height
                    val barWidth = (canvasWidth / (data.size * 1.5)).coerceAtMost(30.dp.toPx())
                    
                    data.forEachIndexed { index, (month, value) ->
                        val barHeight = (value / maxValue) * canvasHeight * 0.8 // 留20%空间
                        val x = index * (barWidth * 1.5) + (barWidth / 2)
                        val y = canvasHeight - barHeight
                        
                        // 绘制柱状图
                        drawRoundRect(
                            color = barColor.copy(alpha = 0.8f),
                            topLeft = Offset(x - barWidth / 2, y),
                            size = Size(barWidth, barHeight),
                            cornerRadius = CornerRadius(4.dp.toPx())
                        )
                        
                        // 绘制月份标签
                        drawContext.canvas.nativeCanvas.drawText(
                            month,
                            x,
                            canvasHeight + 20.dp.toPx(),
                            android.graphics.Paint().apply {
                                color = android.graphics.Color.parseColor("#000000")
                                textSize = 12.sp.toPx()
                                textAlign = android.graphics.Paint.Align.CENTER
                            }
                        )
                        
                        // 绘制数值标签（只显示部分，避免拥挤）
                        if (index % 2 == 0) {
                            drawContext.canvas.nativeCanvas.drawText(
                                AmountUtils.formatCurrency(value),
                                x,
                                y - 10.dp.toPx(),
                                android.graphics.Paint().apply {
                                    color = android.graphics.Color.parseColor("#000000")
                                    textSize = 10.sp.toPx()
                                    textAlign = android.graphics.Paint.Align.CENTER
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ComparisonChart(expenses: List<Pair<String, Double>>, incomes: List<Pair<String, Double>>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "收支对比",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Start).padding(bottom = 16.dp)
            )
            
            if (expenses.isEmpty() || incomes.isEmpty()) {
                Text(
                    text = "暂无数据",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .wrapContentHeight(Alignment.CenterVertically),
                    textAlign = TextAlign.Center
                )
            } else {
                // 计算最大值用于缩放
                val maxExpense = expenses.maxOfOrNull { it.second } ?: 1.0
                val maxIncome = incomes.maxOfOrNull { it.second } ?: 1.0
                val maxValue = maxOf(maxExpense, maxIncome)
                val chartHeight = 200.dp
                
                Canvas(modifier = Modifier
                    .fillMaxWidth()
                    .height(chartHeight)
                ) {
                    val canvasWidth = size.width
                    val canvasHeight = size.height
                    val barWidth = (canvasWidth / (expenses.size * 3)).coerceAtMost(15.dp.toPx())
                    
                    expenses.forEachIndexed { index, (month, value) ->
                        val barHeight = (value / maxValue) * canvasHeight * 0.8
                        val x = index * (barWidth * 3) + barWidth / 2
                        val y = canvasHeight - barHeight
                        
                        // 绘制支出柱状图
                        drawRoundRect(
                            color = ThemeUtils.getExpenseColor().copy(alpha = 0.8f),
                            topLeft = Offset(x - barWidth / 2, y),
                            size = Size(barWidth, barHeight),
                            cornerRadius = CornerRadius(4.dp.toPx())
                        )
                    }
                    
                    incomes.forEachIndexed { index, (month, value) ->
                        val barHeight = (value / maxValue) * canvasHeight * 0.8
                        val x = index * (barWidth * 3) + barWidth * 1.5
                        val y = canvasHeight - barHeight
                        
                        // 绘制收入柱状图
                        drawRoundRect(
                            color = ThemeUtils.getIncomeColor().copy(alpha = 0.8f),
                            topLeft = Offset(x - barWidth / 2, y),
                            size = Size(barWidth, barHeight),
                            cornerRadius = CornerRadius(4.dp.toPx())
                        )
                        
                        // 绘制月份标签
                        drawContext.canvas.nativeCanvas.drawText(
                            month,
                            index * (barWidth * 3) + barWidth,
                            canvasHeight + 20.dp.toPx(),
                            android.graphics.Paint().apply {
                                color = android.graphics.Color.parseColor("#000000")
                                textSize = 12.sp.toPx()
                                textAlign = android.graphics.Paint.Align.CENTER
                            }
                        )
                    }
                }
                
                // 图例
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    LegendItem(color = ThemeUtils.getExpenseColor(), label = "支出")
                    Spacer(modifier = Modifier.width(24.dp))
                    LegendItem(color = ThemeUtils.getIncomeColor(), label = "收入")
                }
            }
        }
    }
}

@Composable
fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun CategoryTypeButton(text: String, isSelected: Boolean, onClick: () -> Unit, color: Color) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) color else MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 8.dp),
            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun CategoryStatisticsCard(type: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${type}分类统计",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Start).padding(bottom = 16.dp)
            )
            
            // 简化的分类统计，显示占位信息
            // 实际项目中应该使用饼图或柱状图展示分类数据
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .wrapContentHeight(Alignment.CenterVertically)
            ) {
                Text(
                    text = "分类统计功能正在开发中...",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}