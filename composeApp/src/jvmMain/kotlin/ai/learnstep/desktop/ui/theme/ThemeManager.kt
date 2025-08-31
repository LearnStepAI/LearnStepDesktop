package ai.learnstep.desktop.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

enum class ThemeMode {
    LIGHT, DARK, SYSTEM
}

object ThemeManager {
    var currentTheme by mutableStateOf(ThemeMode.SYSTEM)
        private set

    fun setTheme(theme: ThemeMode) {
        currentTheme = theme
    }

    // 检测系统主题
    fun isSystemDark(): Boolean {
        return try {
            // 通过系统属性检测Windows主题
            val osName = System.getProperty("os.name").lowercase()
            when {
                osName.contains("windows") -> {
                    // Windows注册表检测（简化版）
                    // 这里应该调用Windows API检测系统主题，暂时默认根据时间判断
                    val hour = java.time.LocalTime.now().hour
                    hour < 6 || hour > 18 // 简单的时间判断逻辑
                }
                osName.contains("mac") -> {
                    // macOS系统主题检测
                    false // 占位符
                }
                else -> {
                    // Linux等其他系统
                    false // 占位符
                }
            }
        } catch (e: Exception) {
            false // 出错时默认浅色
        }
    }

    fun isDarkMode(): Boolean {
        return when (currentTheme) {
            ThemeMode.LIGHT -> false
            ThemeMode.DARK -> true
            ThemeMode.SYSTEM -> isSystemDark()
        }
    }
}
