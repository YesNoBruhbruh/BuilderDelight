package me.maanraj514.builderdelight.database.sql

import me.maanraj514.builderdelight.database.ConnectedCallback
import me.maanraj514.builderdelight.database.Database
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class H2Database(path: String, private val connectedCallback: ConnectedCallback) : Database {

    private var connection: Connection

    init {
        try {
            Class.forName("org.h2.Driver")
            connection = DriverManager.getConnection("jdbc:h2:$path")

            if (isConnected()) {
                connectedCallback.onConnected(connection)
            }
        } catch (e: SQLException) {
            throw RuntimeException(e)
        }
    }

    override fun disconnect() {
        if (isConnected()) {
            try {
                connection.close()
                connectedCallback.onDisconnect()
            } catch (e: SQLException) {
                throw RuntimeException(e)
            }
        }
    }

    override fun isConnected(): Boolean {
        return try {
            !connection.isClosed
        } catch (e: SQLException) {
            false
        }
    }

    override fun getConnection(): Connection {
        return connection
    }
}