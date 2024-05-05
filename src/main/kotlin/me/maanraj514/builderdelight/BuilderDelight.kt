package me.maanraj514.builderdelight

import com.jeff_media.customblockdata.CustomBlockData
import dev.respark.licensegate.LicenseGate
import me.maanraj514.builderdelight.command.BuildModeCommand
import me.maanraj514.builderdelight.command.ConfigReloadCommand
import me.maanraj514.builderdelight.command.PosCommand
import me.maanraj514.builderdelight.command.TestClearCommand
import me.maanraj514.builderdelight.listener.BuildModeListener
import me.maanraj514.builderdelight.task.ClearBlocksTask
import me.maanraj514.builderdelight.task.ScheduledWorkLoadRunnable
import me.maanraj514.builderdelight.worldedit.WorldEditListener
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.plugin.java.JavaPlugin
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class BuilderDelight : JavaPlugin() {

    val builders = mutableSetOf<UUID>()
    val BUILDER_BLOCK_KEY = NamespacedKey(this, "builderModeBlock")

    lateinit var scheduledWorkLoadRunnable: ScheduledWorkLoadRunnable
    private lateinit var clearBlocksTask: ClearBlocksTask

    private val mm = MiniMessage.miniMessage()

    override fun onEnable() {
        saveDefaultConfig()

        licenseCheck()
        checkIp()
        registerCommands()
        registerListeners()

        if (Bukkit.getPluginManager().getPlugin("WorldEdit") != null) {
            WorldEditListener(this)
        }

        scheduledWorkLoadRunnable = ScheduledWorkLoadRunnable(this)
        scheduledWorkLoadRunnable.runTaskTimer(this, 1L, 1L)

        clearBlocksTask = ClearBlocksTask(this)
        clearBlocksTask.runTaskTimer(this, config.getInt("delay").toLong(), config.getInt("interval").toLong())
    }

    override fun onDisable() {
        clearBlocksTask.cancel()
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
}