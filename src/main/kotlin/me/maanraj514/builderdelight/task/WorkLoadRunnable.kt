package me.maanraj514.builderdelight.task

import me.maanraj514.builderdelight.task.structure.WorkLoad
import java.util.*
import java.util.ArrayDeque

abstract class WorkLoadRunnable : Runnable {

    // FOR FUTURE REFERENCE:

    // SAFE VALUES: (all of these work)
    // (2.5 very-slow) default max_millis_per_tick
    // (5 slow speed)
    // (10 normal)
    // (20 fast)

    // EXPERIMENTAL: (doesn't work)
    // (40 very fast)
    // (80 extreme)
    // (100 too fast)

    private val workLoadDeque: Deque<WorkLoad> = ArrayDeque()

    protected var MAX_MILLIS_PER_TICK = 2.5 // higher amount will allow for more loads to be computed.
    private val MAX_NANOS_PER_TICK = (MAX_MILLIS_PER_TICK * 1E6).toInt()

    open fun addWorkLoad(workLoad: WorkLoad) {
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
    }
}