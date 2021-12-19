/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.system;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.github.break27.graphics.ui.AlternativeSkin;
import com.github.break27.graphics.ui.AlternativeWidget;
import java.util.HashMap;

/**
 *
 * @author break27
 */
public class Resource {
    public static interface SerializableResource extends Disposable {
        
    }
    
    public static final class DefaultType {
        public static final String SKIN = "skin";
    }
    
    private static final class Methods {
        private static final int STYLE = 0;
        private static final int LOCALE = 1;
    }
    
    private static final HashMap<String, SerializableResource> Resources = new HashMap();

    public static AlternativeSkin getSkin(String name) {
        return (AlternativeSkin)getResource(DefaultType.SKIN, name, AlternativeSkin.class);
    }
    
    public static SerializableResource getResource(String type, String name, Class clazz) {
        if(type == null || name == null || clazz == null) throw new IllegalArgumentException("type, name or class cannot be null.");
        if(!has(type, name)) throw new GdxRuntimeException("No resource registered with name: " + name);

        SerializableResource resource = Resources.get(parse(type, name));
        if(resource.getClass() != clazz) throw new GdxRuntimeException("Resource type mismatched. \"" + name +
                "\" is registered with class: " + resource.getClass());
        return resource;
    }
    
    public static boolean has(String typename, String name) {
        if(Resources.get(parse(typename, name)) == null) return false;
        return Resources.containsKey(parse(typename, name));
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
    
    public static void applyStyle(Class clazz) {
        apply(null, clazz, Methods.STYLE);
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
    
    public static void applyLocale(Class clazz) {
        apply(null, clazz, Methods.LOCALE);
    }
    
    static void dispose() {
        Resources.values().forEach(resource -> {
            resource.dispose();
        });
    }
    
    static void loadSkin(String name, AlternativeSkin skin) {
        load(DefaultType.SKIN, name, skin);
    }
    
    private static void load(String typename, String name, SerializableResource resource) {
        String fullname = parse(typename, name);
        if(Resources.containsKey(fullname)) {
            Resources.replace(fullname, resource);
        } else {
            Resources.put(fullname, resource);
        }
    }
    
    private static void apply(AlternativeWidget widget, Class clazz, int methodinvoke) {
        AlternativeWidget.WidgetsMap.forEach((Class C, AlternativeWidget W) -> {
            if(widget == null || W.hashCode() == widget.hashCode()) {
                if(clazz == null || C.getName().equals(clazz.getName())) {
                    if(methodinvoke == Methods.STYLE) W.styleApply();
                    if(methodinvoke == Methods.LOCALE) W.localeApply();
                }
            }
        });
    }
    
    private static String parse(String typename, String name) {
        return typename + "@" + name;
    }
}