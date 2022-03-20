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

package com.github.break27.system.shell

import com.badlogic.gdx.ApplicationLogger
import com.badlogic.gdx.utils.Array

class KShell private constructor() {
    private var Log: ApplicationLogger? = null
    private val shells = Array<KotlinShell>()
    private val maxShellNum = 5

    companion object System {
        private val sys = KShell()
        private var tempDest: ShellHandler? = null
        private var record = 0

        fun createKotlinShell(): KotlinShell {
            val shell = KotlinShell()
            sys.shells.add(shell)
            if(sys.shells.size > sys.maxShellNum) {
                sys.shells.get(0).destroy()
                sys.shells.removeIndex(0)
            }
            return shell
        }

        fun setLogger(logger: ApplicationLogger) {
            sys.Log = logger
        }

        fun getLogger(): ApplicationLogger? {
            return sys.Log
        }

        fun echo(message: Any? = null) {
            val direct = getDirect()
            if(message != null) direct?.success(message.toString())
            else direct?.feed("\n")
        }

        fun puts(message: Any?) {
            getDirect()?.feed(message.toString())
        }

        fun error(message: String?) {
            getDirect()?.failed(message)
        }

        fun setSystemDirection(hash: Int) {
            if(record != hash) {
                tempDest = findShell(hash)?.handler
                record = hash
            }
        }

        private fun getDirect(): ShellHandler? {
            if(tempDest == null) sys.Log?.error(KShell::class.qualifiedName,
                "System Destination has not been set!")
            return tempDest
        }

        private fun findShell(hash: Int): KotlinShell? {
            return sys.shells.find { it.hashCode() == hash }
        }
    }
}