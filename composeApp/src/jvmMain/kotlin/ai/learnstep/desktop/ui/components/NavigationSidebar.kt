package ai.learnstep.desktop.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Help
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ai.learnstep.desktop.viewmodel.Screen

@Composable
fun NavigationSidebar(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UserAvatar()

        Spacer(Modifier.height(24.dp))

        NavigationButtons(
            currentScreen = currentScreen,
            onNavigate = onNavigate
        )

        Spacer(Modifier.weight(1f))

        BottomNavigationButtons()
    }
}

@Composable
private fun UserAvatar() {
    Box(
        modifier = Modifier
            .size(36.dp)
            .background(MaterialTheme.colorScheme.primary, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "A",
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun NavigationButtons(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit
) {
    NavigationIcon(
        icon = Icons.Outlined.SmartToy,
        isSelected = currentScreen == Screen.Chat,
        onClick = { onNavigate(Screen.Chat) }
    )

    Spacer(Modifier.height(16.dp))

    NavigationIcon(
        icon = Icons.Outlined.Topic,
        isSelected = false,
        onClick = { /* Topics */ }
    )

    Spacer(Modifier.height(16.dp))

    NavigationIcon(
        icon = Icons.Outlined.Settings,
        isSelected = currentScreen == Screen.Settings,
        onClick = { onNavigate(Screen.Settings) }
    )
}

@Composable
private fun BottomNavigationButtons() {
    NavigationIcon(
        icon = Icons.AutoMirrored.Outlined.Help,
        isSelected = false,
        onClick = { }
    )

    Spacer(Modifier.height(16.dp))

    NavigationIcon(
        icon = Icons.Outlined.Lightbulb,
        isSelected = false,
        onClick = { }
    )

    Spacer(Modifier.height(16.dp))

    NavigationIcon(
        icon = Icons.Outlined.Apps,
        isSelected = false,
        onClick = { }
    )
}

@Composable
fun NavigationIcon(
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
    val iconColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant

    Box(
        modifier = modifier
            .size(40.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(20.dp)
        )
    }
}
