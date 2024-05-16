package me.maanraj514.builderdelight.ocelot.structure

import org.bukkit.block.Block
import java.util.Objects

class BlockPosition(
    val x: Int,
    val y: Int,
    val z: Int
) {

    constructor(block: Block) : this(block.x, block.y, block.z)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as BlockPosition
        return x == that.x && y == that.y && z == that.z
    }

    override fun hashCode(): Int {
        return Objects.hash(x,y,z)
    }

    override fun toString(): String {
        return "BlockPosition{x=$x, y=$y, z=$z}"
    }
}