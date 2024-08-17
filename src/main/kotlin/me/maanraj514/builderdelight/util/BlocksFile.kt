package me.maanraj514.builderdelight.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import me.maanraj514.builderdelight.BuilderDelight
import org.bukkit.Bukkit
import org.bukkit.Location
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import java.io.File
import java.io.FileReader
import java.io.FileWriter


class BlocksFile(
    plugin: BuilderDelight
) {

    private val blocksFile = File(plugin.dataFolder.absolutePath, "/builder-blocks.json")

    init {
        if (!blocksFile.exists()) {
            blocksFile.createNewFile()
        }
    }

    fun saveBlocks(blocks: List<Location>) {
        val serializedBlocks = mutableListOf<String>()

        for (block in blocks) {
            val blockToString = serialize(block)

            serializedBlocks.add(blockToString)
        }

        val gson = GsonBuilder().setPrettyPrinting().create()

        val jsonArray = JSONArray()
        serializedBlocks.forEach {
            jsonArray.add(it)
        }

        val jsonObject = JSONObject()
        jsonObject["blocks"] = jsonArray

        val jsonString = gson.toJson(jsonObject)

        val fileWriter = FileWriter(blocksFile, false)
        fileWriter.write(jsonString)
        fileWriter.flush()
        fileWriter.close()
    }

    fun loadBlocks(): List<Location> {
        val blocks = mutableListOf<Location>()

        if (blocksFile.length() <= 3L) return blocks

        val parser = JsonParser()
        val parsed = parser.parse(FileReader(blocksFile))
        val jsonObject = parsed.asJsonObject

        val blocksJson = jsonObject.get("blocks").asJsonArray
        for (block in blocksJson) {
            val location = deserialize(block.asString)

            blocks.add(location)
        }

        return blocks
    }

    fun jsonToLoc(obj: JSONObject): Location {
        return Location(
            Bukkit.getWorld(obj["world"].toString()),
            obj["x"].toString().toDouble(),
            obj["y"].toString().toDouble(),
            obj["z"].toString().toDouble(),
            obj["yaw"].toString().toFloat(),
            obj["pitch"].toString().toFloat()
        )
    }

    // Location to Json
    fun locToJson(loc: Location): JSONObject {
        val jso = JSONObject()
        jso["world"] = loc.world.name
        jso["x"] = loc.x
        jso["y"] = loc.y
        jso["z"] = loc.z
        jso["yaw"] = loc.yaw
        jso["pitch"] = loc.pitch
        return jso
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