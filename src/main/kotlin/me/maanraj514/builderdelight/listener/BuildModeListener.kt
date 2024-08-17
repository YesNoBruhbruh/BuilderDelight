package me.maanraj514.builderdelight.listener

import me.maanraj514.builderdelight.BuilderDelight
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockExplodeEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.event.player.PlayerQuitEvent

class BuildModeListener(private val plugin: BuilderDelight) : Listener {

    @EventHandler
    fun onPlace(event: BlockPlaceEvent) {
        val player = event.player
        val uuid = player.uniqueId

        //TODO
//        if (plugin.builders.contains(uuid)) {
//            val block = event.block
//            plugin.addBlock(block)
//        }
    }

    @EventHandler
    fun onBreak(event: BlockBreakEvent) {
        val player = event.player
        val uuid = player.uniqueId

        val block = event.block

        //TODO
//        if (!plugin.isBuilderBlock(block)) return // just a normal block
        // now it's a builder block.

        // if they are a normal person, they can't break a builderBlock.
        if (!plugin.builders.contains(uuid)) {
            event.isCancelled = true
            return
        }

        // now they are a builder, we remove the block from the list.
        plugin.removeBlock(block)
    }

//    @EventHandler
//    fun onClick(event: PlayerInteractEvent) {
//        val action = event.action
//        if (!action.isRightClick) return
//
//        val block = event.clickedBlock ?: return
//
//        val player = event.player
//        val uuid = player.uniqueId
//        if (!plugin.builders.contains(uuid)) return
//
//        val pdc = CustomBlockData(block, plugin)
//        if (pdc.has(plugin.BUILDER_BLOCK_KEY)) {
//            player.sendMessage("This is a builder block!")
//        }
//    }

    @EventHandler
    fun onEntityExplode(event: EntityExplodeEvent) {
        for (block in event.blockList()) {
            //TODO
//            if (plugin.isBuilderBlock(block)) {
//                event.isCancelled = true
//                return
//            }
        }
    }

    @EventHandler
    fun onBlockExplode(event: BlockExplodeEvent) {
        for (block in event.blockList()) {
            //TODO
//            if (plugin.isBuilderBlock(block)) {
//                event.isCancelled = true
//                return
//            }
        }
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val uuid = event.player.uniqueId
        plugin.builders.remove(uuid)
    }
}