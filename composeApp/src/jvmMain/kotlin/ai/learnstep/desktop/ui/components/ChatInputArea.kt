package ai.learnstep.desktop.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ChatInputArea(
    input: String,
    onInput: (String) -> Unit,
    onSend: () -> Unit,
    loading: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

        Spacer(Modifier.height(16.dp))

        InputRow(
            input = input,
            onInput = onInput,
            onSend = onSend,
            loading = loading
        )

        // 移除了 InputHint，节省空间
    }
}

@Composable
private fun InputRow(
    input: String,
    onInput: (String) -> Unit,
    onSend: () -> Unit,
    loading: Boolean
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            InputToolbar()

            Spacer(Modifier.width(12.dp))

            MessageTextField(
                input = input,
                onInput = onInput,
                onSend = onSend,
                loading = loading,
                modifier = Modifier.weight(1f)
            )

            Spacer(Modifier.width(12.dp))

            SendButton(
                onClick = { if (input.isNotBlank()) onSend() },
                loading = loading,
                enabled = input.isNotBlank()
            )
        }
    }
}

@Composable
private fun InputToolbar() {
    Row(
        modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp) // 减少间距让工具栏更紧凑
    ) {
        ToolButton(Icons.Outlined.Mood) { /* TODO: 表情 */ }
        ToolButton(Icons.Outlined.AttachFile) { /* TODO: 附件 */ }
        ToolButton(Icons.Outlined.Image) { /* TODO: 图片 */ }
        ToolButton(Icons.Outlined.Link) { /* TODO: 链接 */ }
        ToolButton(Icons.Outlined.Code) { /* TODO: 代码 */ }
    }
}

@Composable
private fun MessageTextField(
    input: String,
    onInput: (String) -> Unit,
    onSend: () -> Unit,
    loading: Boolean,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = input,
        onValueChange = onInput,
        placeholder = {
            Text(
                "在这里输入消息...",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp
            )
        },
        modifier = modifier
            .heightIn(min = 48.dp, max = 120.dp) // 设置最小和最大高度
            .onPreviewKeyEvent { e ->
                when {
                    e.type == KeyEventType.KeyDown && e.key == Key.Enter && !e.isShiftPressed -> {
                        if (!loading && input.isNotBlank()) onSend()
                        true
                    }
                    else -> false
                }
            },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(8.dp),
        minLines = 1,
        maxLines = 4,
        textStyle = TextStyle(
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    )
}

@Composable
private fun SendButton(
    onClick: () -> Unit,
    loading: Boolean,
    enabled: Boolean
) {
    if (loading) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    } else {
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .size(48.dp)
                .background(
                    if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                    CircleShape
                ),
            enabled = enabled
        ) {
            Icon(
                Icons.AutoMirrored.Filled.Send,
                contentDescription = "发送",
                tint = if (enabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}


@Composable
fun ToolButton(
    icon: ImageVector,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(36.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )
    }
}
