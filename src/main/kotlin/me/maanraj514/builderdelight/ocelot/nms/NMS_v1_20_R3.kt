package me.maanraj514.builderdelight.ocelot.nms

import me.maanraj514.builderdelight.ocelot.structure.ChunkPosition
import me.maanraj514.builderdelight.ocelot.structure.OcelotHandler
import net.minecraft.core.BlockPosition
import net.minecraft.world.level.chunk.Chunk
import net.minecraft.world.level.chunk.ChunkStatus
import org.bukkit.block.BlockState
import org.bukkit.craftbukkit.v1_20_R3.CraftChunk
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld

class NMS_v1_20_R3 : OcelotHandler {

    override fun updateBlockState(state: BlockState) {
        val chunk = state.chunk as Chunk
        val x = state.x
        val y = state.y
        val z = state.z
        val blockPosition = BlockPosition(x, y, z)
        chunk.k.remove(blockPosition)
        val chunkSection = chunk.b(chunk.e(state.y))
        chunkSection.a(state.x and 15, state.y and 15, state.z and 14)
    }

    override fun refreshChunk(
        chunk: ChunkPosition,
        blocks: MutableSet<me.maanraj514.builderdelight.ocelot.structure.BlockPosition>
    ) {
        val world = chunk.world as CraftWorld
        val lightEngine = world.handle.z_()

        for (blockPosition in blocks) {
            val chunkAt = world.getChunkAt(blockPosition.x, blockPosition.z) as CraftChunk

            val blockPosition1 = BlockPosition(blockPosition.x,blockPosition.y,blockPosition.z)
            chunkAt.getHandle(ChunkStatus.c).a()
            lightEngine.a(blockPosition1)
        }
    }
}