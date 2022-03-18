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

package com.github.break27.system.shell.plugin

import org.jetbrains.kotlinx.ki.shell.*
import org.jetbrains.kotlinx.ki.shell.configuration.ReplConfiguration
import org.jetbrains.kotlinx.ki.shell.plugins.SymbolsTable
import kotlin.script.experimental.api.ResultValue

class RuntimePlugin: AlterShellPlugin {
    inner class List(config: ReplConfiguration): BaseCommand() {
        override val name: String by config.get("list")
        override val short: String by config.get("ls")
        override val description = "list defined symbols"

        override fun execute(line: String): Command.Result {
            var result = "\n(No symbol assigned)"
            if (!table.isEmpty()) {
                val p = line.indexOf(' ')
                val pattern = if (p >= 0)line.substring(p + 1).trim() else null
                result = table.list(pattern).joinToString("\n")
            }
            return Command.Result.Success(result)
        }
    }

    override fun cleanUp() {
    }

    lateinit var repl: Shell
    lateinit var table: SymbolsTable

    override fun init(repl: Shell, config: ReplConfiguration) {
        this.repl = repl
        this.table = SymbolsTable()

        repl.eventManager.registerEventHandler(OnEval::class, object : EventHandler<OnEval> {
            override fun handle(event: OnEval) {
                val result = event.data().get().result
                if (result is ResultValue.Value || result is ResultValue.Unit) {
                    table.addNewSnippets(event.data())
                }
            }
        })

        repl.registerCommand(List(config))
    }
}