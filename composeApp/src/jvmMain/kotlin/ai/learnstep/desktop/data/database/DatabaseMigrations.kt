package ai.learnstep.desktop.data.database

import java.sql.Connection

object DatabaseMigrations {
    fun createTablesIfNeeded(conn: Connection) {
        createChatsTable(conn)
        createMessagesTable(conn)
        createSettingsTable(conn)
    }

    private fun createChatsTable(conn: Connection) {
        val sql = """
            CREATE TABLE IF NOT EXISTS chats (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                created_at INTEGER NOT NULL
            );
        """.trimIndent()

        conn.createStatement().use { it.execute(sql) }
    }

    private fun createMessagesTable(conn: Connection) {
        val sql = """
            CREATE TABLE IF NOT EXISTS messages (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                chat_id INTEGER NOT NULL,
                role TEXT NOT NULL,
                content TEXT NOT NULL,
                created_at INTEGER NOT NULL,
                FOREIGN KEY(chat_id) REFERENCES chats(id)
            );
        """.trimIndent()

        conn.createStatement().use { it.execute(sql) }
    }

    private fun createSettingsTable(conn: Connection) {
        val sql = """
            CREATE TABLE IF NOT EXISTS settings (
                key TEXT PRIMARY KEY,
                value TEXT
            );
        """.trimIndent()

        conn.createStatement().use { it.execute(sql) }
    }
}
