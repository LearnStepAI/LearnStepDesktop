package ai.learnstep.desktop.ui.theme

import java.awt.Window

object WindowThemeHelper {

    /**
     * 设置应用程序启动时的主题属性
     */
    fun initializeWindowTheme() {
        try {
            // 检测系统是否支持深色模式
            val osName = System.getProperty("os.name").lowercase()

            when {
                osName.contains("windows") -> {
                    // Windows 10/11 深色模式支持
                    if (isWindowsDarkMode()) {
                        setWindowsDarkMode(true)
                    } else {
                        setWindowsDarkMode(false)
                    }
                }
                osName.contains("mac") -> {
                    // macOS 深色模式支持
                    if (ThemeManager.isSystemDark()) {
                        System.setProperty("apple.awt.application.appearance", "NSAppearanceNameDarkAqua")
                    } else {
                        System.setProperty("apple.awt.application.appearance", "NSAppearanceNameAqua")
                    }
                }
            }
        } catch (e: Exception) {
            // 忽略错误，使用默认主题
            println("Failed to initialize window theme: ${e.message}")
        }
    }

    /**
     * 动态更新窗口主题
     */
    fun updateWindowTheme(isDark: Boolean, window: Window? = null) {
        try {
            val osName = System.getProperty("os.name").lowercase()

            when {
                osName.contains("windows") -> {
                    setWindowsDarkMode(isDark)
                    // 强制刷新窗口
                    window?.let { w ->
                        w.isVisible = true
                        w.repaint()
                    }
                }
                osName.contains("mac") -> {
                    if (isDark) {
                        System.setProperty("apple.awt.application.appearance", "NSAppearanceNameDarkAqua")
                    } else {
                        System.setProperty("apple.awt.application.appearance", "NSAppearanceNameAqua")
                    }
                }
            }
        } catch (e: Exception) {
            println("Failed to update window theme: ${e.message}")
        }
    }

    private fun isWindowsDarkMode(): Boolean {
        return try {
            // 尝试读取Windows注册表检测深色模式
            // 这是一个简化版本，实际情况可能需要更复杂的检测
            val process = ProcessBuilder(
                "reg", "query",
                "HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize",
                "/v", "AppsUseLightTheme"
            ).start()

            val result = process.inputStream.bufferedReader().readText()
            process.waitFor()

            // 如果AppsUseLightTheme为0，则是深色模式
            result.contains("AppsUseLightTheme") && result.contains("0x0")
        } catch (e: Exception) {
            // 出错时回退到ThemeManager的检测
            ThemeManager.isSystemDark()
        }
    }

    private fun setWindowsDarkMode(isDark: Boolean) {
        try {
            // 设置Java系统属性来影响窗口外观
            if (isDark) {
                System.setProperty("sun.java2d.uiScale.enabled", "true")
                System.setProperty("awt.useSystemAAFontSettings", "on")
                // 其他可能的深色模式属性
                System.setProperty("swing.defaultlaf", "javax.swing.plaf.nimbus.NimbusLookAndFeel")
            } else {
                System.setProperty("swing.defaultlaf", "javax.swing.plaf.metal.MetalLookAndFeel")
            }
        } catch (e: Exception) {
            println("Failed to set Windows dark mode: ${e.message}")
        }
    }
}
