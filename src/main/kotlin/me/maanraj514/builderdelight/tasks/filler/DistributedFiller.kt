package me.maanraj514.builderdelight.tasks.filler

import com.sk89q.worldedit.regions.Region
import me.maanraj514.builderdelight.BuilderDelight
import me.maanraj514.builderdelight.tasks.DistributedTickTask
import me.maanraj514.builderdelight.tasks.structure.CheckableBlock
import org.bukkit.World

class DistributedFiller(
    val workLoadRunnable: DistributedTickTask,
    val world: World,
    val plugin: BuilderDelight
) : VolumeFiller {

    override fun fill(region: Region) {
        region.forEach { blockVector3 ->
            workLoadRunnable.add(
                CheckableBlock(
                    world,
                    blockVector3.x,
                    blockVector3.y,
                    blockVector3.z,
                    plugin
                )
            )
        }
    }
}