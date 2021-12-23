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
    
    public static final class Widgets {
        
        static final HashMap<Class, Array<AlternativeWidget>> Map = new HashMap();
        
        private static final class Methods {
            private static final int STYLE = 0;
            private static final int LOCALE = 1;
        }
        
        /**
         * @author break27
         */
        public static void applyStyle() {
            apply(null, null, Methods.STYLE);
        }

        public static void applyStyle(AlternativeWidget widget) {
            apply(widget, null, Methods.STYLE);
        }

        public static void applyStyle(Class type) {
            apply(null, type, Methods.STYLE);
        }
        
        public static void applyStyle(AlternativeWidget widget, Class type) {
            apply(widget, type, Methods.STYLE);
        }

        /**
         * @author break27
         */
        public static void applyLocale() {
            apply(null, null, Methods.LOCALE);
        }

        public static void applyLocale(AlternativeWidget widget) {
            apply(widget, null, Methods.LOCALE);
        }

        public static void applyLocale(Class type) {
            apply(null, type, Methods.LOCALE);
        }
        
        public static void applyLocale(AlternativeWidget widget, Class type) {
            apply(widget, type, Methods.LOCALE);
        }
        
        private static void apply(AlternativeWidget widget, Class type, int methodInvoke) {
            Map.forEach((Class C, Array<AlternativeWidget> A) -> {
                if(type == null || C.getName().equals(type.getName())) {
                    A.forEach((AlternativeWidget W) -> {
                        if(widget == null || widget.hashCode() == W.hashCode()) {
                            if(methodInvoke == Methods.STYLE) W.styleApply();
                            if(methodInvoke == Methods.LOCALE) W.localeApply();
                        }
                    });
                }
            });
        }
        
        public static void destroyAll() {
            Map.values().forEach(Widgets -> {
                for(AlternativeWidget widget : Widgets) {
                    widget.destroy();
                }
            });
        }
    }
    
    
    
    /** Enable style management on a widget.
     *  Every class could contain multiple instances (or Widgets).
     */
    public default void setStyleEnabled() {
        if(Widgets.Map.containsKey(this.getClass())) {
            Widgets.Map.get(this.getClass()).add(this);
        } else {
            Widgets.Map.put(this.getClass(), new Array(new AlternativeWidget[]{this}));
        }
        /* Apply to itself immediately */
        styleApply();
        localeApply();
    }
    
    /** Get default Skin.
     *  @return AlternativeSkin
     */
    public default AlternativeSkin getAlterSkin() {
        return Resource.getSkin("default");
    }
    
    public default AlternativeFont getAlterFont() {
        return Resource.getFont("default-font");
    }
    
    public void styleApply();
    
    public void localeApply();
    
    public void destroy();
}