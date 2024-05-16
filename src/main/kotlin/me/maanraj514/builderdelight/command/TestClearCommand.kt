package me.maanraj514.builderdelight.command

import me.maanraj514.builderdelight.BuilderDelight
import me.maanraj514.builderdelight.task.filler.DistributedFiller
import me.maanraj514.builderdelight.util.LocationUtil
import net.kyori.adventure.text.Component
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TestClearCommand(private val plugin: BuilderDelight) : CommandExecutor {

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) return true
        if (sender.name != "pkp0") return true

        DistributedFiller(plugin)
            .fill(
                LocationUtil.getPos1(plugin),
                LocationUtil.getPos2(plugin))

        sender.sendMessage(Component.text("starting the distribution!"))

        return true
    }
}