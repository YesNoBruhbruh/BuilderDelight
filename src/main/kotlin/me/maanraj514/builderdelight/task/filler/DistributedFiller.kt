package me.maanraj514.builderdelight.task.filler

import me.maanraj514.builderdelight.BuilderDelight
import me.maanraj514.builderdelight.task.structure.PlaceableBlock
import me.maanraj514.builderdelight.util.LocationUtil
import org.bukkit.Location

class DistributedFiller(
    val plugin: BuilderDelight
) : VolumeFiller {

    override fun fill(cornerA: Location, cornerB: Location) {
        val locations = LocationUtil.getLocations(cornerA, cornerB)
        val worldId = cornerA.world.uid

        for (location in locations) {
            val placeableBlock = PlaceableBlock(
                worldId,
                location.blockX,
                location.blockY,
                location.blockZ,
                plugin)

            plugin.workLoadRunnable.addWorkLoad(placeableBlock)
        }
    }
}