package me.maanraj514.builderdelight.ocelot

import me.maanraj514.builderdelight.BuilderDelight
import me.maanraj514.builderdelight.ocelot.nms.NMS_v1_20_R3
import me.maanraj514.builderdelight.ocelot.structure.BlockPosition
import me.maanraj514.builderdelight.ocelot.structure.ChunkPosition
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.block.BlockState
import java.util.concurrent.CompletableFuture
import java.util.regex.Matcher
import java.util.regex.Pattern

class OcelotAPI(private val plugin: BuilderDelight) {

    private val BLOCKS = mutableMapOf<ChunkPosition, MutableSet<BlockPosition>>()

    private val nms: NMS_v1_20_R3 = NMS_v1_20_R3()

    private val MID_VERSION = getMidVersion()

    private fun getMidVersion(): Int {
        val pattern: Pattern = Pattern.compile("1\\.([0-9]+)")
        val matcher: Matcher = pattern.matcher(Bukkit.getBukkitVersion())
        matcher.find()
        return matcher.group(1).toInt()
    }

    private fun getMinHeight(world: World): Int {
        if (MID_VERSION >= 17) {
            return world.minHeight
        }

        return 0
    }

    private fun isInBounds(block: Block): Boolean {
        val y: Int = block.location.blockY

        return y >= getMinHeight(block.world) && y <= block.world.maxHeight
    }

    fun updateBlock(block: Block, type: Material): CompletableFuture<Void> {
        require(isInBounds(block)) { "Block has to be in bounds! (Y: " + block.location.blockY + ")" }
        val state: BlockState = block.state
        state.type = type

        return updateBlockState(state)
    }

    private fun updateBlockState(state: BlockState): CompletableFuture<Void> {
        require(isInBounds(state.block)) { "Block has to be in bounds! (Y: " + state.y + ")" }
        val future = CompletableFuture<Void>()
        val stateLocation = state.location
        val stateChunk = stateLocation.chunk

        val run = Runnable {
            nms.updateBlockState(state)
            println("test 1")
            val c = ChunkPosition(stateChunk)
            if (!BLOCKS.containsKey(c)) {
                BLOCKS[c] = HashSet()
                println("test -1")
            }
            println("test 2")
            BLOCKS[c]?.add(BlockPosition(state.block))
            future.complete(null)
            println("test 3")
        }
        if (Bukkit.isPrimaryThread()) {
            run.run()
            println("test 4")
        } else {
            Bukkit.getScheduler().runTask(plugin, run)
            println("test 5")
        }

        println("test 6")
        return future
    }
}