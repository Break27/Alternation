/**************************************************************************
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
 * along with this program.  If not, see <https:></https:>//www.gnu.org/licenses/>.
 */
package com.github.break27.system.shell.plugin

import org.jetbrains.kotlinx.ki.shell.BaseCommand
import org.jetbrains.kotlinx.ki.shell.Command
import org.jetbrains.kotlinx.ki.shell.Shell
import org.jetbrains.kotlinx.ki.shell.configuration.ReplConfiguration

/**
 * @author break27
 */
class EditorPlugin: AlterShellPlugin {
    inner class Editor(config: ReplConfiguration): BaseCommand() {
        override val name: String by config.get("edit")
        override val short: String by config.get("ed")
        override val description = "enter editor view"

        override fun execute(line: String): Command.Result {
            TODO("Not yet implemented")
        }
    }

    override fun cleanUp() {
    }

    override fun init(repl: Shell, config: ReplConfiguration) {
        repl.registerCommand(Editor(config))
    }
}