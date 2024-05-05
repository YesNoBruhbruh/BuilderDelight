package me.maanraj514.builderdelight.task.structure

interface WorkLoad {

    fun compute()

    fun shouldBeRescheduled(): Boolean {
        return false
    }
}