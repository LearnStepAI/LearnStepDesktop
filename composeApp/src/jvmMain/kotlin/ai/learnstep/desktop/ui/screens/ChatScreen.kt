package ai.learnstep.desktop.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ai.learnstep.desktop.ui.components.*

@Composable
fun ChatScreen(
    messages: List<Pair<String, String>>,
    input: String,
    onInput: (String) -> Unit,
    onSend: () -> Unit,
    loading: Boolean,
    selectedModel: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        ChatHeader(selectedModel)

        HorizontalDivider(color = Color(0xFFE5E5E5))

        ChatContent(
            messages = messages,
            loading = loading,
            modifier = Modifier.weight(1f)
        )

        ChatInputArea(
            input = input,
            onInput = onInput,
            onSend = onSend,
            loading = loading
        )
    }
}
