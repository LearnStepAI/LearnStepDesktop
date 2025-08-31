package ai.learnstep.desktop.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ai.learnstep.desktop.data.model.Chat

@Composable
fun AssistantListPanel(
    chats: List<Chat>,
    currentChatId: Long?,
    onSelectChat: (Long) -> Unit,
    onNewAssistant: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        AssistantPanelHeader()

        Spacer(Modifier.height(16.dp))

        SearchBar()

        Spacer(Modifier.height(16.dp))

        AssistantList(
            chats = chats,
            currentChatId = currentChatId,
            onSelectChat = onSelectChat,
            onNewAssistant = onNewAssistant,
            modifier = Modifier.weight(1f)
        )

        Spacer(Modifier.height(16.dp))

        AddAssistantButton(onNewAssistant)
    }
}

@Composable
private fun AssistantPanelHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Assistants",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )

        HeaderActions()
    }
}

@Composable
private fun HeaderActions() {
    Row {
        Text(
            text = "Topics",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.clickable { }
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = "Settings",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.clickable { }
        )
    }
}

@Composable
private fun SearchBar() {
    OutlinedTextField(
        value = "",
        onValueChange = { },
        placeholder = {
            Text(
                "Search",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp
            )
        },
        leadingIcon = {
            Icon(
                Icons.Outlined.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp)
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            focusedBorderColor = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 40.dp), // 限制搜索框最大高度
        shape = RoundedCornerShape(8.dp),
        singleLine = true // 确保单行显示
    )
}

@Composable
private fun AssistantList(
    chats: List<Chat>,
    currentChatId: Long?,
    onSelectChat: (Long) -> Unit,
    onNewAssistant: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier.verticalScroll(scrollState)
    ) {
        // 预定义助手
        PredefinedAssistants(
            currentChatId = currentChatId,
            onNewAssistant = onNewAssistant
        )

        // 用户会话
        chats.forEach { chat ->
            AssistantItem(
                emoji = "💬",
                name = chat.title,
                isSelected = chat.id == currentChatId,
                onClick = { onSelectChat(chat.id) }
            )
        }
    }
}

@Composable
private fun PredefinedAssistants(
    currentChatId: Long?,
    onNewAssistant: () -> Unit
) {
    val assistants = listOf(
        "🤖" to "Default Assistant",
        "🛍️" to "Merchant Operations",
        "🌐" to "Web page generation",
        "🧜‍♀️" to "Mermaid Diagram",
        "🔒" to "Password Generator",
        "🧑‍💻" to "Frontend Development Expert",
        "✅" to "Test Engineer",
        "🎨" to "UX/UI Developer",
        "📊" to "Excel Sheet",
        "🐕" to "Pet Behaviorist",
        "💼" to "Career Guidance",
        "📈" to "Business Data Analysis",
        "🦠" to "Viral Copywriting",
        "📱" to "Marketing Strategy"
    )

    assistants.forEach { (emoji, name) ->
        AssistantItem(
            emoji = emoji,
            name = name,
            isSelected = name == "Default Assistant" && currentChatId == null,
            onClick = {
                if (name == "Default Assistant") {
                    onNewAssistant()
                }
            }
        )
    }
}

@Composable
fun AssistantItem(
    emoji: String,
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
    val textColor = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = emoji,
            fontSize = 16.sp,
            modifier = Modifier.padding(end = 12.dp)
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                color = textColor,
                maxLines = 1
            )
            // 移除了显示数字"1"的代码
        }
    }
    Spacer(Modifier.height(4.dp))
}

@Composable
private fun AddAssistantButton(onNewAssistant: () -> Unit) {
    OutlinedButton(
        onClick = onNewAssistant,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Color(0xFF4CAF50)
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E5E5)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Icon(
            Icons.Outlined.Add,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text("Add Assistant", fontSize = 14.sp)
    }
}
