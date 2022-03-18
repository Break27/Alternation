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
import org.jetbrains.kotlinx.ki.shell.BaseCommand
import org.jetbrains.kotlinx.ki.shell.Command
import org.jetbrains.kotlinx.ki.shell.Shell
import org.jetbrains.kotlinx.ki.shell.configuration.ReplConfiguration
import kotlin.script.experimental.api.asSuccess
import kotlin.script.experimental.api.valueOrNull
import kotlin.script.experimental.jvm.KJvmEvaluatedSnippet
import kotlin.script.experimental.util.LinkedSnippetImpl

class EchoPlugin: AlterShellPlugin {
    inner class EchoCommand(conf: ReplConfiguration): BaseCommand() {
        override val name: String by conf.get("echo")
        override val short: String by conf.get("e")
        override val description: String = "print a message on screen"

        override val params = "[message]"

        override fun execute(line: String): Command.Result {
            val args = line.split(' ', limit = 2)

            repl.apply {
                return when(args.size) {
                    1 -> Command.Result.Success("")
                    else -> {
                        val eval = eval(args[1])
                        val result = repl.getEvalResult(eval.result)
                        if(result != null) {
                            Command.Result.Success(if(result.size==2) result[1] else result[0])
                        } else {
                            Command.Result.Failure(eval.getMessageOrEmpty())
                        }
                    }
                }
            }
        }
    }

    lateinit var repl: KotlinShell

    override fun init(repl: Shell, config: ReplConfiguration) {
        this.repl = repl as KotlinShell

        repl.registerCommand(EchoCommand(config))
    }

    override fun cleanUp() {
    }
}