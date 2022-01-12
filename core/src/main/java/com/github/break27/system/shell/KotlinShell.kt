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

import com.badlogic.gdx.Gdx
import org.jetbrains.kotlinx.ki.shell.*
import org.jetbrains.kotlinx.ki.shell.configuration.CachedInstance
import org.jetbrains.kotlinx.ki.shell.configuration.ReplConfiguration
import kotlin.script.experimental.api.*
import kotlin.script.experimental.jvm.*

class KotlinShell : Shell(
        defaultReplConfiguration(),
        defaultJvmScriptingHostConfiguration,
        defaultScriptCompilationConfiguration(),
        defaultScriptEvaluationConfiguration()
    ) {

    private var io = ShellIO()

    private val shellThread = Thread {
        // initialize
        replConfiguration.load()
        replConfiguration.plugins().forEach { it.init(this, replConfiguration) }
        settings = Settings(replConfiguration)
        if(settings.sayHello) io.put(printMOTD())

        fun parseKotlinCode(code: String) {
            val result = compileAndEval(code).getMessageOrEmpty()
            io.put(result)
        }

        fun parseCommand(command: String) {
            try {
                val action = commands.first { it.match(command) }
                when (val result = action.execute(command)) {
                    is Command.Result.Success -> {
                        result.message.let { io.put(it) }
                        currentSnippetNo.incrementAndGet()
                    }
                    is Command.Result.Failure -> {
                        io.put("Error: ${result.message}")
                    }
                    is Command.Result.RunSnippets -> {
                        result.snippetsToRun.forEach(::parseKotlinCode)
                    }
                }
            } catch (_: NoSuchElementException) {
                io.put("Unknown command '$command'")
            } catch (e: Exception) {
                Gdx.app.error(javaClass.name, "Unexpected error occurred " +
                        "while executing command '$command'.", e)
            }
        }

        do {
            if(io.isUnread()) {
                val input = io.read(false)
                if (input.startsWith(':')) parseCommand(input)
                else parseKotlinCode(input)
            }
        } while (!Thread.currentThread().isInterrupted)
    }

    fun run() {
        shellThread.start()
    }

    fun halt() {
        shellThread.interrupt()
    }

    fun isRunning() : Boolean {
        return shellThread.isAlive
    }

    fun setOutputCallback(listener: ShellOutputCallback) {
        io.setOutputListener(listener)
    }

    fun send(line: String) {
        io.take(line)
    }

    private fun printMOTD() : String {
        //todo
        return "#Test_Feature"
    }
}

interface ShellOutputCallback {

    fun receive(result: String)
}

private class ShellIO {

    private val io = StringBuilder()
    private var unread = false
    private lateinit var callback : ShellOutputCallback

    fun setOutputListener(callback: ShellOutputCallback) {
        this.callback = callback
    }

    fun take(line: String?) {
        io.append(line)
        unread = true
    }

    fun put(line: String?) {
        take(line)
        read(true)
    }

    fun newLine(line: String?) {
        io.appendLine(line)
        read(true)
    }

    fun read(process: Boolean) : String {
        val ret = io.toString()
        io.clear()
        unread = false

        if(process) callback.receive(ret)
        return ret
    }

    fun isUnread() : Boolean {
        return unread
    }
}

private fun defaultReplConfiguration() : ReplConfiguration {
    val instance = CachedInstance<ReplConfiguration>()
    val klassName: String? = System.getProperty("config.class")

    return if (klassName != null) {
        instance.load(klassName, ReplConfiguration::class)
    } else {
        instance.get { object : AlterReplConfiguration() {}  }
    }
}

private fun defaultScriptCompilationConfiguration() : ScriptCompilationConfiguration {
    return ScriptCompilationConfiguration {
        jvm {
            dependenciesFromClassloader(
                    classLoader = KotlinShell::class.java.classLoader,
                    wholeClasspath = true
            )
        }
    }
}

private fun defaultScriptEvaluationConfiguration() : ScriptEvaluationConfiguration {
    return ScriptEvaluationConfiguration {
        jvm {
            baseClassLoader(Shell::class.java.classLoader)
        }
    }
}