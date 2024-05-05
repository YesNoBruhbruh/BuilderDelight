package me.maanraj514.builderdelight.task.filler

import org.bukkit.Location
import org.bukkit.Material

interface VolumeFiller {

    fun fill(cornerA: Location, cornerB: Location, material: Material)
}