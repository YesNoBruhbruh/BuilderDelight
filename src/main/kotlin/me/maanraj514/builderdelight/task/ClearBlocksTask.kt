package me.maanraj514.builderdelight.task

import com.jeff_media.customblockdata.CustomBlockData
import me.maanraj514.builderdelight.BuilderDelight
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.scheduler.BukkitRunnable

class ClearBlocksTask(private val plugin: BuilderDelight) : BukkitRunnable() {

    override fun run() {
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

        val locations = locationsFromTwoPoints(pos1, pos2)

        for (location in locations) {
            // set the blocks that are not in builderMode to air.
            val block = location.block
            val pdc = CustomBlockData(block, plugin)

            if (pdc.has(plugin.BUILDER_BLOCK_KEY)) continue

            // this means its a non-builder block.
            block.type = Material.AIR
        }

        if (plugin.config.getBoolean("clear-message")) {
            println("clearing blocks...")
        }
    }

    private fun locationsFromTwoPoints(location1: Location, location2: Location) : List<Location> {
        val locations = mutableListOf<Location>()

        val topBlockX: Int = location1.blockX.coerceAtLeast(location2.blockX)
        val bottomBlockX: Int = location1.blockX.coerceAtMost(location2.blockX)

        val topBlockY: Int = location1.blockY.coerceAtLeast(location2.blockY)
        val bottomBlockY: Int = location1.blockY.coerceAtMost(location2.blockY)

        val topBlockZ: Int = location1.blockZ.coerceAtLeast(location2.blockZ)
        val bottomBlockZ: Int = location1.blockZ.coerceAtMost(location2.blockZ)

        for (x in bottomBlockX..topBlockX) {
            for (y in bottomBlockY..topBlockY) {
                for (z in bottomBlockZ..topBlockZ) {
                    locations.add(location1.world.getBlockAt(x, y, z).location)
                }
            }
        }

        return locations
    }
}