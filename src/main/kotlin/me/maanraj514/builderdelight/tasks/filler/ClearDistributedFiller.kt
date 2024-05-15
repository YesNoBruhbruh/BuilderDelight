package me.maanraj514.builderdelight.tasks.filler

import com.sk89q.worldedit.regions.Region
import me.maanraj514.builderdelight.BuilderDelight
import me.maanraj514.builderdelight.tasks.DistributedTickTask
import me.maanraj514.builderdelight.tasks.structure.CheckableBlockWorkLoad
import org.bukkit.World

class ClearDistributedFiller(
    val workLoadRunnable: DistributedTickTask,
    val world: World,
    val plugin: BuilderDelight
) : VolumeFiller {

    override fun fill(region: Region) {
        for (vector in region) {
            workLoadRunnable.add(
                CheckableBlockWorkLoad(
                    world,
                    vector.x,
                    vector.y,
                    vector.z,
                    plugin
                )
            )
        }
    }
}