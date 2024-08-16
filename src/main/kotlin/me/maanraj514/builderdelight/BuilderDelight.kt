package me.maanraj514.builderdelight

import me.maanraj514.builderdelight.command.BuildModeCommand
import me.maanraj514.builderdelight.listener.BuildModeListener
import me.maanraj514.builderdelight.util.BlocksFile
import me.maanraj514.builderdelight.worldedit.FAWEListener
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class BuilderDelight : JavaPlugin() {

    val builders = mutableSetOf<UUID>()
    val builderBlocks = mutableSetOf<Location>()

//    val BUILDER_BLOCK_KEY = NamespacedKey(this, "builderModeBlock")

    lateinit var blocksFile: BlocksFile

    private var saveTask = -1

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

        if (config.getBoolean("save-task.enabled", true)) {

            val seconds = config.getInt("save-task.interval", 60)

            saveTask = Bukkit.getScheduler().runTaskTimer(this, Runnable {
                blocksFile.saveBlocks(builderBlocks.toList())
                blocksFile.save()
            }, 0, (seconds * 20).toLong()).taskId
        }
    }

    override fun onDisable() {

        if (saveTask != -1) {
            Bukkit.getScheduler().cancelTask(saveTask)
        }

        blocksFile.saveBlocks(builderBlocks.toList())
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