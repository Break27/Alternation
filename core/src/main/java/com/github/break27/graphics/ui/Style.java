/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.graphics.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import java.util.HashMap;

/**
 *
 * @author break27
 */
public class Style {
    private static final class External {
        private static final HashMap<String, TextureAtlas> AtlasMap = new HashMap();
    }
    
    private static final class Internal {
        private static final HashMap<String, TextureAtlas> AtlasMap = new HashMap();
    }
    
    /**Add Atlas to the Map.
     * @param name Atlas name. It overwrites the original one if name duplicates.
     * @param atlas
     */
    public static void putAtlas(String name, TextureAtlas atlas) {
        if(!External.AtlasMap.containsKey(name))
            External.AtlasMap.put(name, atlas);
        else
            External.AtlasMap.replace(name, atlas);
    }
    
    public static Drawable get(String stylename) {
        String[] val = stylename.split("::", 2);
        String atlas = "alter";
        String name = "icon20-info-wb";
        if(val.length == 1) {
            name = val[0];
        } else if(val.length == 2) {
            atlas = val[0];
            name = val[1];
        }
        return get(atlas, name);
    }
    
    /**To get the Atlas of the Map.
     * It attempts to search for the target in the External Map first.
     * It turns to the Internal Map if failed.
     * @param atlasname
     * @param widgetname
     * @return Drawable
     */
    public static Drawable get(String atlasname, String widgetname) {
        if(External.AtlasMap.containsKey(atlasname))
            return get(External.AtlasMap.get(atlasname), widgetname, false);
        else if(Internal.AtlasMap.containsKey(atlasname))
            return get(Internal.AtlasMap.get(atlasname), widgetname, false);
        return get(Internal.AtlasMap.get("alter"), "icon20-question", false);
    }
    
    static Drawable get(TextureAtlas atlas, String widgetname, boolean useFallback) {
        if(atlas.findRegion(widgetname) == null) {
            if(useFallback) return get(Internal.AtlasMap.get("alter"), "icon20-question", false);
            return get(Internal.AtlasMap.get("alter"), widgetname, true);
        }
        return new Image(atlas.createSprite(widgetname)).getDrawable();
    }
    
    public static void apply() {
        StyleProvider.applyAllStyle();
    }
    
    public static void apply(AlternativeWidget widget) {
        StyleProvider.applyStyle(widget);
    }
    
    public static void apply(String tag) {
        StyleProvider.applyStyle(tag);
    }
    
    public static void apply(AlternativeWidget widget, String tag) {
        StyleProvider.applyStyle(widget, tag);
    }
    
    public static TextureAtlas getAtlas(String name) {
        return External.AtlasMap.get(name);
    }
    
    public static void loadDefault() {
        Internal.AtlasMap.put("alter", new TextureAtlas(Gdx.files.internal("ui/alter/skin.atlas")));
    }
    
    public static void clear() {
        Internal.AtlasMap.values().forEach(atlas -> {
            atlas.dispose();
        });
        External.AtlasMap.values().forEach(atlas -> {
            atlas.dispose();
        });
    }
}
