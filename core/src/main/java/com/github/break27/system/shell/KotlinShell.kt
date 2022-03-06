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
import com.badlogic.gdx.utils.Queue
import org.jetbrains.kotlinx.ki.shell.*
import org.jetbrains.kotlinx.ki.shell.configuration.CachedInstance
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.NoSuchElementException
import kotlin.script.experimental.api.*
import kotlin.script.experimental.jvm.*

class KotlinShell: Shell(defaultReplConfiguration(),
                         defaultJvmScriptingHostConfiguration,
                         defaultScriptCompilationConfiguration(),
                         defaultScriptEvaluationConfiguration()
) {

    private val io = ShellIO()
    private val queue = Queue<ShellTask>()
    private var initialized = false

    private val shellThread = Thread({
        // initialize
        replConfiguration.load()
        replConfiguration.plugins().forEach { it.init(this, replConfiguration) }
        settings = Settings(replConfiguration)
        if(settings.sayHello) io.send(printMOTD())
        initialized = true

        compileAndEval("println(\"[KotlinShell] " +
                "${LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))} Shell created.\")")

        fun compileKotlin(code: String) {
            val result = compileAndEval(code)
            if(result.isCompiled)
                io.send(result.getMessageOrEmpty())
            else {
                io.newLine("Error: " + result.getMessage())
                if(result.getErrorCause() != null)
                    io.feed("Exception: " + result.getErrorCause())
                io.send()
            }
        }

        fun parseCommand(command: String) {
            try {
                val action = commands.first { it.match(command) }
                when (val result = action.execute(command)) {
                    is Command.Result.Success -> {
                        result.message.let { io.send(it) }
                        currentSnippetNo.incrementAndGet()
                    }
                    is Command.Result.Failure -> {
                        io.send("Error: ${result.message}")
                    }
                    is Command.Result.RunSnippets -> {
                        result.snippetsToRun.forEach(::compileKotlin)
                    }
                }
            } catch (_: NoSuchElementException) {
                io.send("Unknown command '$command'")
            } catch (e: Exception) {
                Gdx.app.error(javaClass.name, "Unexpected error occurred " +
                        "while executing command '$command'.", e)
            }
        }

        do {
            if(queue.notEmpty()) {
                queue.last().execute()
                queue.removeLast()

                val input = io.read()
                if (input.startsWith(':')) parseCommand(input)
                else compileKotlin(input)
            }
        } while (!Thread.currentThread().isInterrupted)
    }, "KotlinShell\$shell")

    fun run() {
        shellThread.start()
    }

    fun halt() {
        shellThread.interrupt()
    }

    fun isRunning() : Boolean {
        return shellThread.isAlive
    }

    fun initialized() : Boolean {
        return initialized
    }

    fun setOutputCallback(callback: ShellOutputCallback) {
        io.setOutputCallback(callback)
    }

    fun send(line: String) {
        createTask(object : ShellTask {
            override fun execute() {
                io.feed(line)
            }
        })
    }

    private fun createTask(task: ShellTask) {
        if(shellThread.isAlive)
            queue.addFirst(task)
        else
            Gdx.app.error(javaClass.name, "Error: Shell terminated.")
    }

    private fun printMOTD() : String {
        var motd = printVersion()
        (replConfiguration as AlterReplConfiguration).plugins().forEach { motd += ("\n" + it.printMOTD()) }
        return motd
    }

    private fun printVersion() : String {
        val version = ApplicationProperties.version
        return "ki-shell $version/${KotlinVersion.CURRENT}"
    }
}

interface ShellOutputCallback {

    fun receive(result: String)
}

private interface ShellTask {

    fun execute()
}

private class ShellIO {

    private val pool = StringBuilder()
    private var callback : ShellOutputCallback? = null

    fun setOutputCallback(callback: ShellOutputCallback) {
        this.callback = callback
    }

    fun feed(line: String?) {
        pool.append(line)
    }

    fun newLine(line: String?) {
        pool.appendLine(line)
    }

    fun send(line: String?) {
        feed(line)
        send()
    }

    fun send() {
        if(pool.isNotBlank())
            callback?.receive(pool.toString())
        pool.clear()
    }

    fun read() : String {
        val ret = pool.toString()
        pool.clear()
        return ret
    }
}

private fun defaultReplConfiguration() : AlterReplConfiguration {
    val instance = CachedInstance<AlterReplConfiguration>()
    val klassName: String? = System.getProperty("config.class")

    return if (klassName != null) {
        instance.load(klassName, AlterReplConfiguration::class)
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