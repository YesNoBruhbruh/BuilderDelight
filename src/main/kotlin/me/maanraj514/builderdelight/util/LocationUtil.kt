package me.maanraj514.builderdelight.util

import me.maanraj514.builderdelight.BuilderDelight
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material

object LocationUtil {

    fun getPos1(plugin: BuilderDelight): Location {
        return Location(
            Bukkit.getWorld(plugin.config.get("pos1.world").toString()),
            plugin.config.get("pos1.x").toString().toDouble(),
            plugin.config.get("pos1.y").toString().toDouble(),
            plugin.config.get("pos1.z").toString().toDouble()
        )
    }

    fun getPos2(plugin: BuilderDelight): Location {
        return Location(
            Bukkit.getWorld(plugin.config.get("pos2.world").toString()),
            plugin.config.get("pos2.x").toString().toDouble(),
            plugin.config.get("pos2.y").toString().toDouble(),
            plugin.config.get("pos2.z").toString().toDouble()
        )
    }

    fun getLocations(pos1: Location, pos2: Location): List<Location> {
        val locations = mutableListOf<Location>()

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
                    locations.add(Location(world, x.toDouble(), y.toDouble(), z.toDouble()))
                }
            }
        }

        return locations
    }
}