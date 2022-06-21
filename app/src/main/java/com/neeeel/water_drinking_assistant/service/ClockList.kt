package com.neeeel.water_drinking_assistant.service

import android.util.Log
import com.neeeel.water_drinking_assistant.database.bean.Clock

class ClockList {

    companion object {
        private const val TAG = "ClockList"
    }

    private val clockList = mutableListOf<ClockDecorator>()

    fun addCount(): List<String> {
        val notifyList by lazy { mutableListOf<String>() }
        clockList.forEach {
            it.now += 1
            if (it.now >= it.interval) {
                notifyList.add(it.title)
                // 置为 0，重新循环
                it.now = 0
            }
        }
        return notifyList
    }

    fun compare(list: List<Clock>) {
        val time = System.currentTimeMillis()

        val iterator = clockList.iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            if (!list.any { it.id == next.id }) {
                iterator.remove()
            }
        }
        val clockIterator = list.iterator()
        while (clockIterator.hasNext()) {
            val next = clockIterator.next()
            if (!clockList.any { it.id == next.id }) {
                clockList.add(ClockDecorator(
                    id = next.id?: -1,
                    title = next.title,
                    interval = next.interval,
                    now = 0
                ))
            }
        }
        Log.d(TAG, "compare: ${System.currentTimeMillis() - time}")
    }

    class ClockDecorator(
        var id: Long,
        var title: String,
        var interval: Int,
        var now: Int
    )
}