import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
}

kotlin {
    jvm()
    // Ensure JVM target compatibility
    jvmToolchain(17)

    // Explicitly set JVM target for IDE/Kotlin LS to avoid 1.8 default
    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            // Markdown renderer is multiplatform, used to display AI responses
            implementation(libs.markdownRenderer)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            // Explicitly add Compose modules to jvmMain to improve IDE resolution for androidx.* in jvmMain
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            // Material icons
            implementation(compose.materialIconsExtended)
            // Markdown renderer used directly from jvmMain composables
            implementation(libs.markdownRenderer)
            implementation(libs.kotlinx.coroutinesSwing)
            // HTTP client for OpenRouter API
            implementation(libs.okhttp)
            // JSON parsing
            implementation(libs.orgJson)
            // SQLite for local persistence
            implementation(libs.sqliteJdbc)
        }
    }
}


compose.desktop {
    application {
        mainClass = "ai.learnstep.desktop.MainKt"

        // 添加JVM参数以支持更好的主题处理
        jvmArgs += listOf(
            "-Dfile.encoding=UTF-8",
            "-Dswing.aatext=true",
            "-Dawt.useSystemAAFontSettings=on",
            "-Dsun.java2d.uiScale.enabled=true"
        )

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "ai.learnstep.desktop"
            packageVersion = "1.0.0"
        }
    }
}
