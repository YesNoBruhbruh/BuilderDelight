package me.maanraj514.builderdelight

import com.jeff_media.customblockdata.CustomBlockData
import me.maanraj514.builderdelight.command.BuildModeCommand
import me.maanraj514.builderdelight.listener.BuildModeListener
import me.maanraj514.builderdelight.worldedit.WorldEditListener
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class BuilderDelight : JavaPlugin() {

    val builders = mutableSetOf<UUID>()

    val BUILDER_BLOCK_KEY = NamespacedKey(this, "builderModeBlock")

    override fun onEnable() {
        getCommand("buildmode")?.setExecutor(BuildModeCommand(this))

        CustomBlockData.registerListener(this)
        Bukkit.getPluginManager().registerEvents(BuildModeListener(this), this)

        if (Bukkit.getPluginManager().getPlugin("WorldEdit") != null) {
            WorldEditListener(this)
        }
    }

    override fun onDisable() {
    }
}