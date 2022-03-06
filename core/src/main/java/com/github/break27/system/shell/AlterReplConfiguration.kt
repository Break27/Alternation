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

import com.github.break27.system.shell.plugin.AlterShellPlugin
import com.github.break27.system.shell.plugin.HelpPlugin
import org.jetbrains.kotlinx.ki.shell.configuration.CachedInstance
import org.jetbrains.kotlinx.ki.shell.configuration.ListConverter
import org.jetbrains.kotlinx.ki.shell.configuration.PropertyBasedReplConfiguration
import org.jetbrains.kotlinx.ki.shell.configuration.TrimConverter
import java.util.*

abstract class AlterReplConfiguration(defaultPlugins: List<String> = DEFAULT_PLUGINS) :
    PropertyBasedReplConfiguration(Properties(), defaultPlugins) {
    private val plugins = linkedMapOf<String, CachedInstance<AlterShellPlugin>>()

    companion object {
        val DEFAULT_PLUGINS = listOf(
            /*
            LoadFilePlugin::class.qualifiedName!!,
            RuntimePlugin::class.qualifiedName!!,
            HelpPlugin::class.qualifiedName!!,
            ConfigPlugin::class.qualifiedName!!,
            DependenciesPlugin::class.qualifiedName!!,
            ExecutionEnvironmentPlugin::class.qualifiedName!!
             */
            HelpPlugin::class.qualifiedName!!
        )
    }

    override fun load() {
        val pluginClasses = get("plugins", ListConverter(TrimConverter), defaultPlugins)

        pluginClasses.forEach { klassName ->
            val instance = CachedInstance<AlterShellPlugin>()
            instance.load(klassName, AlterShellPlugin::class)
            plugins[klassName] = instance
        }
    }

    override fun getPlugin(klassName: String): AlterShellPlugin? = plugins[klassName]?.get()

    override fun plugins(): Iterator<AlterShellPlugin> = plugins.values.map { it.get()!! }.iterator()
}