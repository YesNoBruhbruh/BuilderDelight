package me.maanraj514.builderdelight

import me.maanraj514.builderdelight.command.BuildModeCommand
import me.maanraj514.builderdelight.database.ConnectedCallback
import me.maanraj514.builderdelight.database.DatabaseManager
import me.maanraj514.builderdelight.database.sql.H2Database
import me.maanraj514.builderdelight.database.sql.SQLTableBuilder
import me.maanraj514.builderdelight.listener.BuildModeListener
import me.maanraj514.builderdelight.util.LocationUtil
import me.maanraj514.builderdelight.worldedit.FAWEListener
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.sql.Connection
import java.util.*
import kotlin.collections.HashSet

class BuilderDelight : JavaPlugin() {

    val builders = mutableSetOf<UUID>()

    private lateinit var databaseManager: DatabaseManager

    private lateinit var connection: Connection

    private val builderBlocks = HashSet<Location>()

    override fun onEnable() {
        saveDefaultConfig()

//        val licenseKey = config.getString("license-key") ?: "No License"
//        if (!License.isLicenseValid(licenseKey, this)) {
//            Bukkit.getPluginManager().disablePlugin(this)
//        }

        if (!server.pluginManager.isPluginEnabled("FastAsyncWorldEdit")) {
            server.consoleSender.sendMessage("FastAsyncWorldEdit not found! Disabling plugin...")
            server.pluginManager.disablePlugin(this)
        }

        databaseManager = DatabaseManager()

        setupDatabase()

        loadBlocks()

        FAWEListener(this)

        registerCommands()
        registerListeners()
    }

    override fun onDisable() {
        updateBuilderBlocks()

        builders.clear()
        builderBlocks.clear()

        databaseManager.disconnectAll()
    }

    fun loadBlocks() {
        connection.prepareStatement("SELECT * FROM builder_blocks").use { preparedStatement ->
            val results = preparedStatement.executeQuery()
            while (results.next()) {
                builderBlocks.add(LocationUtil.deserialize(results.getString("location")))
            }
        }
        println("Loaded ${builderBlocks.size} builder blocks")
    }

    private fun setupDatabase() {
        val dbFile = File(dataFolder, "blocks.db")
        if (!dbFile.exists()) {
            dbFile.createNewFile()
        }

        val h2Database = H2Database(dataFolder.absolutePath + "/blocks", object : ConnectedCallback {
            override fun onConnected(connection: Connection) {
                SQLTableBuilder("builder_blocks")
                    .addField("location", SQLTableBuilder.DataType.VARCHAR, 100)
                    .setPrimaryKey("location")
                    .execute(connection)
            }

            override fun onDisconnect() {
            }

        })
        connection = h2Database.getConnection()
        databaseManager.createDatabase("blocks", h2Database)
    }

    private fun registerListeners() {
        Bukkit.getPluginManager().registerEvents(BuildModeListener(this), this)
    }

    private fun registerCommands() {
        getCommand("buildmode")?.setExecutor(BuildModeCommand(this))
    }

    fun removeBlock(location: Location) {
        builderBlocks.remove(location)
    }

    fun addBlock(location: Location) {
        if (builderBlocks.contains(location)) return

        builderBlocks.add(location)
    }

    fun isBuilderBlock(location: Location): Boolean {
        return builderBlocks.contains(location)
    }

    private fun updateBuilderBlocks() {
        val statement = connection.prepareStatement("DELETE FROM builder_blocks")
        statement.execute()
        statement.close()

        for (location in builderBlocks) {
            connection.prepareStatement("INSERT INTO builder_blocks VALUES (?)").use { preparedStatement ->
                preparedStatement.setString(1, LocationUtil.serialize(location))
                preparedStatement.execute()
            }
        }
    }
}