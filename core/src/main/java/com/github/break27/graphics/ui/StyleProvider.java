/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.graphics.ui;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import java.util.HashMap;

/**
 *
 * @author break27
 */
public class StyleProvider {
    HashMap<String, String> StyleMap;
    private static final HashMap<AlternativeWidget, HashMap<String, String>> Providers = new HashMap();
    
    StyleProvider(AlternativeWidget widget) {
        Providers.put(widget, StyleMap = new HashMap<>());
    }
    
    public void setStyle(String tag, String stylename) {
        StyleMap.put(tag, stylename);
    }
    
    public Drawable getStyle(String tag) {
        return Style.get(StyleMap.get(tag));
    }
    
    static void applyAllStyle() {
        applyStyle(null, null);
    }
    
    static void applyStyle(AlternativeWidget widget) {
        applyStyle(widget, null);
    }
    
    static void applyStyle(String tag) {
        applyStyle(null, tag);
    }
    
    static void applyStyle(AlternativeWidget widget, String tag) {
        Providers.forEach((AlternativeWidget Widgets, HashMap Maps) -> {
            if(widget == null || Widgets.getClass().getName().equals(widget.getClass().getName())) {
                if(tag == null || Maps.containsKey(tag)) {
                    Widgets.styleApply();
                }
            }
        });
    }
}
