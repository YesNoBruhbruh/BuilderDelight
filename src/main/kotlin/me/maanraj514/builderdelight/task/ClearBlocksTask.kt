package me.maanraj514.builderdelight.task

import me.maanraj514.builderdelight.BuilderDelight
import me.maanraj514.builderdelight.task.filler.DistributedFiller
import org.bukkit.Material
import org.bukkit.scheduler.BukkitRunnable


class ClearBlocksTask(private val plugin: BuilderDelight) : BukkitRunnable() {

    // gets all the blocks that need to be cleared, and adds the workload to the main runnable.
    override fun run() {
        DistributedFiller(plugin.workLoadRunnable, plugin).fill(plugin.getPos1(), plugin.getPos2(), Material.AIR)

        println("clear blocks task.")
    }
}