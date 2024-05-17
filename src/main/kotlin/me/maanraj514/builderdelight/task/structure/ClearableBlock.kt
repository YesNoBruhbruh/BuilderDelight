package me.maanraj514.builderdelight.task.structure

import me.maanraj514.builderdelight.BuilderDelight
import org.bukkit.Bukkit
import org.bukkit.Material
import java.util.UUID

class ClearableBlock(
    val worldId: UUID,
    val blockX: Int,
    val blockY: Int,
    val blockZ: Int,
    val plugin: BuilderDelight
) : WorkLoad {

    override fun compute() {
        val world = Bukkit.getWorld(worldId) ?: return
        val block = world.getBlockAt(blockX, blockY, blockZ)

        if (!plugin.isBuilderBlock(block)) { // if it isn't a builderBlock
            //TODO do logic
//            block.type = Material.AIR
        }
    }
}