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

import org.jetbrains.kotlinx.ki.shell.BaseCommand
import org.jetbrains.kotlinx.ki.shell.Command
import org.jetbrains.kotlinx.ki.shell.Shell
import org.jetbrains.kotlinx.ki.shell.configuration.ReplConfiguration
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

    lateinit var repl: Shell

    override fun init(repl: Shell, config: ReplConfiguration) {
        this.repl = repl

        repl.registerCommand(ClassPath(config))
    }

    override fun cleanUp() {
    }
}