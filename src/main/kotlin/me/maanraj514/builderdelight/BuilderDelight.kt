package me.maanraj514.builderdelight

import com.jeff_media.customblockdata.CustomBlockData
import me.maanraj514.builderdelight.command.BuildModeCommand
import me.maanraj514.builderdelight.command.PosCommand
import me.maanraj514.builderdelight.listener.BuildModeListener
import me.maanraj514.builderdelight.task.ClearBlocksTask
import me.maanraj514.builderdelight.worldedit.WorldEditListener
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class BuilderDelight : JavaPlugin() {

    val builders = mutableSetOf<UUID>()

    val BUILDER_BLOCK_KEY = NamespacedKey(this, "builderModeBlock")

    private val clearBlocksTask = ClearBlocksTask(this)

    override fun onEnable() {
        saveDefaultConfig()

        getCommand("buildmode")?.setExecutor(BuildModeCommand(this))

        val posCommand = PosCommand(this)
        getCommand("pos1")?.setExecutor(posCommand)
        getCommand("pos2")?.setExecutor(posCommand)
        getCommand("savePos")?.setExecutor(posCommand)

        CustomBlockData.registerListener(this)
        Bukkit.getPluginManager().registerEvents(BuildModeListener(this), this)

        if (Bukkit.getPluginManager().getPlugin("WorldEdit") != null) {
            WorldEditListener(this)
        }

        clearBlocksTask.runTaskTimer(this, config.getInt("delay").toLong(), config.getInt("interval").toLong())
    }

    override fun onDisable() {
    }
}