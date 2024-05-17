package me.maanraj514.builderdelight.util

import dev.respark.licensegate.LicenseGate
import me.maanraj514.builderdelight.BuilderDelight
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

object Auth {

    private val mm = MiniMessage.miniMessage()

    fun licenseCheck(plugin: BuilderDelight) {
        val isLicenseValid = LicenseGate("a1c87")
            .verify(plugin.config.getString("license-key"), "BuilderDelight")
            .isValid

        if (!isLicenseValid) {
            val errorMessage = mm.deserialize("<red> INVALID LICENSE KEY! </red>")
            plugin.server.consoleSender.sendMessage(errorMessage)

            Bukkit.getPluginManager().disablePlugin(plugin)
        }
    }

    fun checkIp(plugin: BuilderDelight) {
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
                plugin.server.consoleSender.sendMessage(mm.deserialize("<green> Your IP address is licensed! </green>"))
            } else {
                plugin.server.consoleSender.sendMessage(mm.deserialize("<red> Your ip: $ipAddress is not licensed! </red>"))
                Bukkit.getPluginManager().disablePlugin(plugin)
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
}