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
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *************************************************************************/

package com.github.break27.graphics.ui;

import com.badlogic.gdx.utils.Array;
import com.github.break27.system.AlterAssetManager;

import java.util.HashMap;

/**
 * @author break27
 */
public abstract class Widgets {
    protected static final HashMap<Class<?>, Array<AlternativeWidget>> Map = new HashMap<>();
    protected static AlterAssetManager temporaryAssets;

    private enum Method {
        ALL(),
        STYLE(),
        LOCALE(),
        AUDIO()
    }

    static boolean assetsSet() {
        return (temporaryAssets != null);
    }

    public static void init(AlterAssetManager manager) {
        temporaryAssets = manager;
    }

    public static void applyAll(AlterAssetManager assets) {
        apply(assets, null, null, Method.ALL);
    }

    public static void applyStyle(AlterAssetManager assets) {
        applyStyle(assets, null, null);
    }

    public static void applyStyle(AlterAssetManager assets, AlternativeWidget widget) {
        applyStyle(assets, widget, null);
    }

    public static void applyStyle(AlterAssetManager assets, Class<?> type) {
        applyStyle(assets, null, type);
    }

    /** Apply style attribute to a specific widget with
     * a specific type.
     * */
    public static void applyStyle(AlterAssetManager assets, AlternativeWidget widget, Class<?> type) {
        apply(assets, widget, type, Method.STYLE);
    }

    public static void applyLocale() {
        applyLocale(null, null);
    }

    public static void applyLocale(LocalizableWidget widget) {
        applyLocale(widget, null);
    }

    public static void applyLocale(Class<?> type) {
        applyLocale(null, type);
    }

    /** Apply locale attribute to a specific widget
     * with a specific type.
     * */
    public static void applyLocale(LocalizableWidget widget, Class<?> type) {
        apply(null, widget, type, Method.LOCALE);
    }

    public static void applyAudio(AlterAssetManager assets) {
        applyAudio(assets, null, null);
    }

    public static void applyAudio(AlterAssetManager assets, AudioAppliedWidget widget) {
        applyAudio(assets, widget, null);
    }

    public static void applyAudio(AlterAssetManager assets, Class<?> type) {
        applyAudio(assets, null, type);
    }

    /** Apply audio attribute to a specific widget
     * with a specific type.
     * */
    public static void applyAudio(AlterAssetManager assets, AudioAppliedWidget widget, Class<?> type) {
        apply(assets, widget, type, Method.AUDIO);
    }

    /** Apply specific attributes to specific widgets.
     *
     * */
    private static void apply(AlterAssetManager assets, AlternativeWidget widget, Class<?> type, Method method) {
        Map.forEach((Class<?> C, Array<AlternativeWidget> A) -> {
            // check if the specific type is of the same class
            if (type == null || C.getName().equals(type.getName())) {
                A.forEach((AlternativeWidget W) -> {
                    // check if hashcode equals
                    if (widget == null || widget.hashCode() == W.hashCode()) {
                        if (method == Method.ALL || method == Method.STYLE) {
                            if (W instanceof StyleAppliedWidget)
                                ((StyleAppliedWidget) W).styleApply(assets.getSkin());
                        }
                        if (method == Method.ALL || method == Method.LOCALE) {
                            if (W instanceof LocalizableWidget)
                                ((LocalizableWidget) W).localeApply();
                        }
                        if (method == Method.ALL || method == Method.AUDIO) {
                            if (W instanceof AudioAppliedWidget) {
                                ((AudioAppliedWidget) W).audioApply(assets.getMusic(), assets.getSound());
                            }
                        }
                    }
                });
            }
        });
    }

    /** Destroy all registered widgets.
     *
     * */
    public static void destroyAll() {
        Map.values().forEach(Widgets -> {
            for(AlternativeWidget widget : Widgets) {
                if(widget != null) widget.destroy();
            }
        });
    }
}
