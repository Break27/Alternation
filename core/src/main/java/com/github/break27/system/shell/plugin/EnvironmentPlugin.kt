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

import com.github.break27.system.shell.KotlinShell
import org.jetbrains.kotlinx.ki.shell.*
import org.jetbrains.kotlinx.ki.shell.configuration.ReplConfiguration
import org.jetbrains.kotlinx.ki.shell.plugins.SymbolsTable
import kotlin.script.experimental.api.ResultValue
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.dependencies
import kotlin.script.experimental.jvm.JvmDependency

class EnvironmentPlugin: AlterShellPlugin {
    inner class ClassPath(conf: ReplConfiguration): BaseCommand() {
        override val name: String by conf.get("classpath")
        override val short: String by conf.get("cp")
        override val description: String = "show current script compilation classpath"

        override fun execute(line: String): Command.Result {
            val cp = repl.compilationConfiguration[ScriptCompilationConfiguration.dependencies]?.flatMap {
                if (it is JvmDependency) it.classpath else emptyList()
            }
            return Command.Result.Success(cp?.joinToString("\n") ?: "\n(No class loaded)")
        }
    }

    inner class Initialization(conf: ReplConfiguration): SystemCommand() {
        override val name: String by conf.get("init")
        override val short: String? = null
        override val description: String = "initialize or reset shell environment"

        override fun execute(line: String): Command.Result {
            val predefined = listOf(
                "val _HASH_ = ${repl.hashCode()}; " +
                "fun direct(hash: Int = _HASH_) { com.github.break27.system.shell.KShell.setSystemDirection(hash) };" +
                "fun echo(message: Any? = null) { com.github.break27.system.shell.KShell.echo(message) };" +
                "fun puts(message: Any?) { com.github.break27.system.shell.KShell.puts(message) };" +
                "fun error(message: String?) { com.github.break27.system.shell.KShell.error(message) };"
            )
            return Command.Result.RunSnippets(predefined)
        }
    }

    inner class System(config: ReplConfiguration): SystemCommand() {
        override val name: String by config.get("system")
        override val short: String by config.get("sys")
        override val description = "list system predefined symbols"

        override fun execute(line: String): Command.Result {
            return Command.Result.Success(listSymbols(systemTable, line, "\n(No symbol assigned)"))
        }
    }

    inner class List(config: ReplConfiguration): BaseCommand() {
        override val name: String by config.get("list")
        override val short: String by config.get("ls")
        override val description = "list defined symbols"

        override fun execute(line: String): Command.Result {
            return Command.Result.Success(listSymbols(userTable, line, "\n(No predefined constant or function)"))
        }
    }

    private fun listSymbols(table: SymbolsTable, line: String, fallback: String = ""): String {
        var result = fallback
        if (!table.isEmpty()) {
            val p = line.indexOf(' ')
            val pattern = if (p >= 0)line.substring(p + 1).trim() else null
            result = table.list(pattern).joinToString("\n")
        }
        return result
    }

    lateinit var repl: KotlinShell
    lateinit var userTable: SymbolsTable
    lateinit var systemTable: SymbolsTable

    override fun init(repl: Shell, config: ReplConfiguration) {
        this.repl = repl as KotlinShell
        systemTable = SymbolsTable()
        userTable = SymbolsTable()

        repl.eventManager.registerEventHandler(OnEval::class, object : EventHandler<OnEval> {
            override fun handle(event: OnEval) {
                val result = event.data().get().result
                if (result is ResultValue.Value || result is ResultValue.Unit) {
                    (if(repl.initialized()) userTable else systemTable).addNewSnippets(event.data())
                }
            }
        })

        repl.registerCommand(ClassPath(config))
        repl.registerCommand(Initialization(config))
        repl.registerCommand(System(config))
        repl.registerCommand(List(config))
    }

    override fun cleanUp() {
    }
}