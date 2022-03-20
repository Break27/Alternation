/*
 * Copyright (c) 2022 Breakerbear
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.github.break27.util

class KTimer {
    var timerThread: TimerThread = TimerThread()
    var startTime: Long = 0

    @Volatile
    var set = false
    @Volatile
    var deltaTime: Long = 0

    init { reset() }

    /** Freeze the program for some time
     * @param time milliseconds
     */
    fun freeze(time: Long) {
        reset()
        while (true.also { set = it }) {
            if (calculate() >= time) {
                reset()
                break
            }
        }
    }

    fun setTimeout(timeout: Long, trigger: Trigger?) {
        reset()
        set = true
        timerThread = TimerThread(timeout, trigger)
        timerThread.start()
    }

    fun interval(interval: Long, attempts: Int, trigger: Trigger?) {
        loop@ for (i in 0 until attempts) {
            setTimeout(interval, trigger)
            while (timerThread.isAlive) {
                if (!set) break@loop
            }
        }
    }

    fun reset() {
        timerThread.interrupt()
        set = false
        deltaTime = 0
        startTime = System.currentTimeMillis()
    }

    fun set() {
        set = true
        setTimeout(0, null)
    }

    val time: Float
        get() = deltaTime.toFloat()

    fun finnish(): Float {
        timerThread.interrupt()
        set = false
        return deltaTime.toFloat()
    }

    private fun calculate(): Float {
        val time = System.currentTimeMillis()
        deltaTime = time - startTime
        return deltaTime.toFloat()
    }

    interface Trigger {
        fun fire()
    }

    inner class TimerThread @JvmOverloads constructor(timeout: Long = 0, trigger: Trigger? = null): Thread() {
        private val timeout: Long
        private val trigger: Trigger?

        init {
            name = "TimerUtils.timer"
            this.timeout = timeout
            this.trigger = trigger
        }

        override fun run() {
            while (!isInterrupted) {
                if (calculate() >= timeout && timeout > 0) {
                    trigger!!.fire()
                    break
                }
            }
        }
    }
}
