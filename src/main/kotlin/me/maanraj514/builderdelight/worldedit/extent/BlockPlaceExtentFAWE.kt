package me.maanraj514.builderdelight.worldedit.extent

import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.entity.Player
import com.sk89q.worldedit.event.extent.EditSessionEvent
import com.sk89q.worldedit.extent.AbstractDelegateExtent
import com.sk89q.worldedit.extent.Extent
import com.sk89q.worldedit.function.pattern.Pattern
import com.sk89q.worldedit.regions.Region
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

    // this is for the commands.
    override fun setBlocks(region: Region, pattern: Pattern): Int {
        if (event.actor !is Player) {
            return super.setBlocks(region, pattern)
        }

        val success = super.setBlocks(region, pattern)

        val wePlayer = event.actor as Player

        if (!plugin.builders.contains(wePlayer.uniqueId)) return success // just return.

        val world = BukkitAdapter.asBukkitWorld(weWorld).world

        //TODO make this more performant but it will do for now.
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