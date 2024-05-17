package me.maanraj514.builderdelight.task

import com.fastasyncworldedit.core.FaweAPI
import com.fastasyncworldedit.core.extent.processor.lighting.RelightMode
import com.sk89q.worldedit.WorldEdit
import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.function.operation.Operations
import com.sk89q.worldedit.math.BlockVector3
import com.sk89q.worldedit.regions.CuboidRegion
import com.sk89q.worldedit.regions.Region
import com.sk89q.worldedit.world.block.BlockTypes
import me.maanraj514.builderdelight.BuilderDelight
import me.maanraj514.builderdelight.util.LocationUtil
import me.maanraj514.builderdelight.worldedit.extent.structure.CustomBlockMask
import org.bukkit.scheduler.BukkitRunnable

class ClearBlocksTask(private val plugin: BuilderDelight) : BukkitRunnable() {

    val tasks = mutableListOf<Int>()

    override fun run() {
        val pos1 = LocationUtil.getPos1(plugin)
        val pos2 = LocationUtil.getPos2(plugin)

        val world = pos1.world

        val weWorld = BukkitAdapter.adapt(world)
        val session = WorldEdit.getInstance().newEditSession(weWorld)

        val cuboidRegion = CuboidRegion(
            BlockVector3.at(pos1.x, pos1.y, pos1.z),
            BlockVector3.at(pos2.x, pos2. y, pos2.z))

        val region = cuboidRegion as Region

        session.mask = CustomBlockMask(plugin) // incredibly important!
        val task = object : BukkitRunnable() {
            override fun run() {
                session.setBlocks(region, BlockTypes.AIR)

                Operations.complete(session.commit())

                FaweAPI.fixLighting(session.world, region, null, RelightMode.ALL)
            }
        }

        task.runTaskAsynchronously(plugin)

        tasks.add(task.taskId)
    }
}