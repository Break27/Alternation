/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.system.misc;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.OrderedMap;

/**
 *
 * @author break27
 */
public class SkinBuilder implements Json.Serializable {
    private OrderedMap<String, SkinStyle> Classes;
    private SkinStyle Styles;
    private SkinAttribute Attributes;
    
    
    public SkinBuilder() {
        Classes = new OrderedMap();
    }
    
    public void startStyle() {
        Styles = new SkinStyle();
    }
    
    public void endStyle(String name) {
        Classes.put(name, Styles);
    }
    
    public void startAttribute() {
        Attributes = new SkinAttribute();
    }
    
    public void setAttribute(String name, Object value) {
        Attributes.put(name, value);
    }
    
    public void endAttribute(String name) {
        Styles.put(name, Attributes);
    }

    @Override
    public void write(Json json) {
        Classes.forEach(styles -> {
            json.writeValue(styles.key, styles.value);
        });
    }

    @Override
    public void read(Json json, JsonValue jv) {
    }
}

class SkinStyle implements Json.Serializable {
    private OrderedMap<String, SkinAttribute> Styles = new OrderedMap();
    
    public void put(String name, SkinAttribute attri) {
        Styles.put(name, attri);
    }
    
    @Override
    public void write(Json json) {
        Styles.forEach(attri -> {
            json.writeValue(attri.key, attri.value);
        });
    }

    @Override
    public void read(Json json, JsonValue jv) {
    }
}

class SkinAttribute implements Json.Serializable {
    private OrderedMap<String, Object> Attributes = new OrderedMap();
    
    public void put(String name, Object value) {
        Attributes.put(name, value);
    }
    
    @Override
    public void write(Json json) {
        Attributes.forEach(a -> {
            json.writeValue(a.key, a.value);
        });
    }

    @Override
    public void read(Json json, JsonValue jv) {
    }
}