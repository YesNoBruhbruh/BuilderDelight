package me.maanraj514.builderdelight.task.structure

import org.bukkit.Bukkit
import org.bukkit.Material
import java.util.UUID

class PlaceableBlock(
    private val worldId: UUID,
    private val blockX: Int,
    private val blockY: Int,
    private val blockZ: Int,
    private val material: Material
) : WorkLoad {


    override fun compute() {
        val world = Bukkit.getWorld(worldId) ?: return

        world.getBlockAt(blockX, blockY, blockZ).type = material
    }
}