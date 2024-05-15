package me.maanraj514.builderdelight.tasks.structure

import me.maanraj514.builderdelight.BuilderDelight
import org.bukkit.World

class AddBlockWorkLoad(
    val world: World,
    val blockX: Int,
    val blockY: Int,
    val blockZ: Int,
    val plugin: BuilderDelight
) : WorkLoad {

    override fun compute() {
        val block = world.getBlockAt(blockX, blockY, blockZ)

        plugin.addBlock(block)
    }
}