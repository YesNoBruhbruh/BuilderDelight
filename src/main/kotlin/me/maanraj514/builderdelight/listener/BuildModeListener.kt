package me.maanraj514.builderdelight.listener

import com.jeff_media.customblockdata.CustomBlockData
import com.jeff_media.customblockdata.events.CustomBlockDataRemoveEvent
import me.maanraj514.builderdelight.BuilderDelight
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockExplodeEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.persistence.PersistentDataType

class BuildModeListener(private val plugin: BuilderDelight) : Listener {

    @EventHandler
    fun onPlace(event: BlockPlaceEvent) {
        val player = event.player
        val uuid = player.uniqueId

        if (plugin.builders.contains(uuid)) {
            val block = event.block

            val pdc = CustomBlockData(block, plugin)

            if (pdc.has(plugin.BUILDER_BLOCK_KEY)) return // Don't want to re-set the pdc if it's already set.

            pdc.set(
                plugin.BUILDER_BLOCK_KEY,
                PersistentDataType.STRING,
                "This is a builderMode placed block. Normal humans are unable to break this."
            )
        }
    }

    @EventHandler
    fun onBreak(event: BlockBreakEvent) {
        val player = event.player
        val uuid = player.uniqueId

        val block = event.block
        val pdc = CustomBlockData(block, plugin)

        if (!pdc.has(plugin.BUILDER_BLOCK_KEY)) return

        if (plugin.builders.contains(uuid)){
            // remove the pdc of the block if broken by a builder.
            pdc.remove(plugin.BUILDER_BLOCK_KEY)
        } else {
            // don't allow normal humans to break builderMode blocks.
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onClick(event: PlayerInteractEvent) {
        val action = event.action
        if (!action.isRightClick) return

        val block = event.clickedBlock ?: return

        val player = event.player
        val uuid = player.uniqueId
        if (!plugin.builders.contains(uuid)) return

        val pdc = CustomBlockData(block, plugin)
        if (pdc.has(plugin.BUILDER_BLOCK_KEY)) {
            player.sendMessage("This is a builder block!")
        }
    }

    @EventHandler
    fun onEntityExplode(event: EntityExplodeEvent) {
        for (block in event.blockList()) {
            val pdc = CustomBlockData(block, plugin)
            if (pdc.has(plugin.BUILDER_BLOCK_KEY)) {
                event.isCancelled = true
                return
            }
        }
    }

    @EventHandler
    fun onBlockExplode(event: BlockExplodeEvent) {
        for (block in event.blockList()) {
            val pdc = CustomBlockData(block, plugin)
            if (pdc.has(plugin.BUILDER_BLOCK_KEY)) {
                event.isCancelled = true
                return
            }
        }
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val uuid = event.player.uniqueId
        plugin.builders.remove(uuid)
    }

    @EventHandler
    fun onPDCRemoval(event: CustomBlockDataRemoveEvent) {
        val block = event.block
        val location = block.location

        // doesn't really error when using .remove, so it's safe to use.
        plugin.builderBlocks.remove(location)
    }
}