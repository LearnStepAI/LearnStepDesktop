package ai.learnstep.desktop.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ai.learnstep.desktop.ui.theme.ThemeManager
import ai.learnstep.desktop.ui.theme.ThemeMode

// 浅色主题
private val LightColors = lightColorScheme(
    primary = Color(0xFF5B8DEF),
    secondary = Color(0xFF7A8BA3),
    tertiary = Color(0xFF4FC3F7),
    primaryContainer = Color(0xFFDCE8FF),
    secondaryContainer = Color(0xFFE8EDF3),
    surfaceVariant = Color(0xFFF3F5F7),
    surface = Color.White,
    background = Color(0xFFFAFAFA),
    onSurface = Color(0xFF1A1A1A),
    onBackground = Color(0xFF1A1A1A),
)

// 深色主题
private val DarkColors = darkColorScheme(
    primary = Color(0xFF82A9FF),
    secondary = Color(0xFF9BACBF),
    tertiary = Color(0xFF6DD4FF),
    primaryContainer = Color(0xFF1E3A5F),
    secondaryContainer = Color(0xFF2A2F3A),
    surfaceVariant = Color(0xFF2D2D2D),
    surface = Color(0xFF1E1E1E),
    background = Color(0xFF121212),
    onSurface = Color(0xFFE1E1E1),
    onBackground = Color(0xFFE1E1E1),
)

private val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(18.dp),
    extraLarge = RoundedCornerShape(22.dp),
)

@Composable
fun AppTheme(
    themeMode: ThemeMode? = null,
    content: @Composable () -> Unit
) {
    // 如果传入了themeMode，使用它；否则使用ThemeManager的当前主题
    val currentTheme = themeMode ?: ThemeManager.currentTheme
    val isDarkMode by remember(currentTheme) {
        derivedStateOf {
            when (currentTheme) {
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
                ThemeMode.SYSTEM -> ThemeManager.isSystemDark()
            }
        }
    }
    val colorScheme = if (isDarkMode) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        shapes = AppShapes,
        content = content
    )
}
