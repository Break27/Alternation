package com.github.break27.system.shell.plugin

import kotlinx.cli.HelpPrinter
import org.jetbrains.kotlinx.ki.shell.BaseCommand
import org.jetbrains.kotlinx.ki.shell.Command
import org.jetbrains.kotlinx.ki.shell.Shell
import org.jetbrains.kotlinx.ki.shell.configuration.ReplConfiguration
import org.jetbrains.kotlinx.ki.shell.match

class HelpPlugin: AlterShellPlugin {
    inner class Help(conf: ReplConfiguration): BaseCommand() {
        override val name: String by conf.get("help")
        override val short: String by conf.get("h")
        override val description: String = "print this summary or command-specific help"

        override val params = "[command]"

        override fun execute(line: String): Command.Result {
            val args = line.split(' ')
            val commands = repl.listCommands()

            repl.apply {
                return if (args.size == 1) {
                    val help = commands.joinToString(separator = "") { if(it !is SystemCommand) "${it.desc()}\n" else "" }
                    Command.Result.Success(help)
                } else {
                    val command = args[1]
                    try {
                        val res = commands.first { it.match(":$command") }
                        Command.Result.Success("\n" + res.help())
                    } catch (_: NoSuchElementException) {
                        Command.Result.Failure("No such command or documentation unavailable.")
                    }
                }
            }
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
}