package com.github.break27.system.shell.plugin

import kotlinx.cli.HelpPrinter
import org.jetbrains.kotlinx.ki.shell.BaseCommand
import org.jetbrains.kotlinx.ki.shell.Command
import org.jetbrains.kotlinx.ki.shell.Shell
import org.jetbrains.kotlinx.ki.shell.configuration.ReplConfiguration
import org.jetbrains.kotlinx.ki.shell.match

class HelpPlugin : AlterShellPlugin {
    inner class Help(conf: ReplConfiguration): BaseCommand() {
        override val name: String by conf.get(default = "help")
        override val short: String by conf.get(default = "h")
        override val description: String = "print this summary or command-specific help"

        override val params = "[command]"

        override fun execute(line: String): Command.Result {
            val args = line.split(' ')
            val commands = repl.listCommands()
            var output = ""

            repl.apply {
                if (args.size == 1) {
                    val help = commands.joinToString(separator = "\n") { it.desc() }
                    output = help
                } else {
                    val command = args[1]
                    try {
                        val res = commands.first { it.match(":$command") }
                        output += ("\n" + res.help())
                    } catch (_: NoSuchElementException) {
                        return Command.Result.Failure("$command: no such command. Type :help for help.")
                    }
                }
            }
            return Command.Result.Success(output)
        }
    }

    lateinit var repl: Shell

    override fun init(repl: Shell, config: ReplConfiguration) {
        this.repl = repl

        repl.registerCommand(Help(config))
    }

    override fun printMOTD(): String {
        return "type :h for help"
    }

    override fun cleanUp() {}

    class StringHelpPrinter(private val syntaxWidth: Int = 24): HelpPrinter {
        private val sb = StringBuilder()

        override fun printText(text: String) {
            sb.appendLine(text)
        }

        override fun printSeparator() {
            sb.appendLine()
        }

        override fun printEntry(helpEntry: String, description: String) {
            if (helpEntry.length <= syntaxWidth) {
                sb.appendLine("  ${helpEntry.padEnd(syntaxWidth)}  $description")
            }
            else {
                sb.appendLine("  $helpEntry")
                sb.appendLine("  ${"".padEnd(syntaxWidth)}  $description")
            }
        }

        override fun toString(): String = sb.toString()
    }
}