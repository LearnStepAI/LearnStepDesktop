package ai.learnstep.desktop

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ai.learnstep.desktop.ui.AppScaffold
import ai.learnstep.desktop.ui.AppTheme
import ai.learnstep.desktop.viewmodel.ChatViewModel

@Composable
fun App() {
    val viewModel = remember { ChatViewModel() }
    
    AppTheme(themeMode = viewModel.themeMode) {
        AppScaffold(vm = viewModel)
    }
}
