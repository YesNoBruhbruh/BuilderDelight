package me.maanraj514.builderdelight.worldedit.extent.structure

import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.function.pattern.Pattern
import com.sk89q.worldedit.math.BlockVector3
import com.sk89q.worldedit.world.block.BaseBlock
import com.sk89q.worldedit.world.block.BlockTypes
import me.maanraj514.builderdelight.BuilderDelight
import org.bukkit.World

class CustomAirPattern(private val world: World, private val plugin: BuilderDelight) : Pattern {

    override fun applyBlock(position: BlockVector3): BaseBlock? {
        val bukkitBlock = world.getBlockAt(position.blockX, position.blockY, position.blockZ)
        val bukkitBlockLocation = bukkitBlock.location
        val bukkitBlockType = bukkitBlock.type

        if (!plugin.builderBlocks.contains(bukkitBlockLocation)) { // if it isn't a builderBlock.
            return BlockTypes.AIR?.defaultState?.toBaseBlock()
        }

        val blockType = BukkitAdapter.asBlockType(bukkitBlockType)

        return blockType?.defaultState?.toBaseBlock()
    }
}