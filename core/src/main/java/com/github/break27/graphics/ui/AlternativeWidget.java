/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
        
        public static void destroyAll() {
            WidgetsMap.values().forEach(Widgets -> {
                for(AlternativeWidget widget : Widgets) {
                    widget.destroy();
                }
            });
        }
    }
    
    static final HashMap<Class, Array<AlternativeWidget>> WidgetsMap = new HashMap();
    
    /** Enable style management on a widget.
     *  Every class could contain multiple instances (or Widgets).
     */
    public default void setStyleEnabled() {
        if(WidgetsMap.containsKey(this.getClass())) {
            WidgetsMap.get(this.getClass()).add(this);
        } else {
            WidgetsMap.put(this.getClass(), new Array(new AlternativeWidget[]{this}));
        }
        /* Apply to itself immediately */
        styleApply();
        localeApply();
    }
    
    /** Get default Skin.
     *  @return AlternativeSkin
     */
    public default AlternativeSkin getAlterSkin() {
        return Resource.getSkin("alter");
    }
    
    public default AlternativeFont getAlterFont() {
        return Resource.getFont("default-font");
    }
    
    public void styleApply();
    
    public void localeApply();
    
    public void destroy();
}