package ai.learnstep.desktop.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import ai.learnstep.desktop.data.model.Chat
import ai.learnstep.desktop.data.model.Message
import ai.learnstep.desktop.data.repository.ChatRepository
import ai.learnstep.desktop.data.repository.MessageRepository
import ai.learnstep.desktop.data.repository.SettingsRepository
import ai.learnstep.desktop.net.model.ChatMessage
import ai.learnstep.desktop.net.OpenRouterClient
import ai.learnstep.desktop.ui.theme.ThemeManager
import ai.learnstep.desktop.ui.theme.ThemeMode
import kotlinx.coroutines.*

enum class Screen { Chat, Settings }

class ChatViewModel {
    // Data layer
    private val chatRepository = ChatRepository()
    private val messageRepository = MessageRepository()
    private val settingsRepository = SettingsRepository()
    private val client = OpenRouterClient { apiKey.ifBlank { null } }

    // Coroutine scope for async work
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    // UI State
    var apiKey by mutableStateOf("")
        private set

    var models by mutableStateOf<List<String>>(emptyList())
        private set

    var selectedModel by mutableStateOf("")
        private set

    var chats by mutableStateOf<List<Chat>>(chatRepository.listChats())
        private set

    var currentChatId by mutableStateOf(chats.firstOrNull()?.id)
        private set

    var messages by mutableStateOf<List<Message>>(loadCurrentMessages())
        private set

    var input by mutableStateOf("")
        private set

    var loading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    var currentScreen by mutableStateOf(Screen.Chat)
        private set

    var themeMode by mutableStateOf(ThemeMode.SYSTEM)
        private set

    init {
        initializeApiKey()
        initializeTheme()
        refreshModels()
    }

    // Public API
    fun updateApiKey(newKey: String) {
        apiKey = newKey
    }

    fun saveApiKey() {
        settingsRepository.set(SettingsRepository.KEY_API, apiKey)
        refreshModels()
    }

    fun updateTheme(newTheme: ThemeMode) {
        themeMode = newTheme
        ThemeManager.setTheme(newTheme)
        settingsRepository.set(SettingsRepository.KEY_THEME, newTheme.name)
    }

    fun updateInput(text: String) {
        input = text
    }

    fun selectModel(model: String) {
        selectedModel = model
    }

    fun refreshModels() {
        if (apiKey.isBlank()) return

        scope.launch {
            handleModelsLoading()
        }
    }

    fun createChatAndSelect(title: String = "New Chat") {
        val chat = chatRepository.createChat(title)
        refreshChatsState()
        selectChat(chat.id)
    }

    fun selectChat(chatId: Long) {
        currentChatId = chatId
        messages = messageRepository.getMessagesByChat(chatId)
    }

    fun send() {
        val chatId = ensureChatExists() ?: return
        val messageText = input.trim()

        if (!isValidForSending(messageText)) return

        prepareForSending(chatId, messageText)

        scope.launch {
            handleMessageSending(chatId)
        }
    }

    fun navigate(screen: Screen) {
        currentScreen = screen
    }

    // Private helper methods
    private fun initializeApiKey() {
        apiKey = settingsRepository.get(SettingsRepository.KEY_API)
            ?: System.getenv("OPENROUTER_API_KEY")
            ?: ""
    }

    private fun initializeTheme() {
        val savedTheme = settingsRepository.get(SettingsRepository.KEY_THEME)
        themeMode = try {
            ThemeMode.valueOf(savedTheme ?: ThemeMode.SYSTEM.name)
        } catch (e: IllegalArgumentException) {
            ThemeMode.SYSTEM
        }
        ThemeManager.setTheme(themeMode)
    }

    private fun loadCurrentMessages(): List<Message> {
        return currentChatId?.let { messageRepository.getMessagesByChat(it) } ?: emptyList()
    }

    private suspend fun handleModelsLoading() {
        val result = client.listModels()
        withContext(Dispatchers.Main) {
            result.onSuccess { modelList ->
                updateModelsState(modelList.map { it.id })
            }.onFailure { exception ->
                error = exception.message
            }
        }
    }

    private fun updateModelsState(modelList: List<String>) {
        models = modelList
        if (selectedModel.isBlank() && models.isNotEmpty()) {
            selectedModel = models.first()
        }
    }

    private fun ensureChatExists(): Long? {
        return currentChatId ?: run {
            createChatAndSelect()
            currentChatId
        }
    }

    private fun isValidForSending(messageText: String): Boolean {
        return messageText.isNotEmpty() && selectedModel.isNotBlank()
    }

    private fun prepareForSending(chatId: Long, messageText: String) {
        input = ""
        error = null

        messageRepository.addMessage(chatId, "user", messageText)
        messages = messageRepository.getMessagesByChat(chatId)
        loading = true
    }

    private suspend fun handleMessageSending(chatId: Long) {
        val chatMessages = messages.map { ChatMessage(it.role, it.content) }
        val result = client.chatCompletion(
            model = selectedModel,
            messages = chatMessages
        )

        withContext(Dispatchers.Main) {
            result.onSuccess { content ->
                handleSuccessfulResponse(chatId, content)
            }.onFailure { exception ->
                error = exception.message
            }
            loading = false
        }
    }

    private fun handleSuccessfulResponse(chatId: Long, content: String) {
        messageRepository.addMessage(chatId, "assistant", content)
        messages = messageRepository.getMessagesByChat(chatId)
    }

    private fun refreshChatsState() {
        chats = chatRepository.listChats()
    }
}

