/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.system;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.github.break27.graphics.ui.AlternativeFont;
import com.github.break27.graphics.ui.AlternativeSkin;
import com.github.break27.graphics.ui.AlternativeWidget;
import com.kotcrab.vis.ui.VisUI;
import java.util.HashMap;
import java.util.Optional;

/**
 *
 * @author break27
 */
public class Resource {
    public static interface SerializableResource extends Disposable {
        
    }
    
    public static final class DefaultType {
        public static final String SKIN = "skin";
        public static final String FONT = "font";
    }
    
    private static final HashMap<String, SerializableResource> Resources = new HashMap();

    public static AlternativeSkin getSkin(String name) {
        return (AlternativeSkin) getResource(DefaultType.SKIN, name, AlternativeSkin.class);
    }
    
    public static AlternativeFont getFont(String name) {
        if(has(DefaultType.FONT, name))
            return (AlternativeFont) getResource(DefaultType.FONT, name, AlternativeFont.class);
        Gdx.app.error(Resource.class.getName(), "No font \"" + name + "\" found. Using default.");
        return new AlternativeFont(VisUI.getSkin().getFont("default-font"));
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
    
    static void dispose() {
        Resources.values().forEach(resource -> {
            resource.dispose();
        });
    }
    
    static void loadSkin(String name, AlternativeSkin skin) {
        load(DefaultType.SKIN, name, skin);
    }
    
    static void loadFont(String name, AlternativeFont font) {
        load(DefaultType.FONT, name, font);
    }
    
    private static void load(String typename, String name, SerializableResource resource) {
        String fullname = parse(typename, name);
        if(Resources.containsKey(fullname)) {
            Resources.replace(fullname, resource);
        } else {
            Resources.put(fullname, resource);
        }
    }
    
    private static String parse(String typename, String name) {
        return typename + "@" + name;
    }
}