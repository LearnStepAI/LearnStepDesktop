package ai.learnstep.desktop.data.model

data class Message(
    val id: Long,
    val chatId: Long,
    val role: String,
    val content: String,
    val createdAt: Long,
)
