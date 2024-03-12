package me.maanraj514.builderdelight.worldedit.extent

import com.jeff_media.customblockdata.CustomBlockData
import com.sk89q.worldedit.WorldEdit
import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.entity.Player
import com.sk89q.worldedit.event.extent.EditSessionEvent
import com.sk89q.worldedit.extent.AbstractDelegateExtent
import com.sk89q.worldedit.extent.Extent
import com.sk89q.worldedit.math.BlockVector3
import com.sk89q.worldedit.world.block.BlockStateHolder
import me.maanraj514.builderdelight.BuilderDelight
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.persistence.PersistentDataType
import org.bukkit.scheduler.BukkitRunnable

class BlockPlaceExtent(
    private val event: EditSessionEvent,
    extent: Extent,
    private val weWorld: com.sk89q.worldedit.world.World,
    private val plugin: BuilderDelight
) :
    AbstractDelegateExtent(extent) {

    override fun <T : BlockStateHolder<T>?> setBlock(location: BlockVector3, block: T): Boolean {
        if (event.actor !is Player) {
            return super.setBlock(location, block)
        }

        val task = object : BukkitRunnable() {
            override fun run() {
                val wePlayer = event.actor as Player
                val world = BukkitAdapter.asBukkitWorld(weWorld).world
                val bukkitBlock: Block = world.getBlockAt(location.blockX, location.blockY, location.blockZ)

                //TODO: make it so that, people who aren't builders, when they set
                // the blocks to air, only all the blocks that are non-builder blocks will stay safe.

                if (plugin.builders.contains(wePlayer.uniqueId)) {
                    if (bukkitBlock.type == Material.AIR) {
                        //remove pdc.
                        reloadPdcOfBlock(bukkitBlock)
                    } else {
                        // if the block type is not air, then set the pdc of that block.
                        setPdcOfBlock(bukkitBlock)
                    }
                } else {
                    if (bukkitBlock.type == Material.AIR) {
                        println("block material is air!")
                        // we want to make sure builder blocks STAY.
                        if (doesBlockHavePdc(bukkitBlock)) {
                            println("block has pdc!")
                            val pdc = CustomBlockData(bukkitBlock, plugin)
                            val material = Material.getMaterial(pdc.get(plugin.BUILDER_BLOCK_KEY, PersistentDataType.STRING) ?: "") ?: Material.AIR
                            bukkitBlock.type = material // set it back to the way it was before.
                        }
                    } else {
                        println("block material is not air!")
                        reloadPdcOfBlock(bukkitBlock)
                    }
                }
            }
        }

        task.runTaskLater(plugin, 20L)

        return super.setBlock(location, block)
    }

    private fun reloadPdcOfBlock(bukkitBlock: Block) {
        val pdc = CustomBlockData(bukkitBlock, plugin)
        if (doesBlockHavePdc(bukkitBlock)) {
            pdc.remove(plugin.BUILDER_BLOCK_KEY)
            println("removed pdc")
        }
    }

    private fun setPdcOfBlock(bukkitBlock: Block) {
        if (doesBlockHavePdc(bukkitBlock)) return
        val pdc = CustomBlockData(bukkitBlock, plugin)
        pdc.set(
            plugin.BUILDER_BLOCK_KEY,
            PersistentDataType.STRING,
            bukkitBlock.type.name
        )
        println("set pdc")
    }

    private fun doesBlockHavePdc(bukkitBlock: Block): Boolean {
        val pdc = CustomBlockData(bukkitBlock, plugin)
        return pdc.has(plugin.BUILDER_BLOCK_KEY)
    }
}