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

import com.badlogic.gdx.utils.Array

class SysKshell private constructor() {
    private val shells = Array<KotlinShell>()
    private val maxShellNum = 5

    companion object {
        private val sys = SysKshell()

        fun createKotlinShell(): KotlinShell {
            val shell = KotlinShell()
            sys.shells.add(shell)
            if(sys.shells.size > sys.maxShellNum) {
                sys.shells.get(0).destroy()
                sys.shells.removeIndex(0)
            }
            return shell
        }

        fun setSystemOut(hash: Int, message: Any?) {
            synchronized(this) {
                findShell(hash)?.handler?.feed(message.toString())
            }
        }

        fun setSystemErr(hash: Int, message: Any?) {
            findShell(hash)?.handler?.failed(message.toString())
        }

        private fun findShell(hash: Int): KotlinShell? {
            return sys.shells.find { it.hashCode() == hash }
        }
    }
}