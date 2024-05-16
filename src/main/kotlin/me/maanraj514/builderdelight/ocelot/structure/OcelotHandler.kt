package me.maanraj514.builderdelight.ocelot.structure

import org.bukkit.block.BlockState

interface OcelotHandler {
    fun updateBlockState(state: BlockState)
    fun refreshChunk(chunk: ChunkPosition, blocks: MutableSet<BlockPosition>)
}