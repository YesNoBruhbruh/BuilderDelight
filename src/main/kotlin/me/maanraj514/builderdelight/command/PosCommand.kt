package me.maanraj514.builderdelight.command

import me.maanraj514.builderdelight.BuilderDelight
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.UUID
import kotlin.math.max
import kotlin.math.min

class PosCommand(private val plugin: BuilderDelight) : CommandExecutor {

    private val pos1Map = mutableMapOf<UUID, Location>()
    private val pos2Map = mutableMapOf<UUID, Location>()

    private var taskId = -1

    override fun onCommand(sender: CommandSender, cmd: Command, p2: String, p3: Array<out String>?): Boolean {
        if (sender !is Player) return true
        if (!sender.hasPermission("builderdelight.command.pos")) return true

        val senderUUID = sender.uniqueId
        val senderLocation = sender.location

        when (cmd.name) {
            "pos1" -> {
                pos1Map[senderUUID] = senderLocation
                sender.sendMessage("Successfully set pos1!")
            }
            "pos2" -> {
                pos2Map[senderUUID] = senderLocation
                sender.sendMessage("Successfully set pos2!")
            }
            "savePos" -> {
                val pos1 = pos1Map[senderUUID]
                if (pos1 == null) {
                    sender.sendMessage("You have not set pos1!")
                    return true
                }
                val pos2 = pos2Map[senderUUID]
                if (pos2 == null){
                    sender.sendMessage("You have no set pos2!")
                    return true
                }

                savePosToConfig(pos1, pos2)

                sender.sendMessage("Successfully saved pos1 and pos2 to the config file!")
                sender.sendMessage("builderBlocks list size is ${plugin.builderBlocks.size}")
            }
        }

        return true
    }

    private fun savePosToConfig(pos1: Location, pos2: Location) {
        val minWorld = pos1.world.name
        val minX = min(pos1.x, pos2.x)
        val minY = min(pos1.y, pos2.y)
        val minZ = min(pos1.z, pos2.z)

        val maxWorld = pos2.world.name
        val maxX = max(pos1.x, pos2.x)
        val maxY = max(pos1.y, pos2.y)
        val maxZ = max(pos1.z, pos2.z)

        plugin.config.set("pos1.world", minWorld)
        plugin.config.set("pos1.x", minX)
        plugin.config.set("pos1.y", minY)
        plugin.config.set("pos1.z", minZ)

        plugin.config.set("pos2.world", maxWorld)
        plugin.config.set("pos2.x", maxX)
        plugin.config.set("pos2.y", maxY)
        plugin.config.set("pos2.z", maxZ)

        plugin.saveConfig()
        plugin.reloadConfig()
    }
}