package ai.learnstep.desktop.data.repository

import ai.learnstep.desktop.data.database.DatabaseProvider
import java.sql.Statement

class SettingsRepository {

    fun get(key: String): String? {
        val sql = "SELECT value FROM settings WHERE key=?"
        val conn = DatabaseProvider.connection()

        conn.prepareStatement(sql).use { ps ->
            ps.setString(1, key)
            ps.executeQuery().use { rs ->
                return if (rs.next()) rs.getString(1) else null
            }
        }
    }

    fun set(key: String, value: String) {
        val sql = "INSERT INTO settings(key, value) VALUES(?, ?) ON CONFLICT(key) DO UPDATE SET value=excluded.value"
        val conn = DatabaseProvider.connection()

        conn.prepareStatement(sql, Statement.NO_GENERATED_KEYS).use { ps ->
            ps.setString(1, key)
            ps.setString(2, value)
            ps.executeUpdate()
        }
    }

    companion object {
        const val KEY_API = "openrouter_api_key"
        const val KEY_THEME = "theme_mode"
    }
}
