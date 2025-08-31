package ai.learnstep.desktop.data.database

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import ai.learnstep.desktop.data.database.DatabaseMigrations

object DatabaseProvider {
    private var connection: Connection? = null

    fun init(dbPath: String = "learnstep.db") {
        if (connection != null) return
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:$dbPath")
            connection?.let { conn ->
                DatabaseMigrations.createTablesIfNeeded(conn)
            }
        } catch (e: SQLException) {
            throw RuntimeException("Failed to open database", e)
        }
    }

    fun connection(): Connection {
        return connection ?: throw IllegalStateException("Database not initialized")
    }

    fun close() {
        try {
            connection?.close()
            connection = null
        } catch (e: SQLException) {
            // Log error but don't throw to avoid issues during cleanup
            System.err.println("Failed to close database connection: ${e.message}")
        }
    }
}
