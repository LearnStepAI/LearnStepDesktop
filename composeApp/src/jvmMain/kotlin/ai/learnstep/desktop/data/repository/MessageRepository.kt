package ai.learnstep.desktop.data.repository

import ai.learnstep.desktop.data.database.DatabaseProvider
import ai.learnstep.desktop.data.model.Message
import java.sql.Statement
import java.time.Instant

class MessageRepository {

    fun addMessage(chatId: Long, role: String, content: String): Message {
        val now = Instant.now().toEpochMilli()
        val sql = "INSERT INTO messages(chat_id, role, content, created_at) VALUES(?, ?, ?, ?)"
        val conn = DatabaseProvider.connection()

        conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS).use { ps ->
            ps.setLong(1, chatId)
            ps.setString(2, role)
            ps.setString(3, content)
            ps.setLong(4, now)
            ps.executeUpdate()

            ps.generatedKeys.use { rs ->
                if (rs.next()) {
                    val id = rs.getLong(1)
                    return Message(id, chatId, role, content, now)
                }
            }
        }
        throw IllegalStateException("Failed to insert message")
    }

    fun getMessagesByChat(chatId: Long): List<Message> {
        val sql = "SELECT id, chat_id, role, content, created_at FROM messages WHERE chat_id=? ORDER BY id ASC"
        val conn = DatabaseProvider.connection()

        conn.prepareStatement(sql).use { ps ->
            ps.setLong(1, chatId)
            ps.executeQuery().use { rs ->
                val list = mutableListOf<Message>()
                while (rs.next()) {
                    list += Message(
                        id = rs.getLong("id"),
                        chatId = rs.getLong("chat_id"),
                        role = rs.getString("role"),
                        content = rs.getString("content"),
                        createdAt = rs.getLong("created_at"),
                    )
                }
                return list
            }
        }
    }
}
