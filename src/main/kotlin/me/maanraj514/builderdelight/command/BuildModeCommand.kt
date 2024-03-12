package me.maanraj514.builderdelight.command

import me.maanraj514.builderdelight.BuilderDelight
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class BuildModeCommand(private val plugin: BuilderDelight) : CommandExecutor {
    override fun onCommand(sender: CommandSender, p1: Command, p2: String, p3: Array<out String>?): Boolean {
        if (sender !is Player) return true
        if (!sender.hasPermission("builderdelight.command.buildmode")) return true

        val uuid = sender.uniqueId

        if (plugin.builders.contains(uuid)) {
            plugin.builders.remove(uuid)
            sender.sendMessage("You are no longer in build mode.")
        } else {
            plugin.builders.add(uuid)
            sender.sendMessage("You are now in build mode.")
        }
        return true
    }
}