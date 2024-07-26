package me.maanraj514.builderdelight

import me.maanraj514.builderdelight.command.BuildModeCommand
import me.maanraj514.builderdelight.listener.BuildModeListener
import me.maanraj514.builderdelight.util.BlocksFile
import me.maanraj514.builderdelight.worldedit.FAWEListener
import me.maanraj514.builderdelight.worldedit.WEListener
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class BuilderDelight : JavaPlugin() {

    val builders = mutableSetOf<UUID>()
    val builderBlocks = mutableListOf<Location>()

//    val BUILDER_BLOCK_KEY = NamespacedKey(this, "builderModeBlock")

    lateinit var blocksFile: BlocksFile

    override fun onEnable() {
        saveDefaultConfig()

        //TODO this is disabled as this is a dev build of the plugin.
//        val licenseKey = config.getString("license-key") ?: "No License"
//        if (!License.isLicenseValid(licenseKey, this)) {
//            Bukkit.getPluginManager().disablePlugin(this)
//        }

        val pluginManager = server.pluginManager

        for (plugin in pluginManager.plugins) {
            if (plugin.name == "FastAsyncWorldEdit") {
                FAWEListener(this)
                server.consoleSender.sendMessage("Found FastAsyncWorldEdit! loading support...")
                break
            } else if (plugin.name == "WorldEdit") {
                WEListener(this)
                server.consoleSender.sendMessage("Found WorldEdit! loading support...")
                break
            }
        }

        registerCommands()
        registerListeners()

        blocksFile = BlocksFile(this)
        builderBlocks.addAll(blocksFile.loadBlocks()) // add all blocks from file to builderBlocks list.

        server.consoleSender.sendMessage("Found FastAsyncWorldEdit! loading support...")
    }

    override fun onDisable() {
        blocksFile.saveBlocks(builderBlocks)
        blocksFile.save()
        println("builderBlocks size is ${builderBlocks.size}")

        builders.clear()
        builderBlocks.clear()
    }

    private fun registerListeners() {
        Bukkit.getPluginManager().registerEvents(BuildModeListener(this), this)
    }

    private fun registerCommands() {
        getCommand("buildmode")?.setExecutor(BuildModeCommand(this))
    }

    fun removeBlock(block: Block) {
        val location = block.location

        builderBlocks.remove(location) // doesn't error when it doesn't exist anyway
    }

    fun addBlock(block: Block) {
        val location = block.location

        if (builderBlocks.contains(location)) return // already inside.

        builderBlocks.add(location)
    }

    fun isBuilderBlock(block: Block): Boolean {
        return isBuilderBlock(block.location)
    }

    fun isBuilderBlock(location: Location): Boolean {
        return builderBlocks.contains(location)
    }
}