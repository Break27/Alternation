package com.github.break27.system.shell.plugin

import com.github.break27.system.exception.BadArgumentException
import com.github.break27.system.exception.RedundantException
import com.github.break27.system.shell.EvalResult
import com.github.break27.system.shell.KotlinShell
import org.jetbrains.kotlinx.ki.shell.BaseCommand
import org.jetbrains.kotlinx.ki.shell.Plugin

interface AlterShellPlugin: Plugin {

    @Deprecated("Outdated")
    override fun sayHello() { }

    fun printMOTD(): String? {
        return null
    }
}

abstract class AlterCommand: BaseCommand() {

    inline fun <reified T> evalValue(repl: KotlinShell, arg: String): T {
        val eval = repl.eval(arg)
        val result = repl.getEvalResult(eval.result)

        return if(result == null) {
            throw RedundantException(eval.getMessageOrEmpty())
        } else if(result !is EvalResult.Value || result.type != T::class.java) {
            throw BadArgumentException("Bad Argument. ${T::class.java.simpleName} is expected. (got ${result.typeName})")
        } else T::class.java.cast(result.value)
    }
}

abstract class SystemCommand: AlterCommand()