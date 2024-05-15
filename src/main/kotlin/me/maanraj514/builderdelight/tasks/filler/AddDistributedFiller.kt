package me.maanraj514.builderdelight.tasks.filler

import me.maanraj514.builderdelight.BuilderDelight
import me.maanraj514.builderdelight.tasks.DistributedTickTask
import me.maanraj514.builderdelight.tasks.structure.AddBlockWorkLoad
import org.bukkit.World

class AddDistributedFiller(
    val workLoadRunnable: DistributedTickTask,
    val world: World,
    val plugin: BuilderDelight
) {

    fun fill() {
        val locations = plugin.getLocations()

        for (location in locations) {
            val workLoad = AddBlockWorkLoad(
                world,
                location.blockX,
                location.blockY,
                location.blockZ,
                plugin
            )

            workLoadRunnable.add(workLoad)
        }
    }
}