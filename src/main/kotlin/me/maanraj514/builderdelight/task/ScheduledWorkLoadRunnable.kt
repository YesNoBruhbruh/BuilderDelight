package me.maanraj514.builderdelight.task

import me.maanraj514.builderdelight.BuilderDelight
import me.maanraj514.builderdelight.task.structure.WorkLoad
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class ScheduledWorkLoadRunnable(private val plugin: BuilderDelight) : BukkitRunnable() {

    private val MAX_MILLIS_PER_TICK = 2.5
    private val MAX_NANOS_PER_TICK = (MAX_MILLIS_PER_TICK * 1E6).toInt()

    private val workLoadDeque: Deque<WorkLoad> = ArrayDeque()

    fun addWorkLoad(workLoad: WorkLoad) {
        workLoadDeque.add(workLoad)
    }

    override fun run() {

        val stopTime = System.nanoTime() + MAX_NANOS_PER_TICK

        val lastElement: WorkLoad? = workLoadDeque.peekLast()
        var nextLoad: WorkLoad? = null

        // Compute all loads until the time is run out or the queue is empty, or we did one full cycle
        // The lastElement is here, so we don't cycle through the queue several times
        while (System.nanoTime() <= stopTime && !workLoadDeque.isEmpty() && nextLoad != lastElement) {
            nextLoad = workLoadDeque.poll()
            nextLoad.compute()
            if (nextLoad.shouldBeRescheduled()) {
                addWorkLoad(nextLoad)
            }
        }

        if (plugin.config.getBoolean("clear-message")) {
            println("clearing blocks...")
        }
    }
}