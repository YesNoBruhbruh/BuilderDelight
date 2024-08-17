package me.maanraj514.builderdelight.database

import java.sql.Connection

interface ConnectedCallback {

    fun onConnected(connection: Connection)

    fun onDisconnect()
}