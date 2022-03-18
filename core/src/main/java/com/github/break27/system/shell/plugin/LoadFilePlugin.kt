/**************************************************************************
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
 * along with this program.  If not, see <https:></https:>//www.gnu.org/licenses/>.
 */
package com.github.break27.system.shell.plugin

import com.github.break27.Game3
import org.jetbrains.kotlinx.ki.shell.*
import org.jetbrains.kotlinx.ki.shell.configuration.ReplConfiguration
import org.jetbrains.kotlinx.ki.shell.plugins.SymbolsTable
import kotlin.script.experimental.api.ResultValue

/**
 * @author break27
 */
class LoadFilePlugin: AlterShellPlugin {
    inner class Load(config: ReplConfiguration): BaseCommand() {
        override val name: String by config.get("load")
        override val short: String by config.get("l")
        override val description = "load and evaluate a script"

        override val params = "<filepath>"

        override fun execute(line: String): Command.Result {
            val args = line.split(' ', limit = 2)
            return when(args.size) {
                1 -> Command.Result.Failure("Too less argument. (Minimum of 1)")
                2 -> try {
                    val content = Game3.FileResolver.resolve(args[1]).readString("UTF-8")
                    Command.Result.RunSnippets(listOf(content))
                } catch (e: Exception) {
                    Command.Result.Failure(e.message!!)
                }
                else -> Command.Result.Failure("Too many argument. (Maximum of 1)")
            }
        }
    }

    override fun cleanUp() {
    }

    override fun init(repl: Shell, config: ReplConfiguration) {
        repl.registerCommand(Load(config))
    }

}