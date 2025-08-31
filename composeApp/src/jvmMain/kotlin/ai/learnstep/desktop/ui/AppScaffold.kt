@file:OptIn(ExperimentalMaterial3Api::class)

package ai.learnstep.desktop.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ai.learnstep.desktop.ui.components.AssistantListPanel
import ai.learnstep.desktop.ui.components.NavigationSidebar
import ai.learnstep.desktop.ui.screens.ChatScreen
import ai.learnstep.desktop.ui.screens.SettingsScreen
import ai.learnstep.desktop.viewmodel.ChatViewModel
import ai.learnstep.desktop.viewmodel.Screen

@Composable
fun AppScaffold(vm: ChatViewModel) {
    LaunchedEffect(vm.apiKey) {
        vm.refreshModels()
    }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(vm.error) {
        vm.error?.let {
            snackbarHostState.showSnackbar("错误: $it")
        }
    }

    Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Row(Modifier.fillMaxSize()) {
            NavigationSidebar(
                currentScreen = vm.currentScreen,
                onNavigate = vm::navigate,
                modifier = Modifier.width(60.dp)
            )

            when (vm.currentScreen) {
                Screen.Chat -> {
                    ChatScreenWithSidebar(vm)
                }
                Screen.Settings -> {
                    SettingsScreen(
                        apiKey = vm.apiKey,
                        onApiKeyChange = vm::updateApiKey,
                        onSave = vm::saveApiKey,
                        themeMode = vm.themeMode,
                        onThemeChange = vm::updateTheme,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        SnackbarHost(
            snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp)
        )
    }
}

@Composable
private fun ChatScreenWithSidebar(vm: ChatViewModel) {
    Row(modifier = Modifier.fillMaxSize()) {
        AssistantListPanel(
            chats = vm.chats,
            currentChatId = vm.currentChatId,
            onSelectChat = vm::selectChat,
            onNewAssistant = vm::createChatAndSelect,
            modifier = Modifier.width(280.dp)
        )

        VerticalDivider(color = MaterialTheme.colorScheme.outline, thickness = 1.dp)

        ChatScreen(
            messages = vm.messages.map { it.role to it.content },
            input = vm.input,
            onInput = vm::updateInput,
            onSend = vm::send,
            loading = vm.loading,
            selectedModel = vm.selectedModel,
            modifier = Modifier.weight(1f)
        )
    }
}
