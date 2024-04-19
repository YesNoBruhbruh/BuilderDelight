package me.maanraj514.builderdelight

import com.jeff_media.customblockdata.CustomBlockData
import dev.respark.licensegate.LicenseGate
import me.maanraj514.builderdelight.command.BuildModeCommand
import me.maanraj514.builderdelight.command.ConfigReloadCommand
import me.maanraj514.builderdelight.command.PosCommand
import me.maanraj514.builderdelight.listener.BuildModeListener
import me.maanraj514.builderdelight.task.ClearBlocksTask
import me.maanraj514.builderdelight.worldedit.WorldEditListener
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class BuilderDelight : JavaPlugin() {

    val builders = mutableSetOf<UUID>()

    val BUILDER_BLOCK_KEY = NamespacedKey(this, "builderModeBlock")

    private val clearBlocksTask = ClearBlocksTask(this)

    private val mm = MiniMessage.miniMessage()

    override fun onEnable() {
        saveDefaultConfig()

        val isLicenseValid = LicenseGate("a1c87")
            .verify(config.getString("license-key"), "BuilderDelight")
            .isValid

        if (!isLicenseValid) {
            val errorMessage = mm.deserialize("<red> INVALID LICENSE KEY!!!!! </red>")
            for (i in 0 until 100) {
                server.consoleSender.sendMessage(errorMessage)
            }
            Bukkit.getPluginManager().disablePlugin(this)
        }

        getCommand("buildmode")?.setExecutor(BuildModeCommand(this))

        val posCommand = PosCommand(this)
        getCommand("pos1")?.setExecutor(posCommand)
        getCommand("pos2")?.setExecutor(posCommand)
        getCommand("savePos")?.setExecutor(posCommand)

        getCommand("builderdelight-reloadconfig")?.setExecutor(ConfigReloadCommand(this))

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