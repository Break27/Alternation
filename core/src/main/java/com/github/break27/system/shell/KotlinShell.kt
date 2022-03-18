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
import com.badlogic.gdx.utils.Array
import org.jetbrains.kotlin.com.google.common.base.Objects
import org.jetbrains.kotlinx.ki.shell.*
import org.jetbrains.kotlinx.ki.shell.configuration.CachedInstance
import org.jetbrains.kotlinx.ki.shell.wrappers.ResultWrapper
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.NoSuchElementException
import kotlin.script.experimental.api.*
import kotlin.script.experimental.jvm.*
import kotlin.script.experimental.util.LinkedSnippet
import kotlin.script.experimental.util.LinkedSnippetImpl

class KotlinShell internal constructor(): Shell(defaultReplConfiguration(),
                                                defaultJvmScriptingHostConfiguration,
                                                defaultScriptCompilationConfiguration(),
                                                defaultScriptEvaluationConfiguration()
) {
    private val queue = Array<String>()
    private var shellThread: Thread? = null
    private var name: String = "kotlin"
    private var initialized = false
    private var active = false

    internal lateinit var handler: ShellHandler
    var sleepInterval = 100L

    fun create(handler: ShellHandler) {
        this.handler = handler
        shellThread = Thread({
            initialize()
            if (settings.sayHello) handler.success(printMOTD())
            handler.feed()

            fun compileKotlin(code: String) {
                val time = System.nanoTime()
                val eval = eval(code)
                evaluationTimeMillis = (System.nanoTime() - time) / 1_000_000
                when (eval.getStatus()) {
                    ResultWrapper.Status.ERROR, ResultWrapper.Status.INCOMPLETE -> {
                        // INCOMPLETE is not yet implemented
                        eval.result.reports.forEach {
                            handler.failed(it.render(withStackTrace = eval.isCompiled))
                        }
                    }
                    ResultWrapper.Status.SUCCESS -> {
                        val result = eval.result.valueOrNull()?.asSuccess()?.value as LinkedSnippet<*>
                        if(result.get() is KJvmEvaluatedSnippet) {
                            @Suppress("UNCHECKED_CAST")
                            eventManager.emitEvent(OnEval(result as LinkedSnippet<KJvmEvaluatedSnippet>))
                        }
                        val message = eval.getMessageOrEmpty()
                        if(message.isNotEmpty())
                            handler.success(message)
                        else {
                            val typeResult = getEvalResult(eval.result)?.get(0)
                            val type = typeResult?.split(' ', limit = 2)
                            if(type?.size == 2) handler.success(type[1])
                        }
                    }
                }
            }

            fun parseCommand(command: String) {
                try {
                    val action = commands.first { it.match(command) }
                    when (val result = action.execute(command)) {
                        is Command.Result.Success -> {
                            result.message.let { handler.success(it) }
                            currentSnippetNo.incrementAndGet()
                        }
                        is Command.Result.Failure -> {
                            handler.failed("ERROR ${result.message}")
                        }
                        is Command.Result.RunSnippets -> {
                            result.snippetsToRun.forEach(::compileKotlin)
                        }
                    }
                } catch (_: NoSuchElementException) {
                    handler.failed("Unknown command.")
                } catch (e: Exception) {
                    Gdx.app.error(
                        javaClass.name, "An error occurred " +
                                "while executing command '$command'.", e
                    )
                }
            }

            do if(queue.notEmpty()) {
                    val input = queue.pop()
                    if (input.startsWith(':')) parseCommand(input)
                    else compileKotlin(input)
                    handler.feed()
                } else try {
                    Thread.sleep(sleepInterval)
                } catch (_: InterruptedException) {
                    break
                }
            while (!Thread.currentThread().isInterrupted)
        }, "KotlinShell\$shell")
        shellThread?.start()
    }

    fun getShellName() : String {
        return name
    }

    fun update() {

    }

    fun destroy() {
        shellThread?.interrupt()
        evalThread.interrupt()
    }

    fun isRunning() : Boolean {
        return shellThread != null && shellThread?.isAlive == true
    }

    fun execute(command: String) {
        queue.add(command.trim().ifEmpty {";"})
    }

    override fun hashCode(): Int {
        return Objects.hashCode(queue, shellThread, name, initialized, sleepInterval)
    }

    override fun equals(other: Any?): Boolean {
        return (this === other || javaClass != other?.javaClass)
    }

    fun getEvalResult(result: ResultWithDiagnostics<*>): List<String>? {
        val resultOrNull = result.valueOrNull()
        return if(resultOrNull != null) {
            val resultStr = ((resultOrNull.asSuccess().value as LinkedSnippetImpl<*>).get() as KJvmEvaluatedSnippet).result
            resultStr.toString().split(" = ", limit = 2)
        } else null
    }

    private fun printMOTD() : String {
        var motd = printVersion()
        (replConfiguration as AlterReplConfiguration).plugins().forEach {
            val message = it.printMOTD()
            if(!message.isNullOrEmpty()) motd += ("\n" + it.printMOTD())
        }
        return motd
    }

    private fun printVersion() : String {
        val version = ApplicationProperties.version
        return "ki-shell $version/${KotlinVersion.CURRENT}"
    }

    private fun initialize() {
        if(!initialized) {
            replConfiguration.load()
            replConfiguration.plugins().forEach { it.init(this, replConfiguration) }
            settings = Settings(replConfiguration)
            queue.clear()
            initialized = true
            active = true
            // pre-defined script constants
            compileAndEval("val _HASH_ = ${hashCode()}; " +
                    "fun echo(message: Any?) { com.github.break27.system.shell.SysKshell.setSystemOut(_HASH_, message) };" +
                    "println(\"[KotlinShell@\$_HASH_] " +
                "${LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))} Shell created.\")"
            )
        }
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
                "core",
                classLoader = KotlinShell::class.java.classLoader,
                wholeClasspath = false
            )
        }
    }
}

private fun defaultScriptEvaluationConfiguration() : ScriptEvaluationConfiguration {
    return ScriptEvaluationConfiguration {
        jvm {
            baseClassLoader(KotlinShell::class.java.classLoader)
        }
    }
}