package me.maanraj514.builderdelight.command

import me.maanraj514.builderdelight.BuilderDelight
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ConfigReloadCommand(private val plugin: BuilderDelight) : CommandExecutor {

    override fun onCommand(sender: CommandSender, cmd: Command, p2: String, p3: Array<out String>?): Boolean {
        if (sender !is Player) return true
        if (!sender.hasPermission("builderdeight.command.reloadconfig")) return true

        plugin.saveConfig()
        plugin.reloadConfig()

        sender.sendMessage("Successfully reloaded config file!")

        return true
    }
}