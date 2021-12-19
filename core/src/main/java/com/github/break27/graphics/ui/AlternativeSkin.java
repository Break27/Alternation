/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.graphics.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.SerializationException;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.github.break27.system.Resource.SerializableResource;
import com.github.break27.system.misc.SkinBuilder;

/**
 *
 * @author break27
 */
public class AlternativeSkin extends Skin implements SerializableResource {
    
    Array<TextureAtlas> AtlasSet = new Array<>();
    
    public AlternativeSkin() {
    }
    
    public AlternativeSkin(FileHandle file) {
        load(file);
    }
    
    @Override
    public Drawable getDrawable(String name) {
        try {
            return super.getDrawable(name);
        } catch(GdxRuntimeException ignored) {
        }
        // fallback
        Gdx.app.log(getClass().getName(), "No Resource Found: " + name + ". Using fallback.");
        return super.getDrawable("icon20-question");
    }
    
    @Override
    public void load (FileHandle xmlFile) {
        String jsonString = jsonBuild(xmlFile);
        try {
            getJsonLoader(xmlFile).fromJson(Skin.class, jsonString);
        } catch (SerializationException ex) {
            throw new SerializationException("Error reading file: " + xmlFile, ex);
        }
    }
    
    @Override
    public void addRegions(TextureAtlas atlas) {
        super.addRegions(atlas);
        AtlasSet.add(atlas);
    }
    
    @Override
    public void dispose() {
        super.dispose();
        // dispose all atlases
        for(int i=0; i<AtlasSet.size; i++) {
            AtlasSet.pop().dispose();
        }
    }
    
    private String jsonBuild(FileHandle xmlFile) {
        XmlReader reader = new XmlReader();
        Json json = new Json();
        // build json string
        SkinBuilder builder = parseXml(xmlFile, reader, new SkinBuilder());
        return json.toJson(builder);
    }
    
    private SkinBuilder parseXml(FileHandle xmlFile, XmlReader reader, final SkinBuilder builder) {
        Element root = reader.parse(xmlFile);
        /* Includes */
        if(root.hasChild("include")) {
            Array<Element> includes = root.getChildrenByNameRecursively("include");
            includes.forEach(element -> {
                FileHandle file = null;
                if(element.hasAttribute("internal")) {
                    file = Gdx.files.internal(element.getAttribute("internal"));
                } else if(element.hasAttribute("external")) {
                    file = Gdx.files.external(element.getAttribute("external"));
                }
                
                if(file != null && file.exists()) {
                    // recursive
                    if(!file.equals(xmlFile)) parseXml(file, reader, builder);
                    else Gdx.app.error(getClass().getName(), "Operation Unsupported: File is including itself! Ignored.");
                } else {
                    Gdx.app.error(getClass().getName(), "Include file not found: " + file + ". Ignored.");
                }
            });
        }
        /* TextureAtlas */
        FileHandle defaultFile = xmlFile.sibling(xmlFile.nameWithoutExtension() + ".atlas");
        if(root.hasChild("atlas")) {
            Element atlas = root.getChildByName("atlas");
            FileHandle sibling = xmlFile.sibling(atlas.getAttribute("location"));
            
            if(sibling.exists()) addRegions(new TextureAtlas(sibling));
            else throw new SerializationException("Atlas file not found: " + sibling);
            
        } else if(defaultFile.exists()) {
            addRegions(new TextureAtlas(defaultFile));
        } else {
            throw new SerializationException("Invaild skin file: No .atlas file found.");
        }
        /* Parse <Class> element */
        if(root.hasChild("class")) {
            Array<Element> classes = root.getChildrenByNameRecursively("class");
            classes.forEach(element -> {
                builder.startStyle();

                for(int i=0; i < element.getChildCount(); i++) {
                    builder.startAttribute();
                    Element child = element.getChild(i);

                    for(int j=0; j < child.getChildCount(); j++) {
                        Element grandchild = child.getChild(j);
                        builder.setAttribute(grandchild.getName(), grandchild.getText());
                    }

                    builder.endAttribute(child.getName());
                }

                builder.endStyle(element.getAttribute("name"));
            });
        }
        return builder;
    }
}