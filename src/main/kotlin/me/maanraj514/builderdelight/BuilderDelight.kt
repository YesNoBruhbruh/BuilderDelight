package me.maanraj514.builderdelight

import me.maanraj514.builderdelight.command.BuildModeCommand
import me.maanraj514.builderdelight.listener.BuildModeListener
import me.maanraj514.builderdelight.util.BlocksFile
import me.maanraj514.builderdelight.util.License
import me.maanraj514.builderdelight.worldedit.FAWEListener
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

//        val licenseKey = config.getString("license-key") ?: "No License"
//        if (!License.isLicenseValid(licenseKey, this)) {
//            Bukkit.getPluginManager().disablePlugin(this)
//        }

        if (!server.pluginManager.isPluginEnabled("FastAsyncWorldEdit")) {
            server.consoleSender.sendMessage("FastAsyncWorldEdit not found! Disabling plugin...")
            server.pluginManager.disablePlugin(this)
        }

        FAWEListener(this)

        registerCommands()
        registerListeners()

        blocksFile = BlocksFile(this)
        builderBlocks.addAll(blocksFile.loadBlocks()) // add all blocks from file to builderBlocks list.
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