package me.maanraj514.builderdelight.task

import com.google.common.base.Preconditions
import me.maanraj514.builderdelight.task.structure.WorkLoad
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class DistributedTickTask(private val distributionSize: Int) : BukkitRunnable(), WorkLoadRunnable {

    private val workloadMatrix = mutableListOf<LinkedList<WorkLoad>>()
    private var currentPosition = 0

    init {
        Preconditions.checkArgument(distributionSize > 0)

        for (i in 0 until distributionSize) {
            workloadMatrix.add(LinkedList())
        }
    }

    override fun add(workLoad: WorkLoad) {
        var smallestList = workloadMatrix[0] // get the smallest list.
        for (index in 0 until distributionSize) {
            if (smallestList.size == 0) break // if the smallest list is 0, we don't need to add the workload to next list.

            val next = workloadMatrix[index] // get the next workLoadList
            val size = next.size // get the size

            if (size < smallestList.size) {
                // if the size is less than the first smallestList
                // then the next list becomes the smallest list.
                smallestList = next
            }
        }

        smallestList.add(workLoad) // add the workload
    }

    private fun proceedPosition() {
        if (++currentPosition == distributionSize) {
            currentPosition = 0
        }
    }

    override fun run() {

        workloadMatrix[currentPosition].removeIf { load ->
            load.compute()
            !load.shouldBeRescheduled()
        }

        proceedPosition()
    }

    override fun cancel() {
        super.cancel()

        currentPosition = 0
        workloadMatrix.clear()
    }

    override fun cancelTask() {
        cancel()
    }
}