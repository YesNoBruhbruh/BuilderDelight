package me.maanraj514.builderdelight.worldedit

import com.sk89q.worldedit.EditSession
import com.sk89q.worldedit.WorldEdit
import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.bukkit.BukkitPlayer
import com.sk89q.worldedit.entity.Player
import com.sk89q.worldedit.event.extent.EditSessionEvent
import com.sk89q.worldedit.util.eventbus.Subscribe
import me.maanraj514.builderdelight.BuilderDelight
import me.maanraj514.builderdelight.worldedit.extent.BlockPlaceExtent

class WorldEditListener(private val plugin: BuilderDelight) {

    init {
        WorldEdit.getInstance().eventBus.register(object : Any() {
            @Subscribe
            fun onEditSessionEvent(event: EditSessionEvent) {
                if (event.stage == EditSession.Stage.BEFORE_HISTORY) {
                    val world = event.world ?: return
                    event.extent = BlockPlaceExtent(event, event.extent, world, plugin)
                }
            }
        })
    }
}