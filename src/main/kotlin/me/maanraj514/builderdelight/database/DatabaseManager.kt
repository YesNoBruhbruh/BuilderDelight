package me.maanraj514.builderdelight.database

class DatabaseManager {

    private val databases = mutableMapOf<String, Database>()

    fun createDatabase(databaseName: String, database: Database) {
        databases.putIfAbsent(databaseName, database)
    }

    fun disconnectDatabase(databaseName: String) {
        databases[databaseName]?.disconnect()
    }

    fun deleteDatabase(databaseName: String) {
        databases.remove(databaseName)
    }

    fun getDatabase(databaseName: String): Database? {
        return databases[databaseName]
    }

    fun disconnectAll() {
        if (databases.isEmpty()) return

        for (database in databases.values) {
            database.disconnect()
        }
    }
}