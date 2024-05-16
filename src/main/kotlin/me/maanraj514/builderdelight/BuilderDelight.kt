package me.maanraj514.builderdelight

import me.maanraj514.builderdelight.command.BuildModeCommand
import me.maanraj514.builderdelight.command.ConfigReloadCommand
import me.maanraj514.builderdelight.command.PosCommand
import me.maanraj514.builderdelight.command.TestClearCommand
import me.maanraj514.builderdelight.listener.BuildModeListener
import me.maanraj514.builderdelight.ocelot.OcelotAPI
import me.maanraj514.builderdelight.task.WorkLoadRunnable
import me.maanraj514.builderdelight.util.Auth
import me.maanraj514.builderdelight.util.BlocksFile
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

    lateinit var ocelotAPI: OcelotAPI

    lateinit var blocksFile: BlocksFile
    lateinit var workLoadRunnable: WorkLoadRunnable

    override fun onEnable() {
        saveDefaultConfig()

        Auth.licenseCheck(this)
        Auth.checkIp(this)

        registerCommands()
        registerListeners()

        WorldEditListener(this)

        ocelotAPI = OcelotAPI(this)

        blocksFile = BlocksFile(this)
        builderBlocks.addAll(blocksFile.loadBlocks()) // add all blocks from file to builderBlocks list.

        workLoadRunnable = WorkLoadRunnable()
        workLoadRunnable.runTaskTimer(this, 0L, 1L)

        server.consoleSender.sendMessage("Found WorldEdit! loading support...")
    }

    override fun onDisable() {
        workLoadRunnable.cancel()

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

        println("called removeBlock()!")
    }

    fun addBlock(block: Block) {
        val location = block.location

        if (builderBlocks.contains(location)) return // already inside.

        builderBlocks.add(location)

        println("called addBlock()!")
    }
}