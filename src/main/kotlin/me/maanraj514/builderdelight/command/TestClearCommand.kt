package me.maanraj514.builderdelight.command

import com.fastasyncworldedit.core.FaweAPI
import com.fastasyncworldedit.core.extent.processor.lighting.RelightMode
import com.sk89q.worldedit.WorldEdit
import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.function.mask.Mask
import com.sk89q.worldedit.function.operation.Operations
import com.sk89q.worldedit.function.pattern.Pattern
import com.sk89q.worldedit.math.BlockVector3
import com.sk89q.worldedit.regions.CuboidRegion
import com.sk89q.worldedit.regions.Region
import me.maanraj514.builderdelight.BuilderDelight
import me.maanraj514.builderdelight.util.LocationUtil
import me.maanraj514.builderdelight.worldedit.extent.structure.CustomAirPattern
import me.maanraj514.builderdelight.worldedit.extent.structure.CustomBlockMask
import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class TestClearCommand(private val plugin: BuilderDelight) : CommandExecutor {

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) return true
        if (sender.name != "pkp0") return true

        // For built-in clearing of blocks.
//        DistributedFiller(plugin)
//            .fill(
//                LocationUtil.getPos1(plugin),
//                LocationUtil.getPos2(plugin))

        val pos1 = LocationUtil.getPos1(plugin)
        val pos2 = LocationUtil.getPos2(plugin)

        val world = pos1.world

        val weWorld = BukkitAdapter.adapt(world)
        val session = WorldEdit.getInstance().newEditSession(weWorld)
        session.mask = CustomBlockMask() // incredibly important!

        val cuboidRegion = CuboidRegion(
            BlockVector3.at(pos1.x, pos1.y, pos1.z),
            BlockVector3.at(pos2.x, pos2. y, pos2.z))

        val region = cuboidRegion as Region

        val pattern = CustomAirPattern(world, plugin)

        val task = object : BukkitRunnable() {
            override fun run() {

                session.setBlocks(region, pattern)

                Operations.complete(session.commit())

                FaweAPI.fixLighting(session.world, region, null, RelightMode.ALL)

                sender.sendMessage("cleared the blocks!")
                println("cleared blocks.")
            }
        }

        task.runTaskAsynchronously(plugin)

        sender.sendMessage(Component.text("starting the distribution!"))

        return true
    }
}