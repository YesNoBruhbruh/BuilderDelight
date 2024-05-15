package me.maanraj514.builderdelight.util

import com.google.gson.GsonBuilder
import java.io.FileReader
import java.io.FileWriter

object JsonUtil {

    private val gson = GsonBuilder().setPrettyPrinting().create()

    fun toJson(obj: Any): String = gson.toJson(obj)
    fun toJson(obj: Any, writer: FileWriter) = gson.toJson(obj, writer)

    fun <T> fromJson(reader: FileReader, type: Class<T>) = gson.fromJson(reader, type)
    fun <T> fromJson(json: String, type: Class<T>) = gson.fromJson(json, type)
}