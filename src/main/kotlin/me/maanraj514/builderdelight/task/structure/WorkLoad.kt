package me.maanraj514.builderdelight.task.structure

interface WorkLoad {

    fun compute()

    // this is used for example spawning in a lot of particles at the same location.
    fun shouldBeRescheduled(): Boolean {
        return false
    }
}