package me.maanraj514.builderdelight.worldedit.extent.structure

import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.function.mask.Mask
import com.sk89q.worldedit.math.BlockVector3
import me.maanraj514.builderdelight.BuilderDelight
import me.maanraj514.builderdelight.util.LocationUtil

class CustomBlockMask(private val plugin: BuilderDelight) : Mask {

    override fun test(vector: BlockVector3): Boolean {
        val location = BukkitAdapter.adapt(LocationUtil.getPos1(plugin).world, vector)

        // if true, then return the opposite which is false which doesn't set the block.
        return !plugin.builderBlocks.contains(location) // return opposite of the value gotten.
    }

    override fun copy(): Mask {
        return CustomBlockMask(plugin)
    }
}