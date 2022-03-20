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
package com.github.break27.util

import com.badlogic.gdx.graphics.Color

/**
 * @author break27
 */

enum class ColorMarkup(val color: Color, val mark: String) {
    WHITE(Color.WHITE, "\u2424"),
    RED(Color.RED, "\u2400"),
    GREEN(Color.GREEN, "\u2401"),
    BLUE(Color.BLUE,"\u2402"),
    YELLOW(Color.YELLOW, "\u2403"),
    ORANGE(Color.ORANGE, "\u2404"),
    PINK(Color.PINK, "\u2405"),
    PURPLE(Color.PURPLE, "\u2406"),
    CYAN(Color.CYAN, "\u2407"),
    FOREST(Color.FOREST, "\u2408");

    fun wrap(text: String): String {
        return mark + text + mark
    }
}

fun joinToString(array: Array<String>, separator: CharSequence = ", "): String {
    return array.joinToString(separator) { it }
}

/** Strip all Non-Printing Characters of a String Object.
 * Any character with an ASCII Code below 32 or of 127
 * will be deleted.
 */
fun stripNPC(text: CharArray): String {
    text.forEachIndexed { index, c ->
        if(c.code < 0x20 || c.code == 0x7F)
            text[index] = 0x0.toChar()
    }
    return String(text)
}