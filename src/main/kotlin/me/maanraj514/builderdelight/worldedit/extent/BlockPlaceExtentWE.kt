package me.maanraj514.builderdelight.worldedit.extent

import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.entity.Player
import com.sk89q.worldedit.event.extent.EditSessionEvent
import com.sk89q.worldedit.extent.AbstractDelegateExtent
import com.sk89q.worldedit.extent.Extent
import com.sk89q.worldedit.math.BlockVector3
import com.sk89q.worldedit.world.block.BlockStateHolder
import me.maanraj514.builderdelight.BuilderDelight
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Block

class BlockPlaceExtentWE(
    private val event: EditSessionEvent,
    extent: Extent,
    private val weWorld: com.sk89q.worldedit.world.World,
    private val plugin: BuilderDelight
) : AbstractDelegateExtent(extent) {


    override fun <T : BlockStateHolder<T>?> setBlock(location: BlockVector3?, block: T): Boolean {
        if (event.actor !is Player) {
            return super.setBlock(location, block)
        }

        val success = super.setBlock(location, block)

        val wePlayer = event.actor as Player

        if (!plugin.builders.contains(wePlayer.uniqueId)) return success // just return.

        val world = BukkitAdapter.asBukkitWorld(weWorld).world

        val x: Int? = location?.x
        val y: Int? = location?.y
        val z: Int? = location?.z

        if (x == null || y == null || z == null) return success

        //TODO make this more performant but it will do for now.
        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            val bukkitBlock = world.getBlockAt(x, y, z)

            handleBuilderSetBlock(bukkitBlock)
        }, 1L)

        return success
    }

    private fun handleBuilderSetBlock(bukkitBlock: Block) {
        if (bukkitBlock.type == Material.AIR) {
            plugin.removeBlock(bukkitBlock)
        } else {
            plugin.addBlock(bukkitBlock)
        }
    }
}