package me.maanraj514.builderdelight.task

class ScheduledWorkLoadRunnable : WorkLoadRunnable(), Runnable {

    init {
        MAX_MILLIS_PER_TICK = 20.0
    }
}