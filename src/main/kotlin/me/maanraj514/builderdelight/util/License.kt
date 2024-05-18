package me.maanraj514.builderdelight.util

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import me.maanraj514.builderdelight.BuilderDelight
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.*
import java.util.function.Consumer
import kotlin.collections.ArrayList

object License {

    private const val URL: String = "https://lunarmc.codava.net"

    private const val PRODUCT: String = "BuilderDelight"

    fun isLicenseValid(id: String, plugin: BuilderDelight): Boolean {

        val consoleSender = plugin.server.consoleSender

        if (!isAlive()) {

            consoleSender.sendColorizedMessage("<red>------------------</red>")
            consoleSender.sendColorizedMessage("<red>------------------</red>")
            consoleSender.sendColorizedMessage("<red>PLEASE CONTACT THE AUTHOR ON YOUR DISCORD</red>")
            consoleSender.sendColorizedMessage("<red>NOTE: THE PLUGIN HAS BEEN ENABLED.</red>")
            consoleSender.sendColorizedMessage("<red>------------------</red>")
            consoleSender.sendColorizedMessage("<red>------------------</red>")

            return true
        }

        val licenseData = getLicenseById(id)

        if (licenseData == null || licenseData.product != PRODUCT) {
            consoleSender.sendColorizedMessage("<red>------------------</red>")
            consoleSender.sendColorizedMessage("<red>------------------</red>")
            consoleSender.sendColorizedMessage("<red>INVALID LICENSE KEY '$id'</red>")
            consoleSender.sendColorizedMessage("<red></red>")
            consoleSender.sendColorizedMessage("<red>PLEASE CONTACT THE AUTHOR ON YOUR DISCORD</red>")
            consoleSender.sendColorizedMessage("<red>TO GET $PRODUCT LICENSE KEY.</red>")
            consoleSender.sendColorizedMessage("<red>------------------</red>")
            consoleSender.sendColorizedMessage("<red>------------------</red>")

            return false
        }


        val dateFormat = SimpleDateFormat()

        consoleSender.sendColorizedMessage("<green>-------------------</green>")
        consoleSender.sendColorizedMessage("<green>License Key: ${id.substring(0, id.length / 5)}**********</green>")
        consoleSender.sendColorizedMessage("<green>Owner: ${licenseData.owner}</green>")
        consoleSender.sendColorizedMessage("<green>Creation Date: ${dateFormat.format(Date(licenseData.creation_date))}</green>")
        consoleSender.sendColorizedMessage("<green>Product: ${licenseData.product}</green>")
        consoleSender.sendColorizedMessage("<green>Allowed IPs: ${licenseData.ips}</green>")
        consoleSender.sendColorizedMessage("<green>-------------------</green>")

        return true
    }

    fun isAlive(): Boolean {
        val httpClient: HttpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build()

        try {
            val request: HttpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(URL))
                .setHeader("User-Agent", "Licensing System") // add request header
                .build()

            val response: HttpResponse<String> = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

            return response.statusCode() == 200
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    fun getLicenseById(id: String): LicenseData? {
        val requestStr = "/license/id/$id"

        val httpClient: HttpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build()

        try {
            val request: HttpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(URL + requestStr))
                .setHeader("User-Agent", "Licensing System") // add request header
                .build()

            val response: HttpResponse<String> = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

            if (response.statusCode() != 200) return null

            val gson = Gson()

            val jsonObject: JsonObject = JsonParser().parse(response.body()).getAsJsonObject()

            val owner = jsonObject["owner"].asString
            val product = jsonObject["product"].asString
            val creationDate = jsonObject["creation_date"].asLong
            val ips: MutableList<String> = ArrayList()
            jsonObject["ips"].asJsonArray.forEach(Consumer { jsonElement: JsonElement ->
                ips.add(
                    jsonElement.asString
                )
            })
            return LicenseData(id, owner, product, creationDate, ips)
        } catch (e: Exception) {
            return null
        }
    }

    @JvmRecord
    data class LicenseData(
        val id: String,
        val owner: String,
        val product: String,
        val creation_date: Long,
        val ips: List<String>
    )
}