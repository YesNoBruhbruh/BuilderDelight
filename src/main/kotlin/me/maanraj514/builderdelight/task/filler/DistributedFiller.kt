package me.maanraj514.builderdelight.task.filler

import com.jeff_media.customblockdata.CustomBlockData
import me.maanraj514.builderdelight.BuilderDelight
import me.maanraj514.builderdelight.task.ScheduledWorkLoadRunnable
import me.maanraj514.builderdelight.task.structure.PlaceableBlock
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material

class DistributedFiller(
    private val workloadRunnable: ScheduledWorkLoadRunnable,
    private val plugin: BuilderDelight
) : VolumeFiller {

    override fun fill(cornerA: Location, cornerB: Location, material: Material) {

        val pos1 = Location(
            Bukkit.getWorld(plugin.config.get("pos1.world").toString()),
            plugin.config.get("pos1.x").toString().toDouble(),
            plugin.config.get("pos1.y").toString().toDouble(),
            plugin.config.get("pos1.z").toString().toDouble()
        )
        val pos2 = Location(
            Bukkit.getWorld(plugin.config.get("pos2.world").toString()),
            plugin.config.get("pos2.x").toString().toDouble(),
            plugin.config.get("pos2.y").toString().toDouble(),
            plugin.config.get("pos2.z").toString().toDouble()
        )

        val world = pos1.world

        val topBlockX: Int = pos1.blockX.coerceAtLeast(pos2.blockX)
        val bottomBlockX: Int = pos1.blockX.coerceAtMost(pos2.blockX)

        val topBlockY: Int = pos1.blockY.coerceAtLeast(pos2.blockY)
        val bottomBlockY: Int = pos1.blockY.coerceAtMost(pos2.blockY)

        val topBlockZ: Int = pos1.blockZ.coerceAtLeast(pos2.blockZ)
        val bottomBlockZ: Int = pos1.blockZ.coerceAtMost(pos2.blockZ)

        for (x in bottomBlockX..topBlockX) {
            for (y in bottomBlockY..topBlockY) {
                for (z in bottomBlockZ..topBlockZ) {
                    val block = world.getBlockAt(x, y, z)
                    val pdc = CustomBlockData(block, plugin)
                    if (pdc.has(plugin.BUILDER_BLOCK_KEY)) continue

                    val placeableBlock = PlaceableBlock(world.uid, x, y, z, material)
                    workloadRunnable.addWorkLoad(placeableBlock)
                }
            }
        }
    }
}