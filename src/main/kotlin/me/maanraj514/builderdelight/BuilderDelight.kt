package me.maanraj514.builderdelight

import me.maanraj514.builderdelight.command.BuildModeCommand
import me.maanraj514.builderdelight.command.ConfigReloadCommand
import me.maanraj514.builderdelight.command.PosCommand
import me.maanraj514.builderdelight.command.TestClearCommand
import me.maanraj514.builderdelight.listener.BuildModeListener
import me.maanraj514.builderdelight.task.ClearBlocksTask
import me.maanraj514.builderdelight.util.BlocksFile
import me.maanraj514.builderdelight.util.License
import me.maanraj514.builderdelight.worldedit.WorldEditListener
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
    private lateinit var clearBlocksTask: ClearBlocksTask

    override fun onEnable() {
        saveDefaultConfig()

        val licenseKey = config.getString("license-key") ?: "No License"
        if (!License.isLicenseValid(licenseKey, this)) {
            Bukkit.getPluginManager().disablePlugin(this)
        }

        registerCommands()
        registerListeners()

        WorldEditListener(this)

        blocksFile = BlocksFile(this)
        builderBlocks.addAll(blocksFile.loadBlocks()) // add all blocks from file to builderBlocks list.

        clearBlocksTask = ClearBlocksTask(this)
        clearBlocksTask.runTaskTimer(this, config.getLong("delay"), config.getLong("interval"))

        server.consoleSender.sendMessage("Found WorldEdit! loading support...")
    }

    override fun onDisable() {
        blocksFile.saveBlocks(builderBlocks)
        blocksFile.save()
        println("builderBlocks size is ${builderBlocks.size}")

        for (id in clearBlocksTask.tasks) {
            Bukkit.getScheduler().cancelTask(id)
        }
        clearBlocksTask.tasks.clear()

        clearBlocksTask.cancel()

        builders.clear()
        builderBlocks.clear()
    }

    private fun registerListeners() {
        Bukkit.getPluginManager().registerEvents(BuildModeListener(this), this)
    }

    private fun registerCommands() {
        getCommand("buildmode")?.setExecutor(BuildModeCommand(this))

        val posCommand = PosCommand(this)
        getCommand("pos1")?.setExecutor(posCommand)
        getCommand("pos2")?.setExecutor(posCommand)
        getCommand("savePos")?.setExecutor(posCommand)

        getCommand("builderdelight-reloadconfig")?.setExecutor(ConfigReloadCommand(this))
        getCommand("testclear")?.setExecutor(TestClearCommand(this))
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