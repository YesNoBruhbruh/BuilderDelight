package me.maanraj514.builderdelight.ocelot.structure

import org.bukkit.Chunk
import org.bukkit.World
import java.util.*
import kotlin.math.abs
import kotlin.math.max

class ChunkPosition(
    val world: World,
    val x: Int,
    val z: Int
) {

    constructor(chunk: Chunk) : this(chunk.world, chunk.x, chunk.z)

    fun getChunk() : Chunk {
        return world.getChunkAt(x, z)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ChunkPosition) return false
        return x == other.x && world.name == other.world.name
    }

    fun distance(other: ChunkPosition): Int {
        return max(abs((other.x - x).toDouble()), abs((other.z - z).toDouble())).toInt()
    }

    override fun hashCode(): Int {
        return Objects.hash(world.name, x, z)
    }

    override fun toString(): String {
        return "ChunkPosition{" +
                "world=" + world +
                ", x=" + x +
                ", z=" + z +
                '}'
    }
}