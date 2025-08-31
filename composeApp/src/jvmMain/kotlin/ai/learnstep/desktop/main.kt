package ai.learnstep.desktop

import androidx.compose.runtime.*
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import ai.learnstep.desktop.data.database.DatabaseProvider

fun main() = application {
    // Initialize database before composition
    DatabaseProvider.init()
    
    Window(
        onCloseRequest = ::exitApplication,
        title = "LearnStep",
        state = WindowState(
            position = WindowPosition.Aligned(androidx.compose.ui.Alignment.Center),
            size = DpSize(1600.dp, 1200.dp)
        )
    ) {
        App()
    }
}
