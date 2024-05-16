package me.maanraj514.builderdelight.task.filler

import org.bukkit.Location

interface VolumeFiller {

    fun fill(cornerA: Location, cornerB: Location)
}