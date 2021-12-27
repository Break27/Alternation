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

package com.github.break27.graphics.ui;

import com.badlogic.gdx.utils.Array;
import com.github.break27.system.Resource;

import java.util.HashMap;

/**
 *
 * @author break27
 */
public interface AlternativeWidget {
    
    final class Widgets {
        
        private static final HashMap<Class, Array<AlternativeWidget>> Map = new HashMap<>();
        
        private static final class Methods {
            private static final int STYLE = 0;
            private static final int LOCALE = 1;
            private static final int AUDIO = 2;
        }
        
        /** Apply style attributes to all widgets
         *
         * */
        public static void applyStyle() {
            applyStyle(null, null);
        }

        public static void applyStyle(AlternativeWidget widget) {
            applyStyle(widget, null);
        }

        public static void applyStyle(Class type) {
            applyStyle(null, type);
        }

        /** Apply style attribute to a specific widget with
         * a specific type.
         * */
        public static void applyStyle(AlternativeWidget widget, Class type) {
            apply(widget, type, Methods.STYLE);
        }

        /** Apply locale attribute to all widgets.
         *
         * */
        public static void applyLocale() {
            applyLocale(null, null);
        }

        public static void applyLocale(LocalizableWidget widget) {
            applyLocale(widget, null);
        }

        public static void applyLocale(Class type) {
            applyLocale(null, type);
        }

        /** Apply locale attribute to a specific widget
         * with a specific type.
         * */
        public static void applyLocale(LocalizableWidget widget, Class type) {
            apply(widget, type, Methods.LOCALE);
        }

        /** Apply audio attribute to all widgets.
         *
         * */
        public static void applyAudio() {
            applyAudio(null, null);
        }

        public static void applyAudio(AudioAppliedWidget widget) {
            applyAudio(widget, null);
        }

        public static void applyAudio(Class type) {
            applyAudio(null, type);
        }

        /** Apply audio attribute to a specific widget
         * with a specific type.
         * */
        public static void applyAudio(AudioAppliedWidget widget, Class type) {
            apply(widget, type, Methods.AUDIO);
        }

        /** Apply specific attributes to specific widgets.
         *
         * */
        private static void apply(AlternativeWidget widget, Class type, int invoke) {
            Map.forEach((Class C, Array<AlternativeWidget> A) -> {
                // check if the specific type is of the same class
                if(type == null || C.getName().equals(type.getName())) {
                    A.forEach((AlternativeWidget W) -> {
                        // check if hashcode equals
                        if(widget == null || widget.hashCode() == W.hashCode()) {
                            if(invoke == -1 || invoke == Methods.STYLE) {
                                if(W instanceof StyleAppliedWidget)
                                    ((StyleAppliedWidget) W).styleApply();
                            }
                            if(invoke == -1 || invoke == Methods.LOCALE) {
                                if(W instanceof LocalizableWidget)
                                    ((LocalizableWidget) W).localeApply();
                            }
                            if(invoke == -1 || invoke == Methods.AUDIO) {
                                if(W instanceof AudioAppliedWidget) {
                                    ((AudioAppliedWidget) W).audioApply();
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
                    widget.destroy();
                }
            });
        }
    }
    
    /** Register a widget to Map.
     */
    default void register() {
        if(Widgets.Map.containsKey(this.getClass())) {
            Widgets.Map.get(this.getClass()).add(this);
        } else {
            Widgets.Map.put(this.getClass(), new Array<>(new AlternativeWidget[]{this}));
        }
        // Apply to itself immediately.
        Widgets.apply(this, this.getClass(), -1);
    }
    
    /** Get default Skin.
     *  @return AlternativeSkin
     */
    default AlternativeSkin getAlterSkin() {
        return Resource.getSkin("default");
    }

    /** Get default Font.
     * @return AlternativeFont
     * */
    default AlternativeFont getAlterFont() {
        return Resource.getFont("default-font");
    }

    /** Destroy widget. Note: Normally it would not
     * remove the widget from the stage directly.
     * */
    void destroy();
}