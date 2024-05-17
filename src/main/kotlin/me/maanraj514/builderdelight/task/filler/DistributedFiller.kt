package me.maanraj514.builderdelight.task.filler

import me.maanraj514.builderdelight.BuilderDelight
import me.maanraj514.builderdelight.task.structure.ClearableBlock
import org.bukkit.Location

class DistributedFiller(
    private val plugin: BuilderDelight
) : VolumeFiller {

    override fun fill(cornerA: Location, cornerB: Location) {
        val worldId = cornerA.world.uid

        val runnable = plugin.clearBlocksRunnable

        val topBlockX: Int = cornerA.blockX.coerceAtLeast(cornerB.blockX)
        val bottomBlockX: Int = cornerA.blockX.coerceAtMost(cornerB.blockX)

        val topBlockY: Int = cornerA.blockY.coerceAtLeast(cornerB.blockY)
        val bottomBlockY: Int = cornerA.blockY.coerceAtMost(cornerB.blockY)

        val topBlockZ: Int = cornerA.blockZ.coerceAtLeast(cornerB.blockZ)
        val bottomBlockZ: Int = cornerA.blockZ.coerceAtMost(cornerB.blockZ)

        for (x in bottomBlockX..topBlockX) {
            for (y in bottomBlockY..topBlockY) {
                for (z in bottomBlockZ..topBlockZ) {
                    runnable.addWorkLoad(ClearableBlock(
                        worldId,
                        x,
                        y,
                        z,
                        plugin
                    ))
                }
            }
        }
    }
}