package ai.learnstep.desktop.data.repository

import ai.learnstep.desktop.data.database.DatabaseProvider
import ai.learnstep.desktop.data.model.Chat
import java.sql.Statement
import java.time.Instant

class ChatRepository {

    fun createChat(title: String): Chat {
        val now = Instant.now().toEpochMilli()
        val sql = "INSERT INTO chats(title, created_at) VALUES(?, ?)"
        val conn = DatabaseProvider.connection()

        conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS).use { ps ->
            ps.setString(1, title)
            ps.setLong(2, now)
            ps.executeUpdate()

            ps.generatedKeys.use { rs ->
                if (rs.next()) {
                    val id = rs.getLong(1)
                    return Chat(id, title, now)
                }
            }
        }
        throw IllegalStateException("Failed to insert chat")
    }

    fun listChats(): List<Chat> {
        val sql = "SELECT id, title, created_at FROM chats ORDER BY created_at DESC"
        val conn = DatabaseProvider.connection()

        conn.prepareStatement(sql).use { ps ->
            ps.executeQuery().use { rs ->
                val list = mutableListOf<Chat>()
                while (rs.next()) {
                    list += Chat(
                        id = rs.getLong("id"),
                        title = rs.getString("title"),
                        createdAt = rs.getLong("created_at"),
                    )
                }
                return list
            }
        }
    }
}
