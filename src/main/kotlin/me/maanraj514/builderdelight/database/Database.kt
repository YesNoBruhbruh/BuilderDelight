package me.maanraj514.builderdelight.database

import java.sql.Connection

interface Database {

    fun disconnect()

    fun isConnected(): Boolean

    fun getConnection(): Connection
}