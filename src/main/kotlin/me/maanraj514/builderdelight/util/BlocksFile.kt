package me.maanraj514.builderdelight.util

import me.maanraj514.builderdelight.BuilderDelight
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class BlocksFile(
    plugin: BuilderDelight
) {

    private val blocksFile = File(plugin.dataFolder.absolutePath + "/blocks.yml")
    private val configuration = YamlConfiguration()

    init {
        if (!blocksFile.exists()) {
            blocksFile.createNewFile()
        }

        configuration.load(blocksFile)
    }

    fun save() {
        configuration.save(blocksFile)
    }

    fun reload() {
        configuration.load(blocksFile)
    }

    fun saveBlocks(blocks: List<Location>) {
        val path = "builder-blocks"
        val serializedBlocks = mutableListOf<String>()

        for (block in blocks) {
            val blockToString = serialize(block)

            serializedBlocks.add(blockToString)
        }

        configuration.set(path, null) // to reset the blocks.
        configuration.set(path, serializedBlocks) // add the existing + new ones.
    }

    fun loadBlocks(): List<Location> {
        val blocks = mutableListOf<Location>()
        val blocksList = configuration.getStringList("builder-blocks")

        for (block in blocksList) {
            val stringToBlock = deserialize(block)

            blocks.add(stringToBlock)
        }

        return blocks
    }

    private fun serialize(block: Location): String {
        val world = block.world.name
        val x = block.x
        val y = block.y
        val z = block.z

        return "$world, $x, $y, $z"
    }

    private fun deserialize(string: String): Location {
        val args = string.split(", ") // will make world x y z into diff args.
        val world = Bukkit.getWorld(args[0])
        val x = args[1].toDouble()
        val y = args[2].toDouble()
        val z = args[3].toDouble()

        return Location(world, x, y, z)
    }
}