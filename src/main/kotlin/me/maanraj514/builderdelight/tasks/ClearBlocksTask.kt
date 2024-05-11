package me.maanraj514.builderdelight.tasks

import com.sk89q.worldedit.WorldEdit
import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.math.BlockVector3
import com.sk89q.worldedit.regions.CuboidRegion
import com.sk89q.worldedit.regions.Region
import me.maanraj514.builderdelight.BuilderDelight
import me.maanraj514.builderdelight.tasks.filler.DistributedFiller
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable

class ClearBlocksTask(private val plugin: BuilderDelight) : BukkitRunnable() {

    private val runningTasks = mutableListOf<Int>()

    override fun run() {
        println("attempting to clear blocks...")

        val pos1 = plugin.getPos1()
        val pos2 = plugin.getPos2()

        val weWorld = BukkitAdapter.adapt(pos1.world)
        val session = WorldEdit.getInstance().newEditSession(weWorld)

        val cuboidRegion = CuboidRegion(
            BlockVector3.at(pos1.x, pos1.y, pos1.z),
            BlockVector3.at(pos2.x, pos2. y, pos2.z))

        val region = cuboidRegion as Region

        val task = object : BukkitRunnable() {
            var counter = 0
            override fun run() {
                if (counter >= 1){
                    cancel()
                }

                DistributedFiller(plugin.distributedTickTask, pos1.world, plugin)
                    .fill(region)

//                session.setBlocks(region, BlockTypes.AIR)

//                Operations.complete(session.commit())

//                FaweAPI.fixLighting(session.world, region, null, RelightMode.ALL)

                println("cleared blocks.")
                counter++
            }
        }

        task.runTaskTimer(plugin, 0L, 20L)

        runningTasks.add(task.taskId)

        println("finished clear method")
    }

    override fun cancel() {
        for (taskId in runningTasks) {
            Bukkit.getScheduler().cancelTask(taskId)
        }

        runningTasks.clear()

        super.cancel()
    }
}