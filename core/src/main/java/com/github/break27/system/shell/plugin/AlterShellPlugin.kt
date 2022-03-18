package com.github.break27.system.shell.plugin

import org.jetbrains.kotlinx.ki.shell.Plugin

interface AlterShellPlugin : Plugin {

    @Deprecated("Outdated")
    override fun sayHello() { }

    fun printMOTD(): String? {
        return null
    }
}