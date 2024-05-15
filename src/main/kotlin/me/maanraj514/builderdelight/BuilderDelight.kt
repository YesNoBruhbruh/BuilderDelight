package me.maanraj514.builderdelight

import com.jeff_media.customblockdata.CustomBlockData
import dev.respark.licensegate.LicenseGate
import me.maanraj514.builderdelight.command.BuildModeCommand
import me.maanraj514.builderdelight.command.ConfigReloadCommand
import me.maanraj514.builderdelight.command.PosCommand
import me.maanraj514.builderdelight.command.TestClearCommand
import me.maanraj514.builderdelight.listener.BuildModeListener
import me.maanraj514.builderdelight.tasks.ClearBlocksTask
import me.maanraj514.builderdelight.tasks.DistributedTickTask
import me.maanraj514.builderdelight.util.JsonUtil
import me.maanraj514.builderdelight.worldedit.WorldEditListener
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer.gson
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.block.Block
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class BuilderDelight : JavaPlugin() {

    val builders = mutableSetOf<UUID>()
    val builderBlocks = mutableListOf<Location>()

    val blocksFile = File(dataFolder.absolutePath + "/blocks.json")

    val BUILDER_BLOCK_KEY = NamespacedKey(this, "builderModeBlock")

    lateinit var distributedTickTask: DistributedTickTask
    private lateinit var clearBlocksTask: ClearBlocksTask

    private val mm = MiniMessage.miniMessage()

    private var canLoad = false

    override fun onEnable() {
        saveDefaultConfig()

        licenseCheck()
        checkIp()
        registerCommands()
        registerListeners()

        WorldEditListener(this)

        distributedTickTask = DistributedTickTask(10000) // 20 for 20 ticks
        distributedTickTask.runTaskTimer(this, 0L, 20L)

//        loadBlocks()

        if (canLoad) {
            println("Can load!")

//          clearBlocksTask = ClearBlocksTask(this)
//          clearBlocksTask.runTaskTimer(this, config.getInt("delay").toLong(), config.getInt("interval").toLong())
        }

        server.consoleSender.sendMessage("Found WorldEdit! loading support...")
    }

    override fun onDisable() {
//        saveBlocks()
        println("builderBlocks size is ${builderBlocks.size}")

        builders.clear()
        builderBlocks.clear()

//        clearBlocksTask.cancel()
        distributedTickTask.cancel()
    }

    private fun registerListeners() {
        CustomBlockData.registerListener(this)
        Bukkit.getPluginManager().registerEvents(BuildModeListener(this), this)
    }

    private fun registerCommands() {
        getCommand("buildmode")?.setExecutor(BuildModeCommand(this))

        val posCommand = PosCommand(this)
        getCommand("pos1")?.setExecutor(posCommand)
        getCommand("pos2")?.setExecutor(posCommand)
        getCommand("savePos")?.setExecutor(posCommand)

        getCommand("builderdelight-reloadconfig")?.setExecutor(ConfigReloadCommand(this))

        getCommand("testclear")?.setExecutor(TestClearCommand(this))
    }

    private fun loadBlocks() {

        if (!blocksFile.exists()) {
            blocksFile.createNewFile()
        }

        val reader = FileReader(blocksFile)

        val blocks: Array<Location> = JsonUtil.fromJson(reader, Array<Location>::class.java) ?: return
        builderBlocks.addAll(blocks)

        println("Builder-Blocks loaded!")

        canLoad = true
    }

    fun addBlock(block: Block) {
        println("called addBlock()!")

        // removes useless customBlockData checking.
        val type = block.type
        if (type == Material.AIR) return

        val hasCustomBlock = CustomBlockData.hasCustomBlockData(block, this)
        if (!hasCustomBlock) return

        val customBlockData = CustomBlockData(block, this)
        if (customBlockData.has(BUILDER_BLOCK_KEY)) {
            builderBlocks.add(block.location)
            println("found a builder-block!")
        }
    }

    fun saveBlocks() {
        val writer = FileWriter(blocksFile, false)
        JsonUtil.toJson(builderBlocks, writer)

        writer.flush()
        writer.close()
    }

    private fun licenseCheck() {
        val isLicenseValid = LicenseGate("a1c87")
            .verify(config.getString("license-key"), "BuilderDelight")
            .isValid

        if (!isLicenseValid) {
            val errorMessage = mm.deserialize("<red> INVALID LICENSE KEY! </red>")
            server.consoleSender.sendMessage(errorMessage)

            Bukkit.getPluginManager().disablePlugin(this)
        }
    }

    private fun checkIp() {
        try {
            val url = URL("https://www.ipchicken.com/")
            url.openConnection()
            val scanner = Scanner(url.openStream())
            val stringBuffer = StringBuffer()
            while (scanner.hasNext()) {
                stringBuffer.append(scanner.next())
            }

            val result = stringBuffer.toString()
                .replace("<", "")
                .replace("[", "")
                .replace("]", "")
                .replace(">", "")
                .replace("*", "")
                .replace("^", "")

            val ipAddress = getIpAddressFromString(result)

            if (isLicensedIp(ipAddress)){
                server.consoleSender.sendMessage(mm.deserialize("<green> Your IP address is licensed! </green>"))
            } else {
                server.consoleSender.sendMessage(mm.deserialize("<red> Your ip: $ipAddress is not licensed! </red>"))
                Bukkit.getPluginManager().disablePlugin(this)
            }

        } catch (ex: MalformedURLException) {
            println(ex)
        }
    }

    private fun isLicensedIp(ip: String) : Boolean {
        return getLicensedIps().contains(ip)
    }

    private fun getLicensedIps() : List<String> {
        val ipList = mutableListOf<String>()
        ipList.add("35.240.207.53")
        ipList.add("62.72.177.7")

        return ipList
    }

    private fun getIpAddressFromString(string: String) : String {
        val IPADDRESS_PATTERN =
            "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)"

        val pattern: Pattern = Pattern.compile(IPADDRESS_PATTERN)
        val matcher: Matcher = pattern.matcher(string)
        return if (matcher.find()) {
            matcher.group()
        } else {
            "0.0.0.0"
        }
    }

    fun getPos1(): Location {
        return Location(
            Bukkit.getWorld(config.get("pos1.world").toString()),
            config.get("pos1.x").toString().toDouble(),
            config.get("pos1.y").toString().toDouble(),
            config.get("pos1.z").toString().toDouble()
        )
    }

    fun getPos2(): Location {
        return Location(
            Bukkit.getWorld(config.get("pos2.world").toString()),
            config.get("pos2.x").toString().toDouble(),
            config.get("pos2.y").toString().toDouble(),
            config.get("pos2.z").toString().toDouble()
        )
    }

    fun getLocations(): List<Location> {
        val locations = mutableListOf<Location>()

        val pos1 = getPos1()
        val pos2 = getPos2()

        val world = pos1.world

        val topBlockX: Int = pos1.blockX.coerceAtLeast(pos2.blockX)
        val bottomBlockX: Int = pos1.blockX.coerceAtMost(pos2.blockX)

        val topBlockY: Int = pos1.blockY.coerceAtLeast(pos2.blockY)
        val bottomBlockY: Int = pos1.blockY.coerceAtMost(pos2.blockY)

        val topBlockZ: Int = pos1.blockZ.coerceAtLeast(pos2.blockZ)
        val bottomBlockZ: Int = pos1.blockZ.coerceAtMost(pos2.blockZ)

        for (x in bottomBlockX..topBlockX) {
            for (y in bottomBlockY..topBlockY) {
                for (z in bottomBlockZ..topBlockZ) {
                    locations.add(Location(world, x.toDouble(), y.toDouble(), z.toDouble()))
                }
            }
        }

        return locations
    }
}