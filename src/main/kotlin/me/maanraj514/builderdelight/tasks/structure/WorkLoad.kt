package me.maanraj514.builderdelight.tasks.structure

interface WorkLoad {

    fun compute()

    fun shouldBeRescheduled(): Boolean {
        return false
    }
}