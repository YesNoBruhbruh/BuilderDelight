package me.maanraj514.builderdelight.worldedit.extent

import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.entity.Player
import com.sk89q.worldedit.event.extent.EditSessionEvent
import com.sk89q.worldedit.extent.AbstractDelegateExtent
import com.sk89q.worldedit.extent.Extent
import com.sk89q.worldedit.function.pattern.Pattern
import com.sk89q.worldedit.regions.Region
import me.maanraj514.builderdelight.BuilderDelight

class BlockPlaceExtent(
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
        val world = BukkitAdapter.asBukkitWorld(weWorld).world

        //TODO fix this

//        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
//            region.forEach { blockVector3 ->
//                val bukkitBlock = world.getBlockAt(blockVector3.x, blockVector3.y, blockVector3.z)
//
//                handleSetBlockPlayer(wePlayer, bukkitBlock)
//            }
//        }, 1L)

        return success
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