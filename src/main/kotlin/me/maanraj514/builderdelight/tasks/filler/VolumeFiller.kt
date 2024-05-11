package me.maanraj514.builderdelight.tasks.filler

import com.sk89q.worldedit.regions.Region

interface VolumeFiller {

    fun fill(region: Region)
}