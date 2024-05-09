package me.maanraj514.builderdelight.task

import me.maanraj514.builderdelight.task.structure.WorkLoad

interface WorkLoadRunnable {

    fun add(workLoad: WorkLoad)
    fun cancelTask()
}