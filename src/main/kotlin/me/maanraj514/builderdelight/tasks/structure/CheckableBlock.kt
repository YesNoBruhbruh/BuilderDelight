package me.maanraj514.builderdelight.tasks.structure

import com.jeff_media.customblockdata.CustomBlockData
import me.maanraj514.builderdelight.BuilderDelight
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.World

class CheckableBlock(
    val world: World,
    val blockX: Int,
    val blockY: Int,
    val blockZ: Int,
    val plugin: BuilderDelight
) : WorkLoad {

    override fun compute() {
        val block = world.getBlockAt(blockX, blockY, blockZ)

        val hasCustomBlockData = CustomBlockData.hasCustomBlockData(block, plugin)

        if (hasCustomBlockData) {
            val pdc = CustomBlockData(block, plugin)
            if (!pdc.has(plugin.BUILDER_BLOCK_KEY)) {
                block.type = Material.AIR
            }
        } else {
            block.type = Material.AIR
        }
    }
}