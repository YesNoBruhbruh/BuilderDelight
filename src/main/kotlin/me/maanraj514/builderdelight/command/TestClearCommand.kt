package me.maanraj514.builderdelight.command

import me.maanraj514.builderdelight.BuilderDelight
import net.kyori.adventure.text.Component
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TestClearCommand(private val plugin: BuilderDelight) : CommandExecutor {

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) return true
        if (sender.name != "pkp0") return true

        //TODO
        sender.sendMessage(Component.text("starting the distribution!"))

        return true
    }
}