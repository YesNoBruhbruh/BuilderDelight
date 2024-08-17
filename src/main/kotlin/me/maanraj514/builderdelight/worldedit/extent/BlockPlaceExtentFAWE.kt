package me.maanraj514.builderdelight.worldedit.extent

import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.entity.Player
import com.sk89q.worldedit.event.extent.EditSessionEvent
import com.sk89q.worldedit.extent.AbstractDelegateExtent
import com.sk89q.worldedit.extent.Extent
import com.sk89q.worldedit.function.pattern.Pattern
import com.sk89q.worldedit.regions.Region
import com.sk89q.worldedit.world.block.BlockStateHolder
import me.maanraj514.builderdelight.BuilderDelight
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Block

class BlockPlaceExtentFAWE(
    private val event: EditSessionEvent,
    extent: Extent,
    private val weWorld: com.sk89q.worldedit.world.World,
    private val plugin: BuilderDelight
) : AbstractDelegateExtent(extent) {

    //paste
    override fun <T : BlockStateHolder<T>?> setBlock(x: Int, y: Int, z: Int, block: T): Boolean {
//        println("setBlock1: $x, $y, $z, $block")

        if (event.actor !is Player) {
            return super.setBlock(x, y, z, block)
        }

        val success = super.setBlock(x, y, z, block)

        val wePlayer = event.actor as Player

        if (!plugin.builders.contains(wePlayer.uniqueId)) return success // just return.

        val world = BukkitAdapter.asBukkitWorld(weWorld).world

        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            val bukkitBlock = world.getBlockAt(x, y, z)

            handleBuilderSetBlock(bukkitBlock)
        }, 1L)

        return success
    }

    //cut
//    override fun <T : BlockStateHolder<T>?> setBlock(position: BlockVector3?, block: T): Boolean {
//        println("setBlock2: $position, $block")
//        return super.setBlock(position, block)
//    }

    // this is for the commands.
    override fun setBlocks(region: Region, pattern: Pattern): Int {
//        println("setBlocks: $region, $pattern")
        if (event.actor !is Player) {
            return super.setBlocks(region, pattern)
        }

        val success = super.setBlocks(region, pattern)

        val wePlayer = event.actor as Player

        if (!plugin.builders.contains(wePlayer.uniqueId)) return success // just return.

        val world = BukkitAdapter.asBukkitWorld(weWorld).world

        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            for (blockVector3 in region) {
                val bukkitBlock = world.getBlockAt(blockVector3.x, blockVector3.y, blockVector3.z)

                handleBuilderSetBlock(bukkitBlock)
            }
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

//    private fun handleSetBlockPlayer(wePlayer: Player, bukkitBlock: Block) {
//        if (!plugin.builders.contains(wePlayer.uniqueId)) {
////            println("1 player is not a builder!")
//            // This is for non-builders.
//            removePdcOfBlock(bukkitBlock)
//
//            return // we don't want to do anything else.
//        }
////        println("2 player is a builder!")
//
//        // This is for builders.
//        if (bukkitBlock.type == Material.AIR) {
////            println("2 bukkitBlockType = AIR!")
//            //remove pdc.
//            removePdcOfBlock(bukkitBlock)
////            println("2 bukkitBlock's PDC was removed!")
//        } else {
//            // if the block type is not air, then set the pdc of that block.
////            println("2 bukkitBlock is not air!, it is actually: ${bukkitBlock.type}")
//            setPdcOfBlock(bukkitBlock)
////            println("2 Successfully set pdc to the bukkit block!")
//        }
//    }
}