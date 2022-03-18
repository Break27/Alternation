/**************************************************************************
 * Copyright (c) 2021 Breakerbear
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
 *************************************************************************/

package com.github.break27.system;

import com.badlogic.gdx.utils.I18NBundle;

import java.util.HashMap;

/**
 * @author break27
 */
public abstract class Locales {

    private static final HashMap<String, I18NBundle> Bundles = new HashMap<>();

    public static I18NBundle getBundle(String name) {
        return Bundles.get(name);
    }

    public static void putBundle(String name, I18NBundle bundle) {
        Bundles.put(name, bundle);
    }

    public static String translate(String bundleName, String type, String name) {
        return Bundles.get(bundleName).get(parse(type, name));
    }

    public static String translate(String bundleName, String type, String name, Object... args) {
        return Bundles.get(bundleName).format(parse(type, name), args);
    }

    private static String parse(String type, String name) {
        return type + "_" + name;
    }
}
